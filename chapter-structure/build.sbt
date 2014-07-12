name := "structure"

version := "0.1-SNAPSHOT"

organization := "manning"

scalaVersion := "2.11.1"

libraryDependencies ++= {
  val akkaVersion       = "2.3.4"
  val sprayVersion      = "1.2-20130710"
  Seq(
    "com.typesafe.akka"       %%  "akka-actor"                     % akkaVersion,
    "com.typesafe.akka"       %%  "akka-slf4j"                     % akkaVersion,
    "com.typesafe.akka"       %%  "akka-testkit"                   % akkaVersion   % "test",
    "org.scalatest"           %% "scalatest"                       % "2.2.0"       % "test"
  )
}
