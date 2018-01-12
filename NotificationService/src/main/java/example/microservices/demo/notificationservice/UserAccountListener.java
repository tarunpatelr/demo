package example.microservices.demo.notificationservice;

import example.microservices.demo.notificationservice.model.NotificationUser;
import example.microservices.demo.notificationservice.services.UserAccountService;
import example.microservices.demo.notificationservice.repository.NotificationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAccountListener {

    private Logger logger = LoggerFactory.getLogger(UserAccountListener.class);

    @Autowired
    NotificationUserRepository notificationUserRepository;

    @Autowired
    UserAccountService userAccountService;

    @SuppressWarnings("unused")
    public void handleUserAccountCreated(String userId) {

        logger.info("Handling user created event for user id '" + userId + "'");

        NotificationUser notificationUser = userAccountService.getAccountServiceUser(userId);

        if (notificationUser != null) {

            notificationUserRepository.save(notificationUser);

            logger.info("User created in notification_service db with user id "
                    + notificationUser.getId() + " and email " + notificationUser.getEmail());

            logger.debug("Sending welcome email now ... ");
        }

    }

    @SuppressWarnings("unused")
    public void handleUserAccountDeleted(String userId) {

        logger.debug("Handling user deleted event for user id '" + userId + "'");

        notificationUserRepository.delete(userId);

        logger.info("Deleted user " + userId + " from notification_service db ");

        logger.debug("Sending email to user 'Sad :( to see you go!' ");
    }
}