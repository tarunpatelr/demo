package example.microservices.demo.useraccountservice.repository;

import java.util.List;

import example.microservices.demo.useraccountservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    @SuppressWarnings("unused")
    User findByFirstName(String firstName);

    @SuppressWarnings("unused")
    List<User> findByLastName(String lastName);

}