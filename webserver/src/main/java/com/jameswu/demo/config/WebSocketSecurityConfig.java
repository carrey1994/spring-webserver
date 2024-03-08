// package com.jameswu.demo.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.messaging.Message;
// import org.springframework.messaging.simp.SimpMessageType;
// import org.springframework.security.authorization.AuthorizationManager;
// import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
// import
// org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
//
// @Configuration
// @EnableWebSocketSecurity
// public class WebSocketSecurityConfig {
//
//    @Bean
//    public AuthorizationManager<Message<?>>
// messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
//        messages.anyMessage().permitAll();
////        messages
////                .anyMessage().permitAll();
////                .nullDestMatcher().authenticated()
////                .simpSubscribeDestMatchers("/user/queue/errors").permitAll()
////                .simpDestMatchers("/app/**").hasRole("USER")
////                .simpSubscribeDestMatchers("/user/**", "/topic/friends/*").hasRole("USER")
////                .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll()
////                .anyMessage().denyAll();
//        return messages.build();
//    }
// }
