# spring-session-hazelcast
Examples of using Hazelcast with Spring Session

## security-hazelcast
The [Spring Session Security](http://docs.spring.io/spring-session/docs/current/reference/html5/guides/security.html) sample with Hazelcast as the backend instead of Redis. 

Supports sending SessionDestroyedEvents on expiry.

`./gradlew security-hazelcast:tomcatRun`

The application will be running at http://localhost:8080

Login as

* **Username** *user*
* **Password** *password*