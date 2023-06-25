# Spring Auth JT

This is reworked from a Spring Boot Authentication series that starts with this [tutorial](https://www.youtube.com/watch?v=R76S0tfv36w) which is expanded upon further with [JWTs](https://www.youtube.com/watch?v=NcLtLZqGu2M) and [Refresh Tokens](https://www.youtube.com/watch?v=Wp4h_wYXqmU).

Some shortcuts were taken in the tutorial and I have tried to undo some bad practices.  The video series is worthwhile as a starting point and I have used it to give me some context of prior Spring Boot authentication implementation practices.  Even though the videos are fairly recent - some implementation details are now out of date.  Implementation details do change fairly frequently and it helps to be aware of the various past implementations to help convert to newer practices.  Most of the changes were confined to the Spring Configuration implementation.  

Unlike the video, I have used h2-console and made the necessary tweaks to have h2-console work with Spring Security.   

## Screenshots

![](screenshots/spring-auth-jt-1.png)

***

![](screenshots/spring-auth-jt-2.png)

***

![](screenshots/spring-auth-jt-3.png "Access Admin Protected Route with JWT")

***

![](screenshots/spring-auth-jt-4.png)

***

![](screenshots/spring-auth-jt-5.png "Denied after JWT expires")

***

![](screenshots/spring-auth-jt-6.png "Use token to get new access token")

***

![](screenshots/spring-auth-jt-7.png "Can access protected route again")

***

![](screenshots/spring-auth-jt-8.png "h2 console user info table")

***

![](screenshots/spring-auth-jt-9.png "h2 console refresh token table")

***

## Thoughts
 
- authorizeHttpRequests() implementation has changed - it takes a callback and you set the allowed routes as the return object
- The new implementation helps eliminate chaining and having to use `.and()`.   
- formLogin() deprecated and changed to use same pattern as authorizeHttpRequests()
- VS Code didn't suggest an import for withDefaults()
- "ROLE_" is a hassle that has to be accounted for when setting up role access.  However, hasRole does prepend "ROLE_" to the string you pass it.  
- name vs username variable - better to have the variable match the entity field name (unless not possible) ?  
- Slight differences in route names from second video to third video.  "/new" changed to "/signup" and "/authenticate" changed to "/login".  
- 302 Error Code on users/new post request if you don't disable CSRF
- Weird that most errors return 302 versus 400.  
- Using h2-console with spring security can be problematic - need to disable headers, allow access to the route and worry about x-frame-options (iframe will be denied)
- when logged in as an admin user and you go to a user role protected route, the error message is 403 but it still shows the fallback error page
- Refresh token string would need to be saved on frontend and then you would send request to backend when token got close to expiration time 
- allkeysgenerator site was not working when I checked - used openssl command to generate the secret key for jwt.  Better to put secret inside application properties and use variable to access it.  

## Useful Resources

- [Stack Overflow](https://stackoverflow.com/questions/41946473/springboot-security-hasrole-not-working) - hasRole not working
- [Spring Docs](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html) - authorize http requests
- [Stack Overflow](https://stackoverflow.com/questions/64191637/the-method-withdefaults-is-undefined-for-the-type-securityconfiguration) - withDefaults()
- [Spring Docs](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/basic.html) - authentication basic
- [Baeldung](https://www.baeldung.com/jpa-entities) - Entities
- [Stack Overflow](https://stackoverflow.com/questions/74680244/h2-database-console-not-opening-with-spring-security) - h2 console with spring security
- [Stack Overflow](https://stackoverflow.com/questions/26220083/h2-database-console-spring-boot-load-denied-by-x-frame-options) - spring boot deined by x frame options
- [Spring Docs](https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/headers.html#headers-frame-options) - headers frame options
- [Stack Overflow](https://stackoverflow.com/questions/50157911/spring-security-5-authentication-always-return-302) - authenthication always returns 302
- [Stack Overflow](https://stackoverflow.com/questions/30528255/how-to-access-a-value-defined-in-the-application-properties-file-in-spring-boot) - access a value defined in application properties file