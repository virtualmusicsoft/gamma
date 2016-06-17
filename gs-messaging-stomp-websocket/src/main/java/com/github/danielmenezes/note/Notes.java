package com.github.danielmenezes.note;

import java.util.ArrayList;
import java.util.List;

public class Notes {
	public static NoteC C = new NoteC();
	public static NoteD D = new NoteD();
	
	public static List<Note> ALL = new ArrayList<Note>();
	
	static {
		Notes.ALL.add(Notes.C);
		Notes.ALL.add(Notes.D);		
	}
	
	

}
