package org.realtimemessaging.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.realtimemessaging.dto.ActionType;
import org.realtimemessaging.dto.ApiResponse;
import org.realtimemessaging.dto.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@EnableAsync
public class EventReceiverIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ConcurrentEventTestHelper concurrentEventTestHelper;

    @Autowired
    private EventSenderFactory eventSenderFactory;

    ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void should_process_created_event_message() throws Exception {
        Event testEvent = new Event("05418173433", "information text", ActionType.SMS);

        mockMvc.perform(
                post("/events")
                        .content(mapper.writeValueAsString(testEvent))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Event is added to the messaging queue"));

        // should consume the event from queue in less than 1000 ms since it is called as fire-and-forget.
        Thread.sleep(100);

        assertEquals(1, eventSenderFactory.getEventSender(testEvent.getActionType()).getCount());
    }

    @Test
    public void should_return_error_when_event_data_is_not_valid() throws Exception {
        Event testEvent = new Event("", "", "");

        MvcResult mvcResult = mockMvc.perform(
                post("/events")
                        .content(mapper.writeValueAsString(testEvent))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());

        ApiResponse response = mapper.readValue(mvcResult.getResponse().getContentAsString(), ApiResponse.class);

        assertTrue(response.getMessage().contains("Customer address for given action type is not valid"));
        assertTrue(response.getMessage().contains("Text is mandatory"));
    }

    @Test
    public void should_process_event_concurrently() throws Throwable {
        Stream<MvcResult> resultStream = IntStream.range(0, 10).boxed()
                .map(i -> {
                    try {
                        return concurrentEventTestHelper
                                .sendEvent(new Event("recepatas@gmail.com", "information text" + i, ActionType.EMAIL), mockMvc)
                                .get();
                    } catch (Exception e) { throw new RuntimeException(e.getMessage()); }
                }
                );

        List<Integer> results = resultStream.map(mvcResult -> mvcResult.getResponse().getStatus()).collect(Collectors.toList());
        assertEquals(10, Collections.frequency(results, HttpStatus.OK.value()));

        // Email send process takes 1000 miliseconds.
        // should consume 10(max number of concurrent consumer) the events from the queue after 1000 ms
        Thread.sleep(1100);
        assertEquals(10, eventSenderFactory.getEventSender(ActionType.EMAIL).getCount());
    }

}

@Service
class ConcurrentEventTestHelper {

    ObjectMapper mapper = new ObjectMapper();

    @Async
    public CompletableFuture<MvcResult> sendEvent(Event event, MockMvc mockMvc) throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/events")
                        .content(mapper.writeValueAsString(event))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        return CompletableFuture.completedFuture(mvcResult);
    }
}
