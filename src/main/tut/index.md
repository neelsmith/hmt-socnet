# "Social networks" in HMT texts


Step 1:  we work with archival XML source from the Homer Multitext project archive to [index occurrences of proper names in *scholia*](./indexNetwork.md).

Step 2:  count occurrences of each name.

```tut:silent
import scala.io.Source

val src = "src/main/tut/resources/pn-index.cex"
val lines = Source.fromFile(src).getLines.toVector
val stringPairs = lines.map( ln => { val arr = ln.split("#"); (arr(0), arr(1))})
val byName  = stringPairs.groupBy( _._2)
val nameCounts = byName.map{case (k,v) => (k -> v.size) }
```

Step 3:  count co-occurrences of names.

```tut
// Cooccurence of two names
case class Pairing(n1: String, n2: String)

val byPassage = stringPairs.groupBy( _._1)
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
```

Step 4:  write out GML for use in gephi.
