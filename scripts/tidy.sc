import edu.holycross.shot.cite._
import scala.io.Source

val src = "src/main/tut/resources/pn-index-cleaned.cex"
val lines = Source.fromFile(src).getLines.toVector
val occursStrings = lines.map( ln => { val arr = ln.split("#");  (arr(0), arr(1))})


def collectErrors(occurencesRaw: Vector[(String, String)]) = {
  val errorList = for (pr <- occurencesRaw) yield {
    val psg = pr._1
    val pers = pr._2
    try {
      val personUrn = Cite2Urn(pers)
      require(personUrn.namespace == "hmt")
      require(personUrn.collection == "pers")
      require(personUrn.version == "v1")
      val objId = personUrn.objectComponent.replaceFirst("pers.", "")
      require(objId.forall(_.isDigit))

      None
    } catch {
      case _ : Throwable => {
        println("BAD STRING: " + pers + " in passage " + psg)
        Option(pers)
      }
    }
  }
  errorList.flatten
}

def authList = {
  val srcLines = Source.fromFile("src/main/tut/resources/name-labels.cex").getLines.toVector
  for (ln <- srcLines) yield {
    val cols = ln.split("#")
    (cols(0), cols(1))
  }
}

val labelMap = authList

def label(u: String)  = {
  val matches = labelMap.filter(_._1 == u)
  matches.size match {
    case 0 => None
    case 1 => Option(matches(0)._2)
    case _ => { println("Multiple matches for " + u) ;  None}
  }
}
def notInAuthList(occurencesRaw: Vector[(String, String)]) : Vector[String] = {
  val missing = for (raw <- occurencesRaw) yield {
    label(raw._1)
  }
  missing.flatten
}

require(notInAuthList(occursStrings).isEmpty)
