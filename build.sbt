import Dependencies._

ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.13.10"
Global / onChangedBuildSource := ReloadOnSourceChanges

val `contract-grpc-proto-interface` =
  (project in file("contracts/contract-grpc-proto-interface"))
    .settings(
      name := "contract-grpc-proto-interface",
      libraryDependencies ++= Dependencies.`contract-grpc-proto-interface`,
      Compile / PB.targets := Seq(
        scalapb.gen(grpc = true) -> (Compile / sourceManaged).value
      )
    )

lazy val root = (project in file("."))
  .settings(
    name := "scala-pb-sample",
    libraryDependencies ++= Seq(
      Libraries.grpcNetty
    )
  )
  .dependsOn(`contract-grpc-proto-interface`)
  .aggregate(`contract-grpc-proto-interface`)
