import javax.swing.text.html.HTML
import modules.{ContentRepository, ContentService}
import modules.entity.{Content, Tag}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ContentServiceSpec extends PlaySpec with MockitoSugar with ScalaFutures {
  "ContentService" should {
    "findAll" in {
      val contentRepository: ContentRepository = mock[ContentRepository]
      val content = Content(1,List(Tag("testtag")))
      when(contentRepository.findAll()).thenReturn(
        Future(List(content))
      )
      val contentService = new ContentService(contentRepository)
      contentService.findAll().futureValue mustBe List(content)
    }

    "findByTag" in {
      val contentRepository: ContentRepository = mock[ContentRepository]
      val content = Content(1,List(Tag("testtag")))
      when(contentRepository.findByTag(any[Tag])).thenReturn(
        Future(List(content))
      )
      val contentService = new ContentService(contentRepository)
      contentService.findByTag(Tag("testtag")).futureValue mustBe List(content)
    }

    "store" in {
      val contentRepository: ContentRepository = mock[ContentRepository]
      val content = Content(1,List(Tag("testtag")))
      when(contentRepository.update(any[Content])).thenReturn(
        Future(content)
      )
      val contentService = new ContentService(contentRepository)
      contentService.store(content).futureValue mustBe content
    }

  }
}
