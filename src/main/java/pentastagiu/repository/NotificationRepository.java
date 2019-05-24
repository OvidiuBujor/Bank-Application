package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pentastagiu.model.Notification;

@Repository
public interface NotificationRepository extends CrudRepository<Notification,Long> {
}
