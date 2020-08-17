## Softtech Real Time Messaging Case
Implemented with Java 8 + Spring Boot + Spring JMS

### Run project
```
mvn spring-boot:run
```

### Adding new events
POST API endpoint [localhost:8081/events](localhost:8081/events)

Example input
```json
{
	"customerAddress": "recepatas@gmail.com",
	"text": "test text message",
	"actionType": "EMAIL"
}
```

### Run tests
```
mvn test
```

### Implementation Details

* To reduce the delay in peak times, concurrency level of the JMSListener can be configured in application.yml  
Currently, concurrentConsumers is set to 3 and maxConcurrentConsumers is set to 10.
This means 3 threads will be running and listening the queue at start up.

* As another idea to reduce the delay of the actions, we can call related services as fire-and-forget by using Async methods.
in SMSSender, send method is annotated with Async.

* Test cases for concurrent consuming can be found in EventReceiverIntegrationTest
