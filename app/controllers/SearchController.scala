package controllers

import javax.inject.Inject
import modules.entity.Tag
import modules.{ContentRepository, ContentService}
import play.api.Configuration
import play.api.libs.json.{JsNumber, JsString, Json}
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SearchController @Inject()(cc: ControllerComponents,
                                 val contentService: ContentService
                                ) extends AbstractController(cc){
  def get(): Action[AnyContent] = Action { request =>
    val requestTag: String = request.getQueryString("tag").getOrElse("")
    val result = Await.result(contentService.findByTag(Tag(requestTag)), Duration.Inf)
    Ok(Json.arr(result.map(x => Json.obj("id" -> JsNumber(x.id)))))
  }
}
