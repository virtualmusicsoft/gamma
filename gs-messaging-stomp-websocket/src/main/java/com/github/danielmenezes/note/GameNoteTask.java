package com.github.danielmenezes.note;

import java.util.Timer;
import java.util.TimerTask;

public class GameNoteTask extends TimerTask {
	private int seconds = 8;
	private int i = 0;
	private boolean runnning = false;
	private NoteRandom noteRandom = new NoteRandom();

	@Override
	public void run() {
		if (runnning) {
			i++;

			if(i % seconds == 0) {
				System.out.println("Timer action!");
				noteRandom.generateNextNote();
				debugCurrentNote();
				reset();
			} else {
				System.out.println("Time left:" + (seconds - (i %seconds)) );
			}
		}
	}

	public void reset() {
		i = 0;
	}

	public void start() {
		runnning = true;
		noteRandom.generateNextNote();
		debugCurrentNote();
	}

	public void stop() {
		runnning = true;
	}
	
	public void debugCurrentNote() {
		System.out.println(noteRandom.getCurrentNote().getName());
	}


	public static void main(String[] args) {
		GameNoteTask task = new GameNoteTask();
		Timer timer = new Timer();
		timer.schedule(task, 0, 1000);
		task.start();
	}


}
