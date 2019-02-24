package modules.entity

import play.api.libs.json.Json

case class Content(id: Int, tags: List[Tag])
object Content {
  implicit val jsonWrites = Json.writes[Content]
  implicit val jsonReads = Json.reads[Content]
}