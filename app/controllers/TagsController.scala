package controllers

import javax.inject.Inject
import modules.entity.{Content, Tag}
import modules.ContentRepository
import play.api.Configuration
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

class TagsController @Inject()(cc: ControllerComponents,
                               config: Configuration,
                               contentRepository: ContentRepository
                              ) extends AbstractController(cc){

  def post(pictureId: Int): Action[AnyContent] = Action { request =>
    request.body.asJson.map { json =>
      json.validate[Content] match {
        case play.api.libs.json.JsSuccess(value, path) =>
          contentRepository.update(value)
          Ok(json)
        case play.api.libs.json.JsError(errors) =>
          BadRequest(JsError.toJson(errors))
      }
    }.getOrElse(
      BadRequest("not json")
    )
  }

  def get(pictureId: Int): Action[AnyContent] = Action {
    /*
    val client = getClient()
    client.smembers(config.get[String]("redis.key_prefix") + "tags_" + pictureId) match {
      case Some(s) =>
        val tagRepository: TagRepository = new TagRepository(config)
        val response = tagRepository.findById(pictureId)
        Ok(Json.arr(response.tagList))
      case None =>
        BadRequest("not found")
    }
    */
    Ok("fjdskalfjdskal")
  }
}
