package controllers

import java.nio.file.Paths

import javax.inject.Inject
import play.api.Configuration
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._

import scala.util.Random
import com.redis._
import play.api.libs.json

import scala.concurrent.ExecutionContext
import scala.language.postfixOps

class PictureController @Inject()(cc: ControllerComponents,
                                  config: Configuration,
                                  redisClient: RedisClient
                                 )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  private def getSavePath() :String = {
    config.get[String]("storage.directory") + "hogefuga" + generateId() +  ".jpg"
  }

  private def getSavePath(id: String) :String = {
    config.get[String]("storage.directory") + "hogefuga" + id +  ".jpg"
  }

  private def createSuccessResponseJson(body:Map[String, String]): JsObject = {
    Json.obj(
      "meta" -> Json.obj("status" -> "ok"),
      "body" -> body
    )
  }

  private def createFailResponseJson(): JsObject = {
    Json.obj(
      "meta" -> Json.obj("status" -> "fail"),
      "body" -> Json.obj("error" -> "upload failed")
    )
  }

  private def generateId(): Integer = {
    redisClient.incr( config.get[String]("redis.key_prefix") + "id") match {
      case Some(s) => s.toInt
      case None => Random.nextInt()
    }
  }

  def get(id: String): Action[AnyContent] = Action {
    Ok.sendFile(
      content = new java.io.File(getSavePath(id)),
      inline = true
    )
  }

  def post() = Action(parse.multipartFormData) { request =>
    request.body.file("image").map { picture =>
      val filepath = getSavePath()
      picture.ref.moveFileTo(Paths.get(filepath), true)
      val body = Map("id" -> filepath)
      Ok(createSuccessResponseJson(body))
    }.getOrElse {
      Ok(createFailResponseJson())
    }
  }
}
