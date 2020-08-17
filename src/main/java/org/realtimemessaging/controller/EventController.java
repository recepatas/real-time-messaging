package org.realtimemessaging.controller;

import org.realtimemessaging.dto.Event;
import org.realtimemessaging.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class EventController {

    private Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${queue.name}")
    private String queueName;

    @PostMapping("/events")
    public ResponseEntity<ApiResponse> addMessage(@Valid @RequestBody Event event) {
        jmsTemplate.convertAndSend(queueName, event);
        logger.info("Event is added to the messaging queue");
        return new ResponseEntity<ApiResponse>(new ApiResponse("Event is added to the messaging queue"), HttpStatus.OK);
    }

}
