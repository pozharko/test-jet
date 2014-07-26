name := "Test jet"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += "com.google.guava" % "guava" % "17.0"
