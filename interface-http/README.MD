# HTTP Interface

The HTTP interface allows interaction with the integrator through the HTTP protocol. This interface
creates a simple HTTP server using NanoHTTPD.

This interface is intended to be used as a standard REST API would be used. Assuming that the server
is hosted locally on port 8080, a request would be made using the following syntax:

`http://localhost:8080/<service>/<command>[?arg1=val1&arg2=val2&arg3=val3]`

So, for example, a request to list the registered services would be made through the following URL:

`http://localhost:8080/internal/services`

The response is returned in JSON format. The main structure of the response is the following:
```json
{
    "result": <command_result_code>,
    "message": <command_response>
}
```

Some commands may add extra fields to the response. For example, when listing the services with the
Career and PWCG services registered, the response is the following:
```json
{
    "result": 0,
    "message": "Success",
    "services": [
        {
            "key": "career",
            "name": "Career",
            "versionCode": 1,
            "version": "1.0"
        },
        {
            "key": "pwcg",
            "name": "PWCG",
            "versionCode": 1,
            "version": "1.0"
        }
    ]
}
```

There is no distinction between the multiple HTTP methods. As such, any of them can be used when
making a request.

## Arguments

For commands that require arguments, they are passed as query parameters. The name of the parameter
should match the name of the argument. For example, if we had a service "dummy" with a command
"test" that expected an argument with the name "message", the URL for a request that would have the
value "Test message" associated with the argument would be the following:

`http://localhost:8080/dummy/test?message=Test%20message`

## HTTP response codes

Depending on the response's result code (as defined in `ErrorCodes`), the following response codes
can be returned:
* **SUCCESS** => 200 OK
* **ERR_INVALID_MESSAGE** => 400 Bad Request
* **ERR_INVALID_COMMAND** => 404 Not Found
* **ERR_COMMAND_ERROR** => 500 Internal Error

As such, it is recommended that commands return one of the defined error codes. Otherwise the HTTP
response code will always be 200. 