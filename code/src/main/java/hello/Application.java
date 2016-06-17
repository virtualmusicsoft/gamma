package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jportmidi.JPortMidiException;

@SpringBootApplication
public class Application {
	
    public static void main(String[] args) throws JPortMidiException {
    	checkJava32bits();
        SpringApplication.run(Application.class, args);
    }
    
    private static void checkEnveriment() {
    	checkJava32bits();
    }
    
    private static void checkJava32bits() {
    	if (!"32".equals(System.getProperty("sun.arch.data.model"))) {
    		//TODO: internationalization
    		System.out.println("You need a Java 32 bits!");
    		System.exit(-1);
    	}
    }

}
