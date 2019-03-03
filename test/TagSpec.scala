import modules.entity.Tag
import org.scalatestplus.play.PlaySpec

class TagSpec extends PlaySpec {
  "Tag" should {
    "作成できる" in {
      val tag: Tag = Tag("test")
      tag.value mustBe "test"
    }
  }
}
