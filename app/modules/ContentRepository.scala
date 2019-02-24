package modules

import modules.entity.{Content, Tag}

import scala.concurrent.Future

trait ContentRepository {
  def findByTag(tag: Tag): Future[List[Content]]
  def findAll(): Future[List[Content]]
  def update(content: Content): Future[Content]

}
