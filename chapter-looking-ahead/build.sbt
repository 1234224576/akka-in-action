name := "next"

version := "1.0"

organization := "com.manning"

scalaVersion := "2.11.7"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-Xlint",
  "-Ywarn-unused",
  "-Ywarn-dead-code",
  "-feature",
  "-language:_"
)

resolvers ++= Seq("Typesafe Snapshots" at "http://repo.akka.io/snapshots/",
                  "Sonatype snapshots"  at "http://oss.sonatype.org/content/repositories/snapshots/")

parallelExecution in Test := false

fork := true

libraryDependencies ++= {
  val akkaVersion       = "2.4-SNAPSHOT"
  Seq(
    "com.typesafe.akka"         %%  "akka-actor"                     % akkaVersion,
    "com.typesafe.akka"         %%  "akka-typed-experimental"        % akkaVersion,
    "com.typesafe.akka"         %%  "akka-slf4j"                     % akkaVersion,
    "com.typesafe.akka"         %%  "akka-persistence-experimental"  % akkaVersion,
    "com.typesafe.akka"         %%  "akka-cluster"                   % akkaVersion,
    "com.typesafe.akka"         %%  "akka-contrib"                   % akkaVersion,
    "com.typesafe.akka"         %%  "akka-testkit"                   % akkaVersion   % "test",
    "com.typesafe.akka"         %%  "akka-multi-node-testkit"        % akkaVersion   % "test",
    "com.typesafe.akka"         %%  "akka-stream-experimental"       % "1.0",
    "com.typesafe.akka"         %%  "akka-http-core-experimental"    % "1.0",
    "com.typesafe.akka"         %%  "akka-http-experimental"         % "1.0",
    "io.spray"                  %%  "spray-json"                     % "1.3.2",
    "commons-io"                %   "commons-io"                     % "2.4",
    "org.scalatest"             %%  "scalatest"                      % "2.2.4"       % "test",
    "ch.qos.logback"            %   "logback-classic"                % "1.1.2"
  )
}

// Assembly settings
mainClass in Global := Some("aia.persistence.sharded.ShardedMain")

jarName in assembly := "persistence-examples.jar"
