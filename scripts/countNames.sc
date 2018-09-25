import scala.io.Source

val src = "src/main/tut/resources/pn-index-cleaned.cex"
val lines = Source.fromFile(src).getLines.toVector
val stringPairs = lines.map( ln => { val arr = ln.split("#"); (arr(0), arr(1))})
val byName  = stringPairs.groupBy( _._2)
val nameCounts = byName.map{case (k,v) => (k -> v.size) }


val byPassage = stringPairs.groupBy( _._1)


// Cooccurence of two names
case class Pairing(n1: String, n2: String)

val psgs = byPassage.keySet.toVector
val pairings = for (psg <- psgs) yield {
  val names = byPassage(psg).map(_._2)
  val pairList = for (n1 <- names) yield {
    val pairedUp = for (n2 <- names) yield {
      Pairing(n1,n2)
    }
    pairedUp
  }
  pairList.flatten
}

val pairGrouped = pairings.flatten.groupBy( pr => pr)
val pairCounts = pairGrouped.map{case (k,v) => (k -> v.size) }




val preface = Vector("graph [", "comment \"Co-occurrence network in scholia\"","directed 0", "id hmt1","label \"Personal names in scholia of the Venetus A\"").mkString("\n\t") + "\n\t"
val trail = "]\n"

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

def nodes(nameCounts: Map[String, Int]) :  Vector[String] = {
  for (n <- nameCounts.keySet.toVector) yield {
    val labelString = label(n) match {
      case None => {println("Error: no label for " + n); n}
      case s: Some[String] => s.get
    }

    val count = nameCounts(n)
    val idVal = n.replaceFirst("urn:cite2:hmt:pers.v1:pers", "")
    //Previously checked for this in tidy.sc:
    //if (idVal.forall(_.isDigit)) {

    Vector("node [", "\tid " + idVal,  "\tlabel \"" + labelString + "\"", "\tfreqs " + count, "]").mkString("\n\t")

  }
}

def edges(pairCounts:  Map[Pairing, Int] ):  Vector[String] = {
    for (edg <- pairCounts.keySet.toVector) yield {
      val count = pairCounts(edg)

      // only output valid mappings
      if (edg.n1 == edg.n2) { "" } else if (nameCounts.keySet.contains(edg.n1) && nameCounts.keySet.contains(edg.n2)) {
        println(edg.n1 + "->" + edg.n2)
        Vector("\tedge [", "\tsource " + edg.n1.replaceFirst("urn:cite2:hmt:pers.v1:pers", ""), "\ttarget " + edg.n2.replaceFirst("urn:cite2:hmt:pers.v1:pers", ""), "\tweight " + count, "]").mkString("\n\t") + "\n"
      } else {
        println("ERROR:  node references do not have both of " + edg)
        ""
      }
  }
}


def gml :String = {
  preface + nodes(nameCounts).mkString("\n\t") + "\n" + edges(pairCounts).mkString("") + trail
}

import java.io.PrintWriter
def writeGML = {
  new PrintWriter("scholia-network.gml"){write(gml);close;}

}
