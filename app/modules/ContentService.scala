package modules

import com.google.inject.Inject
import modules.entity.{Content, Tag}

import scala.concurrent.Future

class ContentService @Inject()(contentRepository: ContentRepository) {
  def findAll(): Future[List[Content]] = {
    contentRepository.findAll()
  }

  def findByTag(tag: Tag): Future[List[Content]] = {
    contentRepository.findByTag(tag)
  }

  def store(content: Content): Future[Content] = {
    contentRepository.update(content)
  }
}
