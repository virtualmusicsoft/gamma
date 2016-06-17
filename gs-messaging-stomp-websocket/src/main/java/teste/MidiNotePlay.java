  package teste;


import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;


public class MidiNotePlay {
	/**	Flag for debugging messages.
	 	If true, some messages are dumped to the console
	 	during operation.
	*/
	private static boolean		DEBUG = true;

	public static void play(List<Integer> notes, String	strDeviceName) {
		// TODO: make settable via command line
		int	nChannel = 0;

		int	nKey = 0;	// MIDI key number
		int	nVelocity = 0;

		/*
		 *	Time between note on and note off event in
		 *	milliseconds. Note that on most systems, the
		 *	best resolution you can expect are 10 ms.
		 */
		int	nDuration = 0;
		//int	nArgumentIndexOffset = 0;
		/*if (args.length == 4)
		{
			strDeviceName = args[0];
			nArgumentIndexOffset = 1;
		}
		else if (args.length == 3)
		{
			nArgumentIndexOffset = 0;
		}
		else
		{
			printUsageAndExit();
		}*/
		
		
		nKey = 60;
		nKey = Math.min(127, Math.max(0, nKey));
		nVelocity = 100;
		nVelocity = Math.min(127, Math.max(0, nVelocity));
		nDuration = 3000;
		nDuration = Math.max(0, nDuration);


		MidiDevice	outputDevice = null;
		Receiver	receiver = null;
		if (strDeviceName != null)
		{
			MidiDevice.Info	info = MidiCommon.getMidiDeviceInfo(strDeviceName, true);
			if (info == null)
			{
				out("no device info found for name " + strDeviceName);
				System.exit(1);
			}
			try
			{
				outputDevice = MidiSystem.getMidiDevice(info);
				if (DEBUG) out("MidiDevice: " + outputDevice);
				outputDevice.open();
			}
			catch (MidiUnavailableException e)
			{
				if (DEBUG) out(e);
			}
			if (outputDevice == null)
			{
				out("wasn't able to retrieve MidiDevice");
				System.exit(1);
			}
			try
			{
				receiver = outputDevice.getReceiver();
			}
			catch (MidiUnavailableException e)
			{
				if (DEBUG) out(e);
			}
		} else {
			/*	We retrieve a Receiver for the default
				MidiDevice.
			*/
			try
			{
				receiver = MidiSystem.getReceiver();
			}
			catch (MidiUnavailableException e)
			{
				if (DEBUG) { out(e); }
			}
		}
		
		if (receiver == null)
		{
			out("wasn't able to retrieve Receiver");
			System.exit(1);
		}

		if (DEBUG) out("Receiver: " + receiver);
		/*	Here, we prepare the MIDI messages to send.
			Obviously, one is for turning the key on and
			one for turning it off.
		*/
		
		for (Integer key : notes) {
			sendNote(key, nVelocity, nChannel, receiver);
		}

		/*
		 *	Wait for the specified amount of time
		 *	(the duration of the note).
		 */
		try {
			Thread.sleep(nDuration);
		} catch (InterruptedException e) {
			if (DEBUG) out(e);
		}

		/*
		 *	Turn the note off.
		 */
		if (DEBUG) out("sending off message...");
		
		
		//TODO: enviar OFF
		for(Integer key : notes) {
			ShortMessage	offMessage = null;
			offMessage = new ShortMessage();
			try {
				offMessage.setMessage(ShortMessage.NOTE_OFF, nChannel, key, 0);
			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			if (DEBUG) {
			    out("Off Msg: " + offMessage.getStatus() + " " + offMessage.getData1() + " " + offMessage.getData2());
			}
			    
			receiver.send(offMessage, -1);
			
		}
		
		
		
		if (DEBUG) out("...sent");

		/*
		 *	Clean up.
		 */
		receiver.close();
		if (outputDevice != null) {
			outputDevice.close();
		}
	}



	private static void printUsageAndExit() {
		out("MidiNote: usage:");
		out("  java MidiNote [<device name>] <note number> <velocity> <duration>");
		out("    <device name>\toutput to named device");
		System.exit(1);
	}



	private static void out(String strMessage) {
		System.out.println(strMessage);
	}



	private static void out(Throwable t) {
		t.printStackTrace();
	}
	
	private static void sendNote(int nKey, int nVelocity, int nChannel, Receiver receiver) {
		ShortMessage	onMessage = null;
		try
		{
			onMessage = new ShortMessage();
			
			onMessage.setMessage(ShortMessage.NOTE_ON, nChannel, nKey, nVelocity);

			if (DEBUG) {
			    out("On Msg: " + onMessage.getStatus() + " " + onMessage.getData1() + " " + onMessage.getData2());
			}
		}
		catch (InvalidMidiDataException e)
		{
			if (DEBUG) { out(e); }
		}

		/*
		 *	Turn the note on
		 */
		if (DEBUG) out("sending on message...");
		receiver.send(onMessage, -1);
		if (DEBUG) out("...sent");
	}
}



/*** MidiNote.java ***/