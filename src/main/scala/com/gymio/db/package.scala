package com.gymio
import cats.data.NonEmptyList
import cats.implicits._
import doobie.{Get, Put}
import io.circe.Json
import io.circe.parser.parse
import org.postgresql.util.PGobject

package object db {
  implicit val jsonGet: Get[Json] =
    Get.Advanced.other[PGobject](NonEmptyList.of("json")).tmap[Json] { o =>
      parse(o.getValue).leftMap[Json](throw _).merge
    }

  implicit val jsonPut: Put[Json] =
    Put.Advanced.other[PGobject](NonEmptyList.of("json")).tcontramap[Json] { j =>
      val o = new PGobject
      o.setType("json")
      o.setValue(j.noSpaces)
      o
    }
}
