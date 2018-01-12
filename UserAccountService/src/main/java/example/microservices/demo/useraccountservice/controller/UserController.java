package example.microservices.demo.useraccountservice.controller;

import java.util.List;

import example.microservices.demo.useraccountservice.event.UserEvent;
import example.microservices.demo.useraccountservice.event.UserEventType;
import example.microservices.demo.useraccountservice.model.User;
import example.microservices.demo.useraccountservice.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserRepository repository;

    @GetMapping("/users")
    public List<User> getUsers() {

        logger.info("returning all users in the system");

        return repository.findAll();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUser(@PathVariable("id") String id) {

        User user = repository.findOne(id);
        if (user == null) {
            return new ResponseEntity("No user found for ID " + id, HttpStatus.NOT_FOUND);
        }

        logger.info("returning user " + user.getId());

        return new ResponseEntity(user, HttpStatus.OK);
    }

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createUser(@RequestBody User user) {

        repository.save(user);

        logger.info("created user with firstName " + user.getFirstName() + ", email " + user.getEmail());

        applicationEventPublisher.publishEvent(new UserEvent(this, user, UserEventType.CREATED));

        return new ResponseEntity(user, HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity updateUser(@PathVariable String id, @RequestBody User user) {

        user = repository.save(user);

        if (null == user) {
            return new ResponseEntity("No user found for ID " + id, HttpStatus.NOT_FOUND);
        }

        logger.info("updated user with firstName " + user.getFirstName() + ", email " + user.getEmail());

        applicationEventPublisher.publishEvent(new UserEvent(this, user, UserEventType.UPDATED));

        return new ResponseEntity(user, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {

        repository.delete(id);

        logger.info("deleted user with id " + id);

        applicationEventPublisher.publishEvent(new UserEvent(this, new User(id), UserEventType.DELETED));

        return new ResponseEntity(id, HttpStatus.OK);

    }

    @GetMapping("/user/email/{id}")
    public ResponseEntity getUserEmail(@PathVariable("id") String id) {

        User user = repository.findOne(id);
        if (user == null) {
            return new ResponseEntity("No user found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(user.getEmail(), HttpStatus.OK);
    }
}
