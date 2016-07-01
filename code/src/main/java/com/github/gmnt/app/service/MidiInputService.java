package com.github.gmnt.app.service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Service;

import com.github.danielmenezes.note.Note;
import com.github.gmnt.app.service.MidiInputService.MidiInputDevice;

import jportmidi.JPortMidi;
import jportmidi.JPortMidiException;


@Service
public class MidiInputService 
	extends JPortMidi 
	implements ActionListener, HandleMidiInputListener  {
	
	//TODO: adaptar para que esse parâmetro seja utilizado na nova estrutura
	@Value("${midiinput}")
    private String midiinput;

	private Timer timer;
	private MessageSendingOperations<String> messagingTemplate;
	private static Log logger = LogFactory.getLog(MidiInputService.class);
	private List<HandleMidiInputListener> listeners = new ArrayList<HandleMidiInputListener>(); 

	@Autowired
	public MidiInputService(MessageSendingOperations<String> messagingTemplate) throws JPortMidiException {
		super();
		this.messagingTemplate = messagingTemplate;
		addHandleMidiInputListener(this);
		
	}
	
	public void addHandleMidiInputListener(HandleMidiInputListener listener) {
		listeners.add(listener);
	}

	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) throws JPortMidiException {
		//loadDeviceChoices();
	}

	public void noteOn(int channel, int pitch, int velocity) {
		sendNote(channel, pitch, velocity, true);
	}
	
	public void noteOff(int channel, int pitch, int velocity) {
		sendNote(channel, pitch, velocity, false);
	}
	
	private void sendNote(int channel, int pitch, int velocity, boolean on) {
		String note = Note.toNoteWithOctave(pitch);
		logger.info("Note: " + note + " On: " + on);
		MidiInput midiInput = new MidiInput(note.trim(), on);
		
		for (HandleMidiInputListener listerner : this.listeners) {
			listerner.handleMidiInputNote(midiInput);
		}
	}
	
	@Override
	public void handleMidiInputNote(MidiInput note) {
		this.messagingTemplate.convertAndSend("/topic/midiinput", note);
	}
	
	

	public void poll(int ms) throws JPortMidiException {
		super.poll();
	}

	public void loadDeviceChoices() throws JPortMidiException {
		System.out.println(String.format("MidiInput Name: %s", midiinput));		
		timer = new Timer(50 /* ms */, this);
		timer.addActionListener(this);
		this.setTrace(true);

		int n = this.countDevices();
		int id = -1;
		for (int i = 0; i < n; i++) {
			String interf = this.getDeviceInterf(i);
			String name = this.getDeviceName(i);

			String selection = name + " [" + interf + "]";

			if (this.getDeviceInput(i)) {
				if (midiinput.equals(name)) {
					System.out.println("name " + selection);
					id = i;
					break;
				}
			}
		}

		if (id != -1) {
			this.openInput(id, 100); // buffer size hopes to avoid overflow
			timer.start(); // don't start timer if there's an error
		}		
	}
	
	public void openInput(MidiInputDevice input) throws JPortMidiException {
		timer = new Timer(50 /* ms */, this);
		timer.addActionListener(this);
		super.openInput(input.getId(), 100);
		//TODO: don't start timer if there's an error
		timer.start();
	}
	
	public static class MidiInputDevice {
		
		public static MidiInputDevice DEVICE_FAKE_SELECT = new MidiInputDevice(-1, "(selecione o MIDI INPUT)", "(selecione o MIDI INPUT)");
		
		private int id;
		private String name;
		private String interf;
		public MidiInputDevice(int id, String name, String interf) {
			super();
			this.id = id;
			this.name = name;
			this.interf = interf;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getInterf() {
			return interf;
		}
		public void setInterf(String interf) {
			this.interf = interf;
		}
		public String getSelection() {
			return name + " [" + interf + "]";
		}
		@Override
		public String toString() {
			return name;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MidiInputDevice other = (MidiInputDevice) obj;
			if (id != other.id)
				return false;
			return true;
		}
		
	};
	
	public List<MidiInputDevice> listMidiInput() throws JPortMidiException {
		List<MidiInputDevice> result = new ArrayList<MidiInputDevice>();
		int n = this.countDevices();
		for (int i = 0; i < n; i++) {
			String interf = this.getDeviceInterf(i);
			String name = this.getDeviceName(i);

			if (this.getDeviceInput(i)) {
				result.add(new MidiInputDevice(i, name, interf));
			}
		}
		return result;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		try {
			if (source == timer) {
				this.poll(this.timeGet());
			}
		} catch(JPortMidiException ex) {
			System.out.println(ex);
		}
	}

	

}