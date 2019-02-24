package modules
import com.google.inject.Inject
import modules.entity.{Content, Tag}
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.ReplaceOptions
import org.mongodb.scala.{MongoCollection, MongoDatabase}
import play.api.libs.json.Json

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class ContentRepositoryImpl @Inject()(
                                     mongoDatabase: MongoDatabase,
                                     ) extends ContentRepository {
  def findAll(): Future[List[Content]] = Future {
    val mongoCollection: MongoCollection[Document] = mongoDatabase.getCollection("imagebrowseservice")
    Await.result(mongoCollection.find().toFuture(), Duration.Inf)
      .toList
      .map{
        x =>
          x.toJson() match {
            case v: String =>
              Json.parse(v).validate[Content].get
          }
      }
  }

  def findByTag(tag: Tag): Future[List[Content]] = Future {
    val mongoCollection: MongoCollection[Document] = mongoDatabase.getCollection("imagebrowseservice")
    Await.result(mongoCollection.find(Document("tagList" -> tag.value)).toFuture(), Duration.Inf)
      .toList
      .map{
        x =>
          x.toJson() match {
            case v: String =>
              val json = Json.parse(v).result
              Content(json.get("contentId").toString().toInt, List(Tag("aaa")))
          }
      }
  }

  def update(content: Content): Future[Content] = Future {
    val mongoCollection: MongoCollection[Document] = mongoDatabase.getCollection("imagebrowseservice")
    val document: Document = Document(
      "contentId" -> content.id,
      "tagList" -> content.tags.map{_.value}
    )
    val replaceOption = new ReplaceOptions().upsert(true)
    mongoCollection.replaceOne(
      Document("contentId" -> content.id),
      document,
      replaceOption
    ).toFuture()
    content
  }
}
