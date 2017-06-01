package cn.devmgr.websocketsample;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {


    @MessageMapping("/helloall")
    @SendTo("/queue/greetings")
    public Greeting greetingAll(HelloMessage message, Principal principal) throws Exception {
        Thread.sleep(100); // simulated delay
        return new Greeting("hi all,  " + message.getName() + "! from "  + principal.getName());
    }

    @MessageMapping("/hello")
    @SendToUser("/queue/greetings")
    public Greeting greeting(HelloMessage message, Principal principal) throws Exception {
        Thread.sleep(100); // simulated delay
        return new Greeting("hi, " + message.getName() + "! from " + principal.getName());
    }
}
