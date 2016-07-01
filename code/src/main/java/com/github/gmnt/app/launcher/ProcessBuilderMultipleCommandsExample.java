package com.github.gmnt.app.launcher;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ProcessBuilderMultipleCommandsExample {
	public static void main(String[] args) throws InterruptedException,
	IOException {

		String clientDirectory = "C:\\Users\\bytecode\\git\\Gama-Music-Notation-Training\\code\\front-end-ionic2";
		// multiple commands
		// /C Carries out the command specified by string and then terminates
		/*ProcessBuilder pb = new ProcessBuilder("cmd.exe",
				"/C dir & echo example of & echo working dir");*/

		List<String> commands = new LinkedList<String>();
		commands.add("node");
		//commands.add("--version");
		commands.add("C:\\Users\\bytecode\\AppData\\Roaming\\npm\\node_modules\\ionic\\bin\\ionic\\");
		commands.add("serve");

		ProcessBuilder pb = new ProcessBuilder()
									//.inheritIO()
									.command(commands);

		//		ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\nodejs\\node.exe",
		//				"C:\\Users\\bytecode\\AppData\\Roaming\\npm\\node_modules\\ionic\\bin\\ionic\\ serve");
		pb.directory(new File(clientDirectory));

		Process process = pb.start();
		
		Thread errThread = new Thread(() -> {
		    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
		      // Read stream here
		    } catch (Exception e) {
		    }
		});
		errThread.start();
		
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ( (line = reader.readLine()) != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}
		String result = builder.toString();
		System.out.println("Resultado: " + result);


		int i = process.waitFor();
		

		
	}
	
	private static String getInputAsString(InputStream is)
	{
	   try(java.util.Scanner s = new java.util.Scanner(is)) 
	   { 
	       return s.useDelimiter("\\A").hasNext() ? s.next() : ""; 
	   }
	}
}