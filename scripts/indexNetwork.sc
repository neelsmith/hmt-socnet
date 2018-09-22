import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.homermultitext.edmodel._

val scholiaComposites = "src/main/tut/resources/"

val catalog = s"${scholiaComposites}/ctscatalog.cex"
val citation = s"${scholiaComposites}/citationconfig.cex"

val repo = TextRepositorySource.fromFiles(catalog,citation,scholiaComposites)
val scholiaCorpus = Corpus(repo.corpus.nodes.filterNot(_.urn.passageComponent.endsWith("ref")))
val tokens = TeiReader.fromCorpus(scholiaCorpus)

val pns = tokens.filter(_.analysis.lexicalDisambiguation ~~ Cite2Urn("urn:cite2:hmt:pers:"))

val prs = pns.map( tkn => s"${tkn.analysis.sourceUrn.dropSubref}#${ tkn.analysis.lexicalDisambiguation}")


import java.io.PrintWriter
new PrintWriter("pn-index.cex"){write(prs.mkString("\n")); close;}
