val ConfigVersion = "1.3.2"
val Http4sVersion = "0.19.0-M3"
val Specs2Version = "4.2.0"
val LogbackVersion = "1.2.3"
val FlywayVersion = "4.2.0"
val SlickVersion = "3.2.1"
val CirceVersion = "0.10.0"
val EnumeratumVersion = "1.5.13"
val ScalaTestVersion = "3.0.5"
lazy val DoobieVersion = "0.6.0"

lazy val root = (project in file("."))
  .settings(
    organization := "com.gymio",
    name := "gymio-service",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.7",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % ConfigVersion,
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-server" % Http4sVersion,
      "org.tpolecat" %% "doobie-core" % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres" % DoobieVersion,
      "org.tpolecat" %% "doobie-specs2" % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres"  % DoobieVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-generic-extras" % CirceVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.specs2" %% "specs2-core" % Specs2Version % "test",
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "org.flywaydb" % "flyway-core" % FlywayVersion,
      "com.beachape" %% "enumeratum" % EnumeratumVersion,
      "com.beachape" %% "enumeratum-circe" % EnumeratumVersion,
      "org.scalactic" %% "scalactic" % ScalaTestVersion,
      "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4")
  )
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(DockerPlugin)
  .enablePlugins(AshScriptPlugin)

mainClass in Compile := Some("com.gymio.GymioWebServer")
dockerBaseImage := "openjdk:jre-alpine"
packageName in Docker := "gcr.io/gymio-220023/gymio"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
)
