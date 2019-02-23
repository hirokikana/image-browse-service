package controllers

import javax.inject.Inject
import modules.TagRepository
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc._

class SearchController @Inject()(cc: ControllerComponents, config: Configuration) extends AbstractController(cc){
  def get(): Action[AnyContent] = Action { request =>
    val requestTag: String = request.getQueryString("tag").getOrElse("")
    val tagRepository: TagRepository = new TagRepository(config)
    //Ok("aaa")
    Ok(Json.arr(tagRepository.findByTag(requestTag)))
  }
}
