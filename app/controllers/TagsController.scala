package controllers

import com.redis.RedisClient
import javax.inject.Inject
import modules.{TagRepository, Tags}
import play.api.Configuration
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

class TagsController @Inject()(cc: ControllerComponents, config: Configuration) extends AbstractController(cc){

  private def getClient(): RedisClient = {
    new RedisClient(config.get[String]("redis.host"), config.get[Int]("redis.port"))
  }

  def post(pictureId: Int): Action[AnyContent] = Action { request =>
    request.body.asJson.map { json =>
      json.validate[Tags] match {
        case play.api.libs.json.JsSuccess(value, path) =>
          val tagRepository: TagRepository = TagRepository(config)
          tagRepository.addTags(pictureId, value)
          Ok(json)
        case play.api.libs.json.JsError(errors) =>
          BadRequest(JsError.toJson(errors))
      }
    }.getOrElse(
      BadRequest("not json")
    )
  }

  def get(pictureId: Int): Action[AnyContent] = Action {
    val client = getClient()
    client.smembers(config.get[String]("redis.key_prefix") + "tags_" + pictureId) match {
      case Some(s) =>
        val tagRepository: TagRepository = TagRepository(config)
        val response = tagRepository.findById(pictureId)
        Ok(Json.arr(response.tagList))
      case None =>
        BadRequest("not found")
    }
  }
}
