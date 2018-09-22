# Load HMT achival data


We need a lot of libraries:

```scala
import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.homermultitext.edmodel._
```


Create a `TextRepository`:

```scala
val scholiaComposites = "src/main/tut/resources/"
val catalog = s"${scholiaComposites}/ctscatalog.cex"
val citation = s"${scholiaComposites}/citationconfig.cex"

val repo = TextRepositorySource.fromFiles(catalog,citation,scholiaComposites)
```


Collect comment nodes and tokenize:

```scala
val scholiaCorpus = Corpus(repo.corpus.nodes.filter(_.urn.passageComponent.endsWith("comment")))
val tokens = TeiReader.fromCorpus(scholiaCorpus)
```

Select tokens analyzed as personal names.

```scala
scala> val pns = tokens.filter(_.analysis.lexicalDisambiguation ~~ Cite2Urn("urn:cite2:hmt:pers:"))
pns: scala.collection.immutable.Vector[org.homermultitext.edmodel.TokenAnalysis] = Vector(TokenAnalysis(urn:cts:greekLit:tlg5026.msAint.hmt_xml:1.32.comment,HmtToken(urn:cite2:hmt:va_schAint_tokens:tkn31,urn:cts:greekLit:tlg5026.msAint.hmt_xml:1.32.comment@Αιγιαλέως[1],urn:cts:greekLit:tlg5026.msAint.hmt_xml.tokens:1.32.comment.31,grc,Vector(Reading(Αιγιαλέως,Clear)),LexicalToken,urn:cite2:hmt:pers.r1:pers120,None,DirectVoice,None,ArrayBuffer())), TokenAnalysis(urn:cts:greekLit:tlg5026.msAint.hmt_xml:1.32.comment,HmtToken(urn:cite2:hmt:va_schAint_tokens:tkn34,urn:cts:greekLit:tlg5026.msAint.hmt_xml:1.32.comment@Ἰνάχου[1],urn:cts:greekLit:tlg5026.msAint.hmt_xml.tokens:1.32.comment.34,grc,Vector(Reading(Ἰνάχου,Clear)),LexicalToken,urn:cite2:hmt:pers.r1:pers121,No...
```

Write the scholion's URN, and the URN for the person to a delimited-text file:

```
val prs = pns.map( tkn => s"${tkn.analysis.sourceUrn.dropSubref}#${ tkn.analysis.lexicalDisambiguation}")

import java.io.PrintWriter
new PrintWriter("pn-index.cex"){write(prs.mkString("\n")); close;}
```
