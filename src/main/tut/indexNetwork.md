# Load HMT achival data


We need a lot of libraries:

```tut:silent
import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.homermultitext.edmodel._
```


Create a `TextRepository`:

```tut:silent
val scholiaComposites = "src/main/tut/resources/"
val catalog = s"${scholiaComposites}/ctscatalog.cex"
val citation = s"${scholiaComposites}/citationconfig.cex"

val repo = TextRepositorySource.fromFiles(catalog,citation,scholiaComposites)
```


Collect comment nodes and tokenize:

```tut:silent
val scholiaCorpus = Corpus(repo.corpus.nodes.filter(_.urn.passageComponent.endsWith("comment")))
val tokens = TeiReader.fromCorpus(scholiaCorpus)
```

Select tokens analyzed as personal names.

```tut
val pns = tokens.filter(_.analysis.lexicalDisambiguation ~~ Cite2Urn("urn:cite2:hmt:pers:"))
```

Write the scholion's URN, and the URN for the person to a delimited-text file:

```
val prs = pns.map( tkn => s"${tkn.analysis.sourceUrn.dropSubref}#${ tkn.analysis.lexicalDisambiguation}")

import java.io.PrintWriter
new PrintWriter("pn-index.cex"){write(prs.mkString("\n")); close;}
```
