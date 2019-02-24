import com.google.inject.AbstractModule
import com.redis.RedisClient
import modules.{ContentRepository, ContentRepositoryImpl}
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import play.api.{Configuration, Environment}

class Module(
              environment: Environment,
              configuration: Configuration
            ) extends AbstractModule {

  override def configure(): Unit = {
    val redisHost = "localhost"
    val redisPort = 6379
    val mongoDatabaseName = "test"

    bind(classOf[RedisClient]).toInstance(new RedisClient(redisHost, redisPort))
    bind(classOf[ContentRepository]).to(classOf[ContentRepositoryImpl])
    bind(classOf[MongoDatabase]).toInstance(MongoClient().getDatabase(mongoDatabaseName))
  }

}
