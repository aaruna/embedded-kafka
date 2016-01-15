

import sbt._

object Version {
  final val Kafka            = "0.9.0.0"
  final val Logback          = "1.0.7"
  final val JavaVersion      = scala.util.Properties.javaVersion
  final val JavaBinary       = JavaVersion.dropRight(5)
  final val JOpt             = "3.2"
  final val Scala            = Seq("2.11.7", "2.10.5")
  final val ScalaCheck       = "1.12.5"
  final val ScalaLogging     = "3.1.0"
  final val ScalaTest        = "2.2.5"
  final val Slf4j            = "1.7.13"

  def cross(version: String): Unit =
    println(s"Scala: $version Java: $JavaVersion")

  /* initialize := assert(
     Integer.parseInt(javaVersion.split("\\.")(1)) >= 8,
     "Java 8 or above required.")*/

}

object Library {
  val kafka            = "org.apache.kafka"           %% "kafka"                % Version.Kafka excludeAll(Exclusions.forKafka: _*)
  val jopt             = "net.sf.jopt-simple"         % "jopt-simple"           % Version.JOpt
  val logback          = "ch.qos.logback"             % "logback-classic"       % Version.Logback
  val scalaCheck       = "org.scalacheck"             %% "scalacheck"           % Version.ScalaCheck
  val scalaTest        = "org.scalatest"              %% "scalatest"            % Version.ScalaTest

  object Cross {
    val slf4j          = "org.slf4j"                  % "slf4j-api"             % Version.Slf4j
    val scalaLogging   = "com.typesafe.scala-logging" %% "scala-logging"        % Version.ScalaLogging
  }
}

object Exclusions {
  lazy val forKafka = Seq(
    ExclusionRule("com.sun.jmx", "jmxri"),
    ExclusionRule("com.sun.jdmk", "jmxtools"),
    ExclusionRule("net.sf.jopt-simple", "jopt-simple"),
    ExclusionRule("org.slf4j", "slf4j-simple")
  )
}