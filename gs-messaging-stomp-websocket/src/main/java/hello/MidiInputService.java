package hello;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import jportmidi.JPortMidi;
import jportmidi.JPortMidiException;


@Service
public class MidiInputService 
	extends JPortMidi 
	implements ActionListener  {
	
	@Value("${midiinput}")
    private String midiinput;

	private Timer timer;
	private MessageSendingOperations<String> messagingTemplate;
	private static Log logger = LogFactory.getLog(MidiInputService.class);

	@Autowired
	public MidiInputService(MessageSendingOperations<String> messagingTemplate) throws JPortMidiException {
		super();
		this.messagingTemplate = messagingTemplate;
	}

	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) throws JPortMidiException {
		loadDeviceChoices();
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
		this.messagingTemplate.convertAndSend("/topic/midiinput", midiInput);
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