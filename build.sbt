val Http4sVersion     = "0.19.0-M3"
val Specs2Version     = "4.2.0"
val LogbackVersion    = "1.2.3"
val FlywayVersion     = "4.2.0"
val SlickVersion      = "3.2.1"
val CirceVersion      = "0.10.0"
val EnumeratumVersion = "1.5.13"

lazy val root = (project in file("."))
  .settings(
    organization := "com.gymio",
    name := "gymio-service",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.7",
    libraryDependencies ++= Seq(
      "org.http4s"          %% "http4s-blaze-server"  % Http4sVersion,
      "org.http4s"          %% "http4s-circe"         % Http4sVersion,
      "io.circe"            %% "circe-generic"        % CirceVersion,
      "io.circe"            %% "circe-generic-extras" % CirceVersion,
      "org.http4s"          %% "http4s-dsl"           % Http4sVersion,
      "org.specs2"          %% "specs2-core"          % Specs2Version % "test",
      "ch.qos.logback"      % "logback-classic"       % LogbackVersion,
      "org.flywaydb"        % "flyway-core"           % FlywayVersion,
      "com.typesafe.slick"  %% "slick"                % SlickVersion,
      "com.typesafe.slick"  %% "slick-hikaricp"       % SlickVersion,
      "com.beachape"        %% "enumeratum"           % EnumeratumVersion,
      "com.beachape"        %% "enumeratum-circe"     % EnumeratumVersion,
      "com.github.tminglei" %% "slick-pg"             % "0.15.7",
      "io.strongtyped"      %% "active-slick"         % "0.3.5",
      "com.github.tminglei" %% "slick-pg_circe-json"  % "0.15.7"
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.6"),
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4")
  )

mainClass in Compile := Some("com.gymio.GymioWebServer")

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
)

