package com.jameswu.demo.controller;

import com.jameswu.demo.model.Greeting;
import com.jameswu.demo.model.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class ChatController {

    //    @Autowired
    //    private SimpMessagingTemplate messagingTemplate;

    //    @MessageMapping("/ask/{roomId}")
    //    @SendTo("/greetings/ask/{roomId}")
    //    public void greeting(@DestinationVariable int roomId, String ask) {
    //        messagingTemplate.convertAndSend("/greetings/answer/" + roomId, ask);
    //    }
    //
    //    @MessageMapping("/answer/{roomId}")
    //    @SendTo("/greetings/answer/{roomId}")
    //    public void answer(@DestinationVariable int roomId, String message) {
    //        messagingTemplate.convertAndSend("/greetings/ask/" + roomId, message);
    //    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.name()) + "!");
    }
}
