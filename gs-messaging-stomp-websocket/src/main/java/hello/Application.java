package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jportmidi.JPortMidiException;

@SpringBootApplication
public class Application {
	
    public static void main(String[] args) throws JPortMidiException {
        SpringApplication.run(Application.class, args);
    }

}
