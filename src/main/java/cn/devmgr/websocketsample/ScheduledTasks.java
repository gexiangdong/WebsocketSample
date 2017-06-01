package cn.devmgr.websocketsample;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private SimpMessagingTemplate template;
    
    @Scheduled(fixedRate = 5000)
    @SendTo("/queue/greetings")
    public void reportCurrentTime() throws Exception{
        log.info("The time is now {}", dateFormat.format(new Date()));
        System.out.println("scheduleTask------>reportCurrentTime");
        
        this.template.convertAndSend("/queue/greetings",  new Greeting("Hello, it's " + dateFormat.format(new Date()) + " now!"));
        this.template.convertAndSendToUser("000", "/queue/greetings",  new Greeting("Konichiwa 000, it's " + dateFormat.format(new Date()) + " now!"));
  
    }
}
