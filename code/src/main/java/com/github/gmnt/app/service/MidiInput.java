package com.github.gmnt.app.service;

public class MidiInput {
	
	private String note;
	private boolean on;
	
	public MidiInput(String note, boolean on) {
		super();
		this.note = note;
		this.on = on;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public boolean getOn() {
		return on;
	}
	public void setOn(boolean on) {
		this.on = on;
	}

}
