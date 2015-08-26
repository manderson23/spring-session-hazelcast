# spring-session-hazelcast
Examples of using Hazelcast 3.5.1 with Spring Session 1.0.2.

From Spring Session 1.1 onwards this sample is now part of the Spring Session project itself. See https://github.com/spring-projects/spring-session/tree/master/samples/hazelcast-spring.

## security-hazelcast
The [Spring Session Security](http://docs.spring.io/spring-session/docs/current/reference/html5/guides/security.html) sample with Hazelcast as the backend instead of Redis. 

Supports sending SessionDestroyedEvents on expiry.

`./gradlew security-hazelcast:tomcatRun`

The application will be running at http://localhost:8080

Login as

* **Username** *user*
* **Password** *password*