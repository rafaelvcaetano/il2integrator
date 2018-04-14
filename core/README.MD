# IL-2 Integrator Core

The core module implements the main behaviour of the integrator. It is responsible for handling the
lifetime of both services and interfaces, as well as handling command requests. _Per se_, the core
module is useless and only when accompanied by service and interface implementations becomes useful.

## Creating an integrator instance

An instance of the integrator can be created through the `IL2IntegratorBuilder` class which provides
the necessary methods to setup an integrator. When building, the following actions can be made:
* Add services
* Add interfaces
* Define logging output

Once all setup is performed, the instance can the be created and started so that initialization of
both the services and the interfaces occurs.
```java
IL2Integrator integrator = new IL2IntegratorBuilder()
        .addService(new CareerService())
        .addService(new PWCGService())
        .addInterface(new HttpInterface(8080))
        .build();
        
integrator.start();
```

Services are initialized when the integrator instance is built. Then, when `start()` is called, the
interfaces are initialized and the services are requested to perform command registration. If an
interface fails to initialize, the integrator continues normally. When registering the commands, if
the same command is registered two times, an `IllegalStateException` is raised. If the integrator
fails to start, `false` is returned. Currently, this never happens.

## The internal service

The core module implements an internal service that provides behaviour that should always be present
in any integrator instance. Currently, this service allows the list of registered services to be
retrieved. The internal service is not included in this list.

### Available commands
#### List Services
* Description: Lists all registered public services that started successfully 
* Command: `services`
* Defined in class: `ListServices`
* Arguments: none
* Response class: `ServiceList`
* Notes: Each entry in the returned service list includes the key that must be used when invoking
the service, the human-readable name, the version code that can be used to check if the minimum
service version is present, and the human-readable version.

## Performing a request

The integrator handles requests and, upon validation, the requests are delegated to the
`CommandProxy` which is responsible for finding the correct service and command that should be
invoked. A basic request can be represented the following way:
```java
Request request = new Request();
request.service = "internal";
request.command = "services";

Response response = integrator.processCommand(request);
```
This example invoked the command "services" which is associated to the service "internal". This
would effectively query the internal service for the list of available services which would then be
sent in the response as an instance of `ServiceList`, which extends from `Response`.
The `processCommand()` method always returns a `Reponse` instance, even if an error occurs. Some
commands, such as the one in this example, return an instance of a subtype. However, this is only
true if no error occurs. As such, in direct calls to this method, one should always check if an
error occurred before casting the response. In this example, the correct implementation would be:
```java
Request request = new Request();
request.service = "internal";
request.command = "services";

Response response = integrator.processCommand(request);
if (response.result == ErrorCodes.SUCCESS) {
    ServiceList services = (ServiceList) response;
}
```
Another way of checking this is using `instanceof`.

Some commands require arguments. These are sent through a `Map<String, String>` in the request,
where the key of each pair is the name of the argument and the value, the corresponding value of the
argument. To see which arguments each command required, check the documentation of the service that
provides that command.

### Response results
The following results can be obtained from a request (as defined in `ErrorCodes`):
* **SUCCESS** (0) - The command executed successfully
* **ERR_INVALID_MESSAGE** (100) - The request message was invalid (for example, the request instance was
null or there were missing parameters)
* **ERR_INVALID_COMMAND** (101) - The specified command was invalid (for example, the queried service or
the requested command were not registered)
* **ERR_COMMAND_ERROR** (102) - The command failed to execute

The response is usually accompanied by a response message that provides further information about
the result.

## Logging

To keep logging standardized, it is recommended that all logging is done through the `Logger` class.
Three logging levels are provided:
* **DEBUG** - For simple information that aids during development
* **WARN** - For warning messages that may indicate that a non-critical problem has occurred
* **ERROR** - For error messages that indicate that a serious problem was encountered

The logging level can be set through `Logger.setLogLevel(int)` and specifying one of the constants
defined inside the class.