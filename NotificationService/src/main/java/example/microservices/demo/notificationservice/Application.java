package example.microservices.demo.notificationservice;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableHystrix
@EnableHystrixDashboard
public class Application {

    @Value("${user.account.service.exchange.name}")
    public String userAccountServiceExchangeName;

    @Value("${user.account.service.routingKey.prefix}")
    public String userAccountServiceRoutingKeyPrefix;

    @Value("${user.account.service.baseUrl}")
    public String accountServiceBaseUrl;

    @Value("${order.service.exchange.name}")
    public String orderServiceExchangeName;

    @Value("${order.service.routingKey.prefix}")
    public String orderServiceRoutingKeyPrefix;

    @Value("${order.service.baseUrl}")
    public String orderServiceBaseUrl;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate accountServiceRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        DefaultUriTemplateHandler handler = new DefaultUriTemplateHandler();
        handler.setBaseUrl(accountServiceBaseUrl);
        restTemplate.setUriTemplateHandler(handler);
        return restTemplate;
    }

    @Bean
    TopicExchange userAccountServiceExchange() {
        return new TopicExchange(userAccountServiceExchangeName, true, false);
    }

    @Bean
    Queue userAccountCreatedQueue() {
        return new Queue(this.getAccountCreatedQueueName(), false);
    }

    @Bean
    Queue userAccountDeletedQueue() {
        return new Queue(this.getAccountDeletedQueueName(), false);
    }

    @Bean
    Binding userAccountCreatedQueueBinding(Queue userAccountCreatedQueue,
                                           TopicExchange userAccountServiceExchange) {
        return BindingBuilder.bind(userAccountCreatedQueue)
                .to(userAccountServiceExchange).with(this.getAccountCreatedQueueName());
    }

    @Bean
    Binding userAccountDeletedQueueBinding(Queue userAccountDeletedQueue, TopicExchange userAccountServiceExchange) {
        return BindingBuilder.bind(userAccountDeletedQueue).to(userAccountServiceExchange).with(this.getAccountDeletedQueueName());
    }

    @Bean
    SimpleMessageListenerContainer containerOne(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapterOne) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(this.getAccountCreatedQueueName());
        container.setMessageListener(listenerAdapterOne);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapterOne(UserAccountListener receiver) {
        return new MessageListenerAdapter(receiver, "handleUserAccountCreated");
    }

    @Bean
    SimpleMessageListenerContainer containerTwo(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapterTwo) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(this.getAccountDeletedQueueName());
        container.setMessageListener(listenerAdapterTwo);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapterTwo(UserAccountListener receiver) {
        return new MessageListenerAdapter(receiver, "handleUserAccountDeleted");
    }

    private String getAccountDeletedQueueName() {
        return this.userAccountServiceRoutingKeyPrefix + "deleted";
    }

    private String getAccountCreatedQueueName() {
        return this.userAccountServiceRoutingKeyPrefix + "created";
    }

    @Bean
    @LoadBalanced
    RestTemplate orderServiceRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        DefaultUriTemplateHandler handler = new DefaultUriTemplateHandler();
        handler.setBaseUrl(orderServiceBaseUrl);
        restTemplate.setUriTemplateHandler(handler);
        return restTemplate;
    }

    @Bean
    TopicExchange orderServiceExchange() {
        return new TopicExchange(orderServiceExchangeName, true, false);
    }

    @Bean
    Queue orderCreatedQueue() {
        return new Queue(this.getOrderCreatedQueueName(), false);
    }

    @Bean
    Binding orderCreatedQueueBinding(Queue orderCreatedQueue, TopicExchange orderServiceExchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(orderServiceExchange).with(this.getOrderCreatedQueueName());
    }

    @Bean
    SimpleMessageListenerContainer containerThree(ConnectionFactory connectionFactory,
                                                MessageListenerAdapter listenerAdapterThree) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(this.getOrderCreatedQueueName());
        container.setMessageListener(listenerAdapterThree);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapterThree(OrderEventListener receiver) {
        return new MessageListenerAdapter(receiver, "handleOrderCreated");
    }

    private String getOrderCreatedQueueName() {
        return this.orderServiceRoutingKeyPrefix + "created";
    }

}