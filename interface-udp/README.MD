# UDP Interface

The UDP interface allows interaction with the integrator through the UDP protocol. This interface
creates a simple UDP socket server.

The server listens for incoming connections. When a packet is received, it is processed, executed,
and a response is sent back through an UDP packet. 

The request and response packets' content must be a string that represents a JSON object. For a
request, the format is the following: 

```json
{
    "service": <service_key>,
    "command": <command>,
    "args": {
        "arg1": val1,
        "arg2": val2
    }
}
```

The returned response is a direct JSON representation of the Response object. For example, when
listing the services with the Career and PWCG services registered, the response is the following:
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

## Limitations
The request and response packets have a maximum size of 1024 bytes. If a packet exceeds this limit,
the message will be invalid.