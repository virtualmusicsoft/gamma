package com.github.gmnt.app.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

//  @RequestMapping("/*")
//  void handleFoo(HttpServletResponse response) throws IOException {
//	String hostName = InetAddress.getLocalHost().getHostName();
//    String server = String.format("%s:%s", hostName, System.getProperties().getProperty("server.port"));
//	String uri = String.format("http://%s:%s/?h=%s", hostName, System.getProperties().getProperty("client.port"), encodeURIComponent(server));
//    response.sendRedirect(uri);
//  }
//  
  public static String encodeURIComponent(String s) {
	    String result;

	    try {
	        result = URLEncoder.encode(s, "UTF-8")
	                .replaceAll("\\+", "%20")
	                .replaceAll("\\%21", "!")
	                .replaceAll("\\%27", "'")
	                .replaceAll("\\%28", "(")
	                .replaceAll("\\%29", ")")
	                .replaceAll("\\%7E", "~");
	    } catch (UnsupportedEncodingException e) {
	        result = s;
	    }

	    return result;
	}

}