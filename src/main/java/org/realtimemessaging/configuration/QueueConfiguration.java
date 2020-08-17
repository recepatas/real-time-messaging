package org.realtimemessaging.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class QueueConfiguration {

    @Value("${queue.name}")
    private String queueName;

    @Value("${queue.consumer.concurrency}")
    private String concurrency;

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        // set the concurrency level based on the configuration in application.yml to run concurrent consumers
        factory.setConcurrency(concurrency);

        configurer.configure(factory, connectionFactory);
        return factory;
    }

}
