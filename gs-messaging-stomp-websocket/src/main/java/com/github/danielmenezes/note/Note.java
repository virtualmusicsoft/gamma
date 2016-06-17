package com.github.danielmenezes.note;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Note {

	private BufferedImage img;
	private ImageIcon icon;
	private JLabel label;
	private String name;
	
	public Note(String name) {
		this.name = name;
	}

	public void load(String path) {
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			throw new IllegalStateException("N�o foi poss�vel carregar a imagem.");
		}
		icon = new ImageIcon(img);
		label = new JLabel(icon);
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public static String toBaseNote(int pitch) {
		return "C C#D D#E F F#G G#A A#B ".substring(
				(pitch % 12) * 2,
				(pitch % 12) * 2 + 2);

	}
	
	public static String toNoteWithOctave(int pitch) {
		String notes = "C C#D D#E F F#G G#A A#B ";
		int octave;
		String note;

		octave = pitch / 12 - 1;
		note = notes.substring(
				(pitch % 12) * 2,
				(pitch % 12) * 2 + 2);

		return note.trim() + String.valueOf(octave);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
