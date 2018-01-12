package example.microservices.demo.orderservice.controllers;

import example.microservices.demo.orderservice.events.OrderEvent;
import example.microservices.demo.orderservice.events.OrderEventType;
import example.microservices.demo.orderservice.models.Order;
import example.microservices.demo.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/order/{orderId}")
    public ResponseEntity getOrder(@PathVariable("orderId") String orderId) {

        Order order = repository.findOne(orderId);
        if (order == null) {
            return new ResponseEntity("No order found for ID " + orderId, HttpStatus.NOT_FOUND);
        }

        logger.info("returning order " + order.getId());

        return new ResponseEntity(order, HttpStatus.OK);
    }

    @PostMapping(value = "/order",  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createOrder(@RequestBody Order order) {

        order = repository.save(order);

        logger.info("created order with items " + order.getItems());

        applicationEventPublisher.publishEvent(new OrderEvent(this, order, OrderEventType.CREATED));

        return new ResponseEntity(order, HttpStatus.OK);
    }

    @PostMapping("order/cancel/{orderId}")
    public String cancelOrder(@PathVariable("orderId") String orderId) {

        Order order = repository.findOne(orderId);

        if (order != null) {
            order.setCancelled(true);
            repository.save(order);
            applicationEventPublisher.publishEvent(new OrderEvent(this, order, OrderEventType.CANCELLED));
            return "Order " + orderId + " cancelled";
        }

        return "Cannot find order";
    }
}
