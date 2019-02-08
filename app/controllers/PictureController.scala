package controllers

import java.nio.file.Paths
import javax.inject.Inject
import play.api.Configuration
import play.api.Logger
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.mvc.AbstractController
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import play.api.mvc.Request
import scala.util.Random
import com.redis._

class PictureController @Inject()(cc: ControllerComponents, config: Configuration) extends AbstractController(cc) {
  private val logger = Logger(this.getClass)

  private def getSavePath() :String = {
    config.get[String]("storage.directory") + "hogefuga" + generateId() +  ".jpg"
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
    val client = new RedisClient(config.get[String]("redis.host"), config.get[Int]("redis.port"))
    client.incr( config.get[String]("redis.key_prefix") + "id") match {
      case Some(s) => s.toInt
      case None => Random.nextInt()
    }
  }


  def post() = Action(parse.multipartFormData) { request =>
    request.body.file("image").map { picture =>
      val filepath = getSavePath()
      picture.ref.moveFileTo(Paths.get(filepath), true)
      val body = Map("id" -> filepath)
      Ok(Json.stringify(createSuccessResponseJson(body)))
    }.getOrElse {
      Ok(Json.stringify(createFailResponseJson()))
    }
  }
}
