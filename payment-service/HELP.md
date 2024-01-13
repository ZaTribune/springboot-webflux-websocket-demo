
## Troubleshooting Notes
If the WebSocket connection is still closing unexpectedly, try to isolate and troubleshoot the issue. 
Here are some suggestions:

### Logging and Debugging:
Add detailed logging statements throughout your code to trace the flow
and identify the point at which the WebSocket connection is being closed.
Ensure that you log information about the WebSocket session, repository operations, and any error conditions.

```
.doOnTerminate(() -> log.info("WebSocket processing terminated"))
.doOnError(error -> log.error("Error in WebSocket processing", error))
```

### Error Handling:
Make sure that you handle errors properly, especially within the reactive streams.
If an exception occurs during repository operations or other asynchronous tasks, 
it could lead to premature termination.

```
.onErrorResume(Exception.class, ex -> {
log.error("Error in reactive stream", ex);
return Mono.empty(); // or another fallback strategy
})
```

### WebSocket Lifecycle:
Review any other parts of your WebSocket logic or application that might be explicitly
closing the WebSocket connection.
Ensure that you are not calling session.close() or similar methods unintentionally.

### Reactive Repository Behavior:
Ensure that your reactive repository (paymentSessionRepository and paymentEventRepository) is behaving as expected.
If there's an issue with the repository (e.g., incorrect configuration, misbehaving database driver),
it might lead to unexpected behavior.

### WebSocket Session Management:
Check if there is any custom logic related to WebSocket session management or cleanup in your application.
Ensure that the WebSocket session is not closed by external factors.

### Reactive Repository Configuration:
Review the configuration of your reactive repositories (paymentSessionRepository and paymentEventRepository).
Ensure that they are configured correctly, and the underlying database connection is stable.
Check for Unhandled Exceptions:
In the reactive flow, ensure that there are no unhandled exceptions 
that might lead to the termination of the WebSocket connection. 
Carefully review your code for potential exception scenarios.

### Simplify the Code:
Temporarily simplify your code to the bare minimum
(e.g., remove repository operations) and see if the WebSocket connection still closes.
Gradually reintroduce components to identify the specific part causing the issue.

### Reactor Hooks:
You can use Reactor Hooks to globally handle errors or other events in your reactive flows.
This might help catch any issues in the entire reactive pipeline.

```
Hooks.onErrorDropped(error -> log.error("Error dropped in reactive flow", error));
```

By carefully reviewing and logging the execution flow,
you should be able to identify the root cause of the unexpected WebSocket connection closure.
If the issue persists, please provide additional details or specific error messages,
and I'll do my best to assist further.