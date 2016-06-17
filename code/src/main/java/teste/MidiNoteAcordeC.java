  package teste;
/*
 *	MidiNote.java
 *
 *	This file is part of jsresources.org
 */

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 1999 - 2006 by Matthias Pfisterer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;


// TODO: an optional delay parameter that is added to getMicrosecondPosition to be used as timestamp for the event delivery.

/**	<titleabbrev>MidiNote</titleabbrev>
	<title>Playing a note on a MIDI device</title>

	<formalpara><title>Purpose</title>
	<para>Plays a single note on a MIDI device. The MIDI device can
	be a software synthesizer, an internal hardware synthesizer or
	any device connected to the MIDI OUT port.</para>
	</formalpara>

	<formalpara><title>Usage</title>
	<para>
	<cmdsynopsis><command>java MidiNote</command>
	<arg choice="opt"><replaceable class="parameter">devicename</replaceable></arg>
	<arg choice="plain"><replaceable class="parameter">keynumber</replaceable></arg>
	<arg choice="plain"><replaceable class="parameter">velocity</replaceable></arg>
	<arg choice="plain"><replaceable class="parameter">duration</replaceable></arg>
	</cmdsynopsis>
	</para></formalpara>

	<formalpara><title>Parameters</title>
	<variablelist>
	<varlistentry>
	<term><replaceable class="parameter">devicename</replaceable></term>
	<listitem><para>the name of the device to send the MIDI messages to</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><replaceable class="parameter">keynumber</replaceable></term>
	<listitem><para>the MIDI key number</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><replaceable class="parameter">velocity</replaceable></term>
	<listitem><para>the velocity</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><replaceable class="parameter">duration</replaceable></term>
	<listitem><para>the duration in milliseconds</para></listitem>
	</varlistentry>
	</variablelist>
	</formalpara>

	<formalpara><title>Bugs, limitations</title>
	<para>Not well-tested.</para>
	</formalpara>

	<formalpara><title>Source code</title>
	<para>
	<ulink url="MidiNote.java.html">MidiNote.java</ulink>,
	<ulink url="MidiCommon.java.html">MidiCommon.java</ulink>
	</para>
	</formalpara>

*/
public class MidiNoteAcordeC {
	public static void main(String[] args) {	
		List<Integer> notes = new ArrayList<Integer>();
		notes.add(60); //C
		notes.add(64); //E
		notes.add(67); //G
		
		MidiNotePlay.play(notes, "DANIEL");	
	}
}



/*** MidiNote.java ***/