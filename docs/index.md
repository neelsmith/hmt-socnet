# "Social networks" in HMT texts


Step 1:  we work with archival XML source from the Homer Multitext project archive to [index occurrences of proper names in *scholia*](./indexNetwork.md).

Step 2:  count occurrences of each name.

```scala
import scala.io.Source

val src = "src/main/tut/resources/pn-index.cex"
val lines = Source.fromFile(src).getLines.toVector
val stringPairs = lines.map( ln => { val arr = ln.split("#"); (arr(0), arr(1))})
val byName  = stringPairs.groupBy( _._2)
val nameCounts = byName.map{case (k,v) => (k -> v.size) }
```

Step 3:  count co-occurrences of names.

```scala
scala> // Cooccurence of two names
     | case class Pairing(n1: String, n2: String)
defined class Pairing

scala> val byPassage = stringPairs.groupBy( _._1)
byPassage: scala.collection.immutable.Map[String,scala.collection.immutable.Vector[(String, String)]] = Map(urn:cts:greekLit:tlg5026.msAim.hmt_xml:18.64.comment -> Vector((urn:cts:greekLit:tlg5026.msAim.hmt_xml:18.64.comment,urn:cite2:hmt:pers.r1:pers16)), urn:cts:greekLit:tlg5026.msA.hmt_xml:4.795.comment -> Vector((urn:cts:greekLit:tlg5026.msA.hmt_xml:4.795.comment,urn:cite2:hmt:pers.r1:pers75), (urn:cts:greekLit:tlg5026.msA.hmt_xml:4.795.comment,urn:cite2:hmt:pers.r1:pers75)), urn:cts:greekLit:tlg5026.msAint.hmt_xml:10.3523.comment -> Vector((urn:cts:greekLit:tlg5026.msAint.hmt_xml:10.3523.comment,urn:cite2:hmt:pers.r1:pers118)), urn:cts:greekLit:tlg5026.msA.hmt_xml:15.96.comment -> Vector((urn:cts:greekLit:tlg5026.msA.hmt_xml:15.96.comment,urn:cite2:hmt:per...

scala> val psgs = byPassage.keySet.toVector
psgs: Vector[String] = Vector(urn:cts:greekLit:tlg5026.msAim.hmt_xml:18.64.comment, urn:cts:greekLit:tlg5026.msA.hmt_xml:4.795.comment, urn:cts:greekLit:tlg5026.msAint.hmt_xml:10.3523.comment, urn:cts:greekLit:tlg5026.msA.hmt_xml:15.96.comment, urn:cts:greekLit:tlg5026.msA.hmt_xml:14.G2.comment, urn:cts:greekLit:tlg5026.msA.hmt_xml:2.665.comment, urn:cts:greekLit:tlg5026.msAint.hmt_xml:4.263.comment, urn:cts:greekLit:tlg5026.msA.hmt_xml:1.67.comment, urn:cts:greekLit:tlg5026.msAint.hmt_xml:15.38.comment, urn:cts:greekLit:tlg5026.msA.hmt_xml:6.57.lemma, urn:cts:greekLit:tlg5026.msA.hmt_xml:2.127.comment, urn:cts:greekLit:tlg5026.msA.hmt_xml:15.118.comment, urn:cts:greekLit:tlg5026.msA.hmt_xml:24.12.comment, urn:cts:greekLit:tlg5026.msAint.hmt_xml:18.15.comment, ...

scala> val pairings = for (psg <- psgs) yield {
     |   val names = byPassage(psg).map(_._2)
     |   val pairList = for (n1 <- names) yield {
     |     val pairedUp = for (n2 <- names) yield {
     |       Pairing(n1,n2)
     |     }
     |     pairedUp
     |   }
     |   pairList.flatten
     | }
pairings: scala.collection.immutable.Vector[scala.collection.immutable.Vector[Pairing]] = Vector(Vector(Pairing(urn:cite2:hmt:pers.r1:pers16,urn:cite2:hmt:pers.r1:pers16)), Vector(Pairing(urn:cite2:hmt:pers.r1:pers75,urn:cite2:hmt:pers.r1:pers75), Pairing(urn:cite2:hmt:pers.r1:pers75,urn:cite2:hmt:pers.r1:pers75), Pairing(urn:cite2:hmt:pers.r1:pers75,urn:cite2:hmt:pers.r1:pers75), Pairing(urn:cite2:hmt:pers.r1:pers75,urn:cite2:hmt:pers.r1:pers75)), Vector(Pairing(urn:cite2:hmt:pers.r1:pers118,urn:cite2:hmt:pers.r1:pers118)), Vector(Pairing(urn:cite2:hmt:pers.r1:pers348,urn:cite2:hmt:pers.r1:pers348)), Vector(Pairing(urn:cite2:hmt:pers.r1:pers172,urn:cite2:hmt:pers.r1:pers172)), Vector(Pairing(urn:cite2:hmt:pers.r1:pers469,urn:cite2:hmt:pers.r1:pers469), Pairing...
```

Step 4:  write out GML for use in gephi.
