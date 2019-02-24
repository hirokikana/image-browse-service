package modules.entity

import play.api.libs.json.Json

case class Tag(value: String)
object Tag {
  implicit val jsonWrites = Json.writes[Tag]
  implicit val jsonRead = Json.reads[Tag]
}