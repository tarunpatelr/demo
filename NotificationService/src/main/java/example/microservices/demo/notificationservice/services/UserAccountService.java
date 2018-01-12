package example.microservices.demo.notificationservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import example.microservices.demo.notificationservice.model.NotificationUser;
import example.microservices.demo.notificationservice.repository.NotificationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserAccountService {

    private Logger logger = LoggerFactory.getLogger(UserAccountService.class);

    @Autowired
    RestTemplate accountServiceRestTemplate;

    @Autowired
    NotificationUserRepository notificationUserRepository;

    @HystrixCommand(fallbackMethod = "handleNotificationUserFetchFailure")
    public NotificationUser getAccountServiceUser(String userId) {

        return accountServiceRestTemplate.getForObject("/user/" + userId,
                NotificationUser.class);
    }

    @SuppressWarnings("unused")
    public NotificationUser handleNotificationUserFetchFailure(String userId) {
        logger.error("Error occurred trying to fetch user detail .. " + userId);
        return null;
    }

    @HystrixCommand(fallbackMethod = "getFromLocalDB")
    public Map<String, String> findUser(String userId) {
        return accountServiceRestTemplate.getForObject("/user/" + userId,
                Map.class);
    }

    @SuppressWarnings("unused")
    public Map<String, String> getFromLocalDB(String userId) {
        NotificationUser notificationUser = notificationUserRepository.findOne(userId);
        Map<String, String> localUser = new HashMap<String, String>();
        localUser.put("id", notificationUser.getId());
        localUser.put("firstName", notificationUser.getFirstName());
        localUser.put("email", notificationUser.getEmail());
        localUser.put("lastName", "--na--");

        return localUser;
    }
}
