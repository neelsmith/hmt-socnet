# Tidy up data


Run this `collectErrors` function on list of occurrences until no errors occur.

```tut:silent
import edu.holycross.shot.cite._
import scala.io.Source

val src = "src/main/tut/resources/pn-index-cleaned.cex"
val lines = Source.fromFile(src).getLines.toVector
val occursStrings = lines.map( ln => { val arr = ln.split("#");  (arr(0), arr(1))})


def collectErrors(occurencesRaw: Vector[(String, String)]) :  Vector[String]= {
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

val errors = collectErrors(occursStrings)
require(errors.isEmpty)
```
