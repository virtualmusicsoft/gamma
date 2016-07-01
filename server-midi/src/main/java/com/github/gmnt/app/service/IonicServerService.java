package com.github.gmnt.app.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class IonicServerService {

	@Async
	public Future<IonicServerResult> startIonicServer() throws InterruptedException, IOException {
		IonicServerResult ionicResult = new IonicServerResult();		
		
		String clientDirectory = Paths.get("front-end-ionic2").toAbsolutePath().normalize().toString();

		List<String> commands = new LinkedList<String>();
		commands.add(Paths.get("tools", "nodejs", "node.exe").toAbsolutePath().normalize().toString());
		//commands.add("--version");
		commands.add(Paths.get("tools", "npm", "node_modules", "ionic", "bin", "ionic").toAbsolutePath().normalize().toString());
		commands.add("serve");
		commands.add("--all");
		commands.add("--nobrowser");
		commands.add("--nolivereload");
		commands.add("--noproxy");
		String port = System.getProperties().getProperty("client.port");
		commands.add("--port"); 
		commands.add(port);		

		ProcessBuilder pb = new ProcessBuilder()
				//.inheritIO()
				.command(commands);

		pb.directory(new File(clientDirectory));

		Process process = null;
		try {
			process = pb.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Thread errThread = new ThreadError(process, ionicResult);
		errThread.start();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = null;
		while ((line = reader.readLine()) != null && ionicResult.status != IonicServerStatus.ERROR) {
			//TODO: put line in log
			if (line.contains("Ionic server commands, enter:")) {
				ionicResult.setStatus(IonicServerStatus.SUCCESS);
				return new AsyncResult<IonicServerResult>(ionicResult);
			}
		}

		process.waitFor();
		errThread.join();
		return new AsyncResult<IonicServerResult>(ionicResult);
	}
	
	public enum IonicServerStatus {
		STARTING,
		ERROR,
		SUCCESS
	}
	
	public class IonicServerResult {
		
		private IonicServerStatus status = IonicServerStatus.STARTING; 
		private StringBuilder builderError = new StringBuilder();
		public IonicServerStatus getStatus() {
			return status;
		}
		public void setStatus(IonicServerStatus status) {
			this.status = status;
		}
		public StringBuilder getBuilderError() {
			return builderError;
		}
		public void setBuilderError(StringBuilder builderError) {
			this.builderError = builderError;
		}
		
	}
	
	public class ThreadError extends Thread {
		
		private Process process;
		private IonicServerResult ionicResult;
		
		public ThreadError(Process process, IonicServerResult ionicResult) {
			super();
			this.process = process;
			this.ionicResult = ionicResult;
		}

		@Override
		public void run() {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
				String line = null;
				while ( (line = reader.readLine()) != null) {
					ionicResult.getBuilderError().append(line);
					ionicResult.getBuilderError().append(System.getProperty("line.separator"));
					ionicResult.setStatus(IonicServerStatus.ERROR);
				}
			} catch (Exception e) {
			}
		}
		
	}

}
