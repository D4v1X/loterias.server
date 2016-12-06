name := """lotoserver"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.postgresql"       % "postgresql"               %  "9.4.1209"  withSources() withJavadoc(),
  "org.jooq"             % "jooq"                     %  "3.8.6"     withSources() withJavadoc(),
  "org.jooq"             % "jooq-meta"                %  "3.8.6"     withSources() withJavadoc(),
  "org.jooq"             % "jooq-codegen"             %  "3.8.6"     withSources() withJavadoc()
)



// **************************************************
// Task to generate the JOOQ model from the DB tables
// **************************************************

val genJooqModel = taskKey[Seq[File]]("Generate JOOQ classes")

val genJooqTask = (sourceManaged, fullClasspath in Compile, runner in Compile, streams) map { (src, cp, r, s) =>
  toError(r.run("org.jooq.util.GenerationTool", cp.files, Array("conf/jooq.xml"), s.log))
  ((src / "main/generated") ** "*.scala").get
}

genJooqModel <<= genJooqTask

unmanagedSourceDirectories in Compile += sourceManaged.value / "main/generated"



// **************************************************************
// Avoid adding the Javadocs when generating a distributable file
// **************************************************************

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false
