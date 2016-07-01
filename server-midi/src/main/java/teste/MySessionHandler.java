package teste;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.github.gmnt.app.service.Greeting;

public class MySessionHandler extends StompSessionHandlerAdapter {
	

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        // ...
    	System.out.println("afterConnected");    	
    	session.subscribe("/topic/greetings", new StompFrameHandler() {

    	    @Override
    	    public Type getPayloadType(StompHeaders headers) {
    	    	System.out.println("getPayloadType");
    	    	return byte[].class;
    	    }

    	    @Override
    	    public void handleFrame(StompHeaders headers, Object payload) {
    	    	String json = new String((byte[]) payload);
    	    	System.out.println("handleFrame");
    	    }

    	});
    	session.send("/topic/greetings", new Greeting("DANIEL"));
    }
}