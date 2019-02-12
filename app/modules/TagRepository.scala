package modules

import com.redis.RedisClient
import org.mongodb.scala.bson.BsonInt32
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.{ReplaceOptions, UpdateOptions}
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import play.api.Configuration

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.result.UpdateResult
import play.api.libs.json.Json

case class Tag(val name: String)
case class Content(val id:Int, val tags:List[Tag])

sealed trait TagRepositoryError
case object BackendDatabaseConnectionError extends TagRepositoryError

class TagRepository(config: Configuration) {
  private val databaseName: String = "test"
  private val collectionName: String = "imagebrowseservice"

  private def getCollection(): MongoCollection[Document] = {
    MongoClient().getDatabase(databaseName).getCollection(collectionName)
  }

  def findContent(tag: Tag): Either[TagRepositoryError, List[Tags]] = {
    val collection: MongoCollection[Document] = getCollection()
    val future:Future[Seq[Document]] = collection.find(Document("tagList" -> tag.name)).toFuture()
    val result[Seq[Tag]] = for {
      docs <- future
      doc <- docs
    } yield Tag(doc.toString())
    result
  }
  
  private def getClient(): MongoDatabase = {
    MongoClient().getDatabase(databaseName)
  }

  def findByTag(tag: String): List[Int] = {
    val collection: MongoCollection[Document] = getClient().getCollection("imagebrowseservice")
    val response = Await.result(
      collection.find(Document("tagList" -> tag)).toFuture(),
      Duration.Inf
    )

    response.map{_.toJson match {
      case s: String => Json.parse(s).validate[Tags].get.contentId
    }}.toList
  }

  def findById(contentId: Int): Tags = {
    val collection: MongoCollection[Document] = getClient().getCollection("imagebrowseservice")
    val response = Await.result(
      collection.find(Document("contentId" -> contentId)).toFuture(),
      Duration.Inf
    )

    Tags(contentId,
      response.map{_.toJson match {
        case s: String => Json.parse(s).validate[Tags].get.tagList
      }}.last
    )
  }

  def addTags(contentId: Int, tags: Tags): UpdateResult = {
    val collection: MongoCollection[Document] = getClient().getCollection("imagebrowseservice")
    val document: Document = Document(
      "contentId" -> contentId,
      "tagList" -> tags.tagList
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
