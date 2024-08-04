import scala.util.Try
import org.scalajs.linker.interface.ESVersion
import org.scalajs.linker.interface.ModuleSplitStyle
import sbt.ProjectOrigin.Organic
import sbtcrossproject.CrossProject
import scala.io.Source._

ThisBuild / scalaVersion := "3.4.2"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "net.apiduck"
ThisBuild / scalacOptions ++= Seq("-encoding", "utf-8", "-deprecation", "-feature", "-unchecked", "-Wunused:all", "-Wshadow:all", "-Yexplicit-nulls") //, "-rewrite", "-source", "3.2-migration") // TODO add in scala 3.5.0 -Yflexible-types

val PekkoVersion = "1.0.2"
val BorerVersion = "1.14.0"
val ScalaJsDomVersion = "2.8.0"
val DuckTapeVersion = "0.0.4"

ThisBuild / resolvers += "ApiDuck GitHub Package Registry" at "https://maven.pkg.github.com/phoenixmitx/apiduck"
val GITHUB_TOKEN = sys.env.get("GITHUB_TOKEN").orElse(Try(fromFile("GITHUB_TOKEN").getLines.next).toOption).getOrElse(throw new RuntimeException("GITHUB_TOKEN not found\nPlease set GITHUB_TOKEN environment variable or create a file named GITHUB_TOKEN with the token as the first line"))
ThisBuild / credentials += Credentials("GitHub Package Registry", "maven.pkg.github.com", "phoenixmitx", GITHUB_TOKEN)

lazy val core: CrossProject = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .withoutSuffixFor(JVMPlatform)
  .in(file("modules/core"))
  .settings(
    name := "vetris-core",
  )
	.settings(
		libraryDependencies ++= Seq(
			"net.apiduck" %%% "ducktape-core" % DuckTapeVersion,
		)
	)
  .jsConfigure(
    _.enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
  )
  .jsSettings(
    stUseScalaJsDom := true,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % ScalaJsDomVersion,

    // Tell ScalablyTyped that we manage `bun install` ourselves
    externalNpm := baseDirectory.value / "../../../",
  )

lazy val app: Project = project
  .in(file("modules/app"))
  .dependsOn(core.js)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "vetris-app",
    scalaJSUseMainModuleInitializer := true,

    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
      .withESFeatures(_
        .withESVersion(ESVersion.ES2021)
        .withAvoidClasses(false)
        .withAllowBigIntsForLongs(true)
        // generates smaller bundles if we keep avoidLetsAndConsts on
        // .withAvoidLetsAndConsts(false)
      )
      // small modules are counterproductive
      .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("de.phoenixmitx")))
    },
  )

lazy val server: Project = project
  .in(file("modules/server"))
  .dependsOn(core.jvm)
  .settings(
    name := "vetris-server",
  )

