package org.realtimemessaging.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.realtimemessaging.dto.ActionType;
import org.realtimemessaging.dto.ApiResponse;
import org.realtimemessaging.dto.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolutionException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EventController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EventControllerTest {

    @MockBean
    private JmsTemplate jmsTemplate;

    @Value("${queue.name}")
    private String queueName;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_send_event_to_the_queue() throws Exception {
        Event testEvent = new Event("recepatas@gmail.com", "information text", ActionType.EMAIL);

        doNothing().when(jmsTemplate).convertAndSend(eq(queueName), any(Event.class));

        mockMvc.perform(
                post("/events")
                        .content(mapper.writeValueAsString(testEvent))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Event is added to the messaging queue"));

        verify(jmsTemplate, times(1)).convertAndSend(eq(queueName), any(Event.class));
    }

    @Test
    public void should_return_server_error_when_jms_error_occured() throws Exception {
        Event testEvent = new Event("recepatas@gmail.com", "information text", ActionType.EMAIL);

        doThrow(DestinationResolutionException.class).when(jmsTemplate).convertAndSend(eq(queueName), any(Event.class));

        mockMvc.perform(
                post("/events")
                        .content(mapper.writeValueAsString(testEvent))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void should_return_bad_request_when_event_data_is_not_valid() throws Exception {
        Event testEvent = new Event("", "", "");

        MvcResult mvcResult = mockMvc.perform(
                post("/events")
                        .content(mapper.writeValueAsString(testEvent))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        verify(jmsTemplate, never()).convertAndSend(eq(queueName), any(Event.class));

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());

        ApiResponse response = mapper.readValue(mvcResult.getResponse().getContentAsString(), ApiResponse.class);
        assertTrue(response.getMessage().contains("Customer address for given action type is not valid"));
        assertTrue(response.getMessage().contains("Text is mandatory"));
    }

    @Test
    public void should_return_bad_request_when_sms_event_data_is_not_valid() throws Exception {
        Event testEvent = new Event("invalid_phone_number", "information text", ActionType.SMS);

        doNothing().when(jmsTemplate).convertAndSend(eq(queueName), any(Event.class));

        mockMvc.perform(
                post("/events")
                        .content(mapper.writeValueAsString(testEvent))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Customer address for given action type is not valid"));

        verify(jmsTemplate, never()).convertAndSend(eq(queueName), any(Event.class));
    }

    @Test
    public void should_return_bad_request_when_email_event_data_is_not_valid() throws Exception {
        Event testEvent = new Event("invalid_email", "information text", ActionType.EMAIL);

        doNothing().when(jmsTemplate).convertAndSend(eq(queueName), any(Event.class));

        mockMvc.perform(
                post("/events")
                        .content(mapper.writeValueAsString(testEvent))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Customer address for given action type is not valid"));

        verify(jmsTemplate, never()).convertAndSend(eq(queueName), any(Event.class));
    }

    @Test
    public void should_return_bad_request_when_push_event_data_is_not_valid() throws Exception {
        Event testEvent = new Event("invalid_push", "information text", ActionType.PUSH);

        doNothing().when(jmsTemplate).convertAndSend(eq(queueName), any(Event.class));

        mockMvc.perform(
                post("/events")
                        .content(mapper.writeValueAsString(testEvent))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Customer address for given action type is not valid"));

        verify(jmsTemplate, never()).convertAndSend(eq(queueName), any(Event.class));
    }

}
