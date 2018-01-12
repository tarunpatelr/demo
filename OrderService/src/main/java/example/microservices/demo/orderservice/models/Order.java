package example.microservices.demo.orderservice.models;


import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Order {

    @Id
    private String id;
    private String userId;
    private String customerEmail;
    private List<String> items;
    private boolean isCancelled;
}
