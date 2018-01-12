package example.microservices.demo.orderservice.repository;


import example.microservices.demo.orderservice.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {

}