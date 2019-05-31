package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pentastagiu.model.Notification;

/**
 * This class is the repository class
 * for Notification model class.
 */
@Repository
public interface NotificationRepository extends CrudRepository<Notification,Long> {
}
