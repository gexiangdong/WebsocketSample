package cn.devmgr.websocketsample;

import java.security.Principal;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket/sample").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
    	registration.setInterceptors(new MyChannelInterceptorAdapter());
    }
    
    public class MyChannelInterceptorAdapter extends ChannelInterceptorAdapter{
    	
    	
    	@Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {

            StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                String name = accessor.getNativeHeader("access-token").get(0);
//            	System.out.println("CONNECT:" +  mhs + "--" + name );
                if(name == null){
                	name = "GUEST";
                }
                System.out.println("MyChannelInterceptorAdapter->preSend() ... " + name);
                User user = new User(name);
                accessor.setUser(user);
            }

            return message;
        }
    }
    
    public class User implements Principal{
    	private String name;
    	
    	public User(String name){
    		this.name = name;
    	}
		@Override
		public String getName() {
			System.out.println("Principal.getName() return " + name );
			return name;
		}
    	
    	
    }
}