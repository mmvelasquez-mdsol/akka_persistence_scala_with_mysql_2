name := "akka_persistence_scala"

version := "1.0"

scalaVersion := "2.12.2"

val akkaVersion = "2.5.2"

scalacOptions := Seq("-encoding", "utf8", "-feature", "-language:postfixOps")

resolvers += Resolver.jcenterRepo

// Akka Persistence Plugin
resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= {
  val akkaPersistenceJdbcVersion = "3.4.0"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
    // Begin: Akka Persistence Plugin
    "com.github.dnvriend" %% "akka-persistence-jdbc" % akkaPersistenceJdbcVersion,
    "mysql" % "mysql-connector-java" % "5.1.39",
    // End: Akka Persistence Plugin
    "org.scalatest" %% "scalatest" % "3.0.1",
    "org.mockito" % "mockito-all" % "1.10.19",
    "com.github.dnvriend" %% "akka-persistence-inmemory" % "2.5.1.1",
    "org.json4s" %% "json4s-native" % "3.6.0",
    "com.trueaccord.scalapb" %% "scalapb-json4s" % "0.3.3"
  )
}
