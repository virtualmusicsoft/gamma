package com.github.danielmenezes.note;

import java.util.concurrent.ThreadLocalRandom;

public class NoteRandom {
	
	private Note currentNote;
	private Note priorNote;
	
	public Note generateNextNote() {
		int index = ThreadLocalRandom.current().nextInt(0, Notes.ALL.size());
		priorNote = currentNote;
		currentNote = Notes.ALL.get(index);
		return currentNote;
	}

	public Note getCurrentNote() {
		return currentNote;
	}

	public void setCurrentNote(Note currentNote) {
		this.currentNote = currentNote;
	}

	public Note getPriorNote() {
		return priorNote;
	}

	public void setPriorNote(Note priorNote) {
		this.priorNote = priorNote;
	}

}
