package controllers

import modules.ContentService
import modules.entity.{Content, Tag}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting
import play.api.test.Helpers._
import play.api.test._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mockito.MockitoSugar
import play.api.libs.json.{JsNumber, Json}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class SearchControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar{
  "SearchController GET" should {
    "aaaaaaaaa" in {

      val contentService = mock[ContentService]
      when(contentService.findByTag(any[Tag])).thenReturn(
        Future(List(Content(1, List(Tag("test")))))
      )
      val controller = new SearchController(stubControllerComponents(), contentService)
      val search = controller.get().apply(FakeRequest(GET, "/api/v1/search?tag=test"))

      status(search) mustBe OK
      contentType(search) mustBe Some("application/json")
      val jsonResult = contentAsJson(search)
      jsonResult mustBe Json.arr(Json.arr(Json.obj("id" -> JsNumber(1))))
    }
  }

}
