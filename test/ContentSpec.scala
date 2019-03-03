import modules.entity.{Content, Tag}
import org.scalatestplus.play.PlaySpec

class ContentSpec extends PlaySpec {
  "Content" must {
    "作成できる" in {
      val content: Content = Content(1, List(Tag("test")))
      content.id mustBe 1
      content.tags mustBe List(Tag("test"))
    }
  }

  "タグが空でも作成できる" in {
    val content: Content = Content(1, List())
    content.id mustBe 1
    content.tags mustBe List()
  }

}
