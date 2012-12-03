scalaVersion := "2.9.2"

organization := "org.gtri.util"

name := "iteratee.impl"

version := "1.0-SNAPSHOT"

scalacOptions ++= Seq("-unchecked", "-deprecation")

//resolvers += "iead-all" at "https://iead.ittl.gtri.org/artifactory/all" // need this for getting IEAD artifacts

libraryDependencies += "org.gtri.util" % "iteratee.api" % "1.0-SNAPSHOT"

//libraryDependencies += "org.scalaz" %% "scalaz-core" % "6.0.4"

publishTo <<= {    // set publish repository url according to whether `version` ends in "-SNAPSHOT"
  val releases = "iead-artifactory" at "https://iead.ittl.gtri.org/artifactory/internal"
  val snapshots = "iead-artifactory-snapshots" at "https://iead.ittl.gtri.org/artifactory/internal-snapshots"
  version { v =>
    if (v.endsWith("-SNAPSHOT"))
      Some(snapshots)
    else Some(releases)
  }
}