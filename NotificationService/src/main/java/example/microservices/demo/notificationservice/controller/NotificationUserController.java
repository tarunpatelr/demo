package example.microservices.demo.notificationservice.controller;


import example.microservices.demo.notificationservice.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationUserController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/notification-user/{userId}")
    public ResponseEntity getNotificationUser(@PathVariable("userId") String userId) {

        return new ResponseEntity(userAccountService.findUser(userId),
                HttpStatus.OK);
    }
}
