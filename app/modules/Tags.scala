package modules

import play.api.libs.json.Json

case class Tags(contentId: Int, tagList: List[String])
object Tags {
  implicit val jsonWrites = Json.writes[Tags]
  implicit val jsonRead = Json.reads[Tags]
}
