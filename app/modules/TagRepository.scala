package modules

import com.redis.RedisClient
import play.api.Configuration

case class TagRepository(config: Configuration)
object TagRepository {
  def findById(contnteId: Int): Tags = {
    Tags(contnteId, List("a", "b"))
  }

  def addTags(contentId: Int, tags: Tags) = {
  }
}
