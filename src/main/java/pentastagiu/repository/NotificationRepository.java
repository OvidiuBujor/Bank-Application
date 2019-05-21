package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import pentastagiu.model.Notification;

public interface NotificationRepository extends CrudRepository<Notification,Integer> {
}
