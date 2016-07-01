package com.github.gmnt.app.launcher;

import com.github.gmnt.app.Application;

public class SpyLauncher {
	
	private boolean continueExecution = true;
	private int serverPort;
	private int clientPort;
	
	public static void main(String args) throws InterruptedException {
		SpyLauncher spy = new SpyLauncher();
		spy.init(args);
		spy.startCLient();
		spy.run();
	}
	
	public void init(String args) {
		
	}
	
	public void run() throws InterruptedException {
		while(continueExecution) {
			Thread.sleep(1000);
			if (Application.availablePort(serverPort)) {
				shutdownClient();
				continueExecution = false;
			}
		}
	}
	
	private void shutdownClient() {
		
	}
	
	private void startCLient() {
		
	}

}
