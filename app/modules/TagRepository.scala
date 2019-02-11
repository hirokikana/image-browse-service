package modules

import com.redis.RedisClient
import org.mongodb.scala.bson.BsonInt32
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.{ReplaceOptions, UpdateOptions}
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import play.api.Configuration

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.Filters._

case class TagRepository(config: Configuration) {
  private def getClient(): MongoDatabase = {
    MongoClient().getDatabase("test")
  }

  def findById(contnteId: Int): Tags = {
    Tags(contnteId, List("a", "b"))
  }

  def addTags(contentId: Int, tags: Tags) = {
    val collection: MongoCollection[Document] = getClient().getCollection("imagebrowseservice")
    val document: Document = Document(
      "contentId" -> contentId,
      "tags" -> tags.tagList
    )
    val replaceOption = new ReplaceOptions().upsert(true)
    Await.result(
      collection.replaceOne(
        Document("contentId" -> contentId),
        document,
        replaceOption
      ).toFuture,
      Duration.Inf)
  }
}
