
// must be at least 2.11 to use hmt_textmodel
scalaVersion := "2.12.4"

enablePlugins(TutPlugin)
tutTargetDirectory := file("docs")

resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith","maven")
libraryDependencies ++=   Seq(
  "edu.holycross.shot.cite" %% "xcite" % "3.6.0",
  "edu.holycross.shot" %% "ohco2" % "10.9.0",
  "edu.holycross.shot" %% "orca" % "4.2.0",
  "edu.holycross.shot" %% "scm" % "6.1.1",

  "edu.holycross.shot" %% "greek" % "1.4.0",
  "edu.holycross.shot" %% "gsphone" % "1.1.0",
  "org.homermultitext" %% "hmt-textmodel" % "4.0.0"
)
