package teste;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class StompClientTeste {
	
	@Autowired
	public TaskScheduler taskScheduler;
	
	public static void main(String[] args) throws IOException {
		StompClientTeste c = new StompClientTeste();
		
		List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport( new StandardWebSocketClient()) );
		WebSocketClient transport = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(transport);
		
//		WebSocketClient transport = new StandardWebSocketClient();
//		WebSocketStompClient stompClient = new WebSocketStompClient(transport);
//		stompClient.setMessageConverter(new StringMessageConverter());
		//TaskScheduler 
		
		stompClient.setTaskScheduler(new ThreadPoolTaskScheduler()); // for heartbeats, receipts
		
		String url = "ws://127.0.0.1:8080/hello";
		StompSessionHandler handler = new MySessionHandler() ;
		stompClient.connect(url, handler);
		System.in.read();
	}

}
