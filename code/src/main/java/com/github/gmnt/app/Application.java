package com.github.gmnt.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.concurrent.Future;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import com.github.gmnt.app.service.BrowserService;
import com.github.gmnt.app.service.IonicServerService;
import com.github.gmnt.app.service.IonicServerService.IonicServerResult;

import jportmidi.JPortMidiException;

@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration
@ComponentScan
public class Application {
	
    public static SplashScreen mySplash;                   // instantiated by JVM we use it to get graphics
    static Graphics2D splashGraphics;               // graphics context for overlay of the splash image
    static Rectangle2D.Double splashTextArea;       // area where we draw the text
    static Rectangle2D.Double splashProgressArea;   // area where we draw the progress bar
    static Font font;                               // used to draw our text
    static boolean showProgressArea = false;
    
    public static void main(String[] args) throws JPortMidiException, InterruptedException, IOException {
    	splashInit();           // initialize splash overlay drawing parameters
        appInit(args);              // simulate what an application would do before starting
        //if (mySplash != null)   // check if we really had a spash screen
        //    mySplash.close();   // we're done with it
        
        // begin with the interactive portion of the program	
    }
    
    /**
     * just a stub to simulate a long initialization task that updates
     * the text and progress parts of the status in the Splash
     * @throws IOException 
     * @throws InterruptedException 
     */
    private static void appInit(String[] args) throws InterruptedException, IOException {
    	checkEnveriment();
    	startBrowser();
    	ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class).headless(false).run(args);
    	//startIonicServer(context);
        AppPrincipalFrame appFrame = context.getBean(AppPrincipalFrame.class);
        appFrame.startUp();
    }
    
    private static void startBrowser() {
    	(new Thread(BrowserService.getInstance())).start();
    }
    
    private static void startIonicServer(ConfigurableApplicationContext context) {
    	IonicServerService ionicServerService = context.getBean(IonicServerService.class);
    	try {
			Future<IonicServerResult> result = ionicServerService.startIonicServer();
			while (!result.isDone()) {
				Thread.sleep(1000);
			}
			int i = 0;
			i++;
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

    /**
     * Prepare the global variables for the other splash functions
     */
    private static void splashInit()
    {
        // the splash screen object is created by the JVM, if it is displaying a splash image
        
        mySplash = SplashScreen.getSplashScreen();
        // if there are any problems displaying the splash image
        // the call to getSplashScreen will returned null

        if (mySplash != null)
        {
            // get the size of the image now being displayed
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;

            // stake out some area for our status information
            splashTextArea = new Rectangle2D.Double(15., height*0.88, width * .45, 32.);
            splashProgressArea = new Rectangle2D.Double(width * .55, height*.92, width*.4, 12 );

            // create the Graphics environment for drawing status info
            splashGraphics = mySplash.createGraphics();
            font = new Font("Dialog", Font.PLAIN, 14);
            splashGraphics.setFont(font);

            // initialize the status info
            splashText("Carregando...");
            splashProgress(0);
        }
    }
    /**
     * Display text in status area of Splash.  Note: no validation it will fit.
     * @param str - text to be displayed
     */
    public static void splashText(String str)
    {
        if (mySplash != null && mySplash.isVisible())
        {   // important to check here so no other methods need to know if there
            // really is a Splash being displayed

            // erase the last status text
            splashGraphics.setPaint(Color.LIGHT_GRAY);
            splashGraphics.fill(splashTextArea);

            // draw the text
            splashGraphics.setPaint(Color.BLACK);
            splashGraphics.drawString(str, (int)(splashTextArea.getX() + 10),(int)(splashTextArea.getY() + 15));

            // make sure it's displayed
            mySplash.update();
        }
    }
    /**
     * Display a (very) basic progress bar
     * @param pct how much of the progress bar to display 0-100
     */
    public static void splashProgress(int pct)
    {
        if (mySplash != null && mySplash.isVisible() && showProgressArea)
        {

            // Note: 3 colors are used here to demonstrate steps
            // erase the old one
            splashGraphics.setPaint(Color.LIGHT_GRAY);
            
            splashGraphics.fill(splashProgressArea);

            // draw an outline
            splashGraphics.setPaint(Color.BLUE);
            splashGraphics.draw(splashProgressArea);

            // Calculate the width corresponding to the correct percentage
            int x = (int) splashProgressArea.getMinX();
            int y = (int) splashProgressArea.getMinY();
            int wid = (int) splashProgressArea.getWidth();
            int hgt = (int) splashProgressArea.getHeight();
            

            int doneWidth = Math.round(pct*wid/100.f);
            doneWidth = Math.max(0, Math.min(doneWidth, wid-1));  // limit 0-width

            // fill the done part one pixel smaller than the outline
            splashGraphics.setPaint(Color.GREEN);
            splashGraphics.fillRect(x, y+1, doneWidth, hgt-1);

            // make sure it's displayed
            mySplash.update();
        }
    }
    

    
    private static void checkEnveriment() {
    	checkJava32bits();
    	configPort("server.port", 4444);
    	configPort("client.port", 55555);
    	//configPort("managent.port", 20000);
    }
    
    private static void checkJava32bits() {
    	if (!"32".equals(System.getProperty("sun.arch.data.model"))) {
    		//TODO: internationalization
    		JDialog dialog = new JOptionPane("É necessário Java 32 bits.",
    				JOptionPane.ERROR_MESSAGE,
    				JOptionPane.DEFAULT_OPTION).createDialog("Erro");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            dialog.dispose();
    		System.exit(-1);
    	}
    }
    
    /**
     * Porta padrão é port.
     * Caso port esteja em uso, uma nova porta acima de port é utilizada.
     */
    private static void configPort(String systemProperties, int port) {
    	String newPort = System.getProperties().getProperty(systemProperties);
    	if (newPort == null) {
    		newPort = String.valueOf(port - 1);
    	}
    	boolean found = false;
    	do {
    		newPort = String.valueOf(Integer.valueOf(newPort) + 1); 
    		found = availablePort(Integer.valueOf(newPort));
    	} while(!found);
    	
    	System.getProperties().put(systemProperties, newPort );
    }
    
    public static boolean availablePort(int port) {
        if (port < 10 || port > 65535) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                }
            }
        }

        return false;
    }

}
