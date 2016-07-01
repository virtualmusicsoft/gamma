package com.github.gmnt.app.service;

import com.teamdev.jxbrowser.chromium.Browser;

public class BrowserService implements Runnable {
	
	private Browser browser;
	private boolean isReady;
	
	private static final BrowserService instance = new BrowserService();
	 
	protected BrowserService() {
	}
 
	// Runtime initialization
	// By defualt ThreadSafe
	public static BrowserService getInstance() {
		return instance;
	}

	@Override
	public void run() {
		browser = new Browser();
		isReady = true;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public Browser getBrowser() {
		return browser;
	}

}
