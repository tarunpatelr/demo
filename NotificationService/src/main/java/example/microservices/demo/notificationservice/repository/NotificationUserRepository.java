package example.microservices.demo.notificationservice.repository;


import example.microservices.demo.notificationservice.model.NotificationUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationUserRepository extends CrudRepository<NotificationUser, String> {

}