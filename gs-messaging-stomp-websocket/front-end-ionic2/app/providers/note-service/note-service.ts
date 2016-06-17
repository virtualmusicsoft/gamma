import {Injectable} from '@angular/core';


  
export class NoteHtml {
  
  constructor(public src:string, public left:string, public top:string) {}
  
  static Default = class {
    static left_upwline : string = "80px";
    static left_upwoline : string = "86px";
  }
  
  
  
   static Clave = class {
    static Sol = class {
      static C = new NoteHtml("img/note_up_w_line.png", NoteHtml.Default.left_upwline, "68px");
      static D = new NoteHtml("img/note_up_wo_line.png", NoteHtml.Default.left_upwoline, "60px");
      static E = new NoteHtml("img/note_up_wo_line.png", NoteHtml.Default.left_upwoline, "55px");
      static F = new NoteHtml("img/note_up_wo_line.png", NoteHtml.Default.left_upwoline, "49px");
      static G = new NoteHtml("img/note_up_wo_line.png", NoteHtml.Default.left_upwoline, "43px"); 
    }
    static Fa = class {
      static C = new NoteHtml("img/note_down_w_line.png", NoteHtml.Default.left_upwline, "160px");
      static B = new NoteHtml("img/note_down_wo_line.png", NoteHtml.Default.left_upwoline, "167px");
      static A = new NoteHtml("img/note_down_wo_line.png", NoteHtml.Default.left_upwoline, "172px");
      static G = new NoteHtml("img/note_down_wo_line.png", NoteHtml.Default.left_upwoline, "178px");
      static F = new NoteHtml("img/note_down_wo_line.png", NoteHtml.Default.left_upwoline, "184px"); 
    }
  }
}


export class BasicNote {
    constructor(public note:string) {
      
    }
    
    //TODO: Fazer com as notas #
    static C = new BasicNote("C");
    static D = new BasicNote("D");
    static E = new BasicNote("E");
    static F = new BasicNote("F");
    static G = new BasicNote("G");
    static A = new BasicNote("A");
    static B = new BasicNote("B");
    static ALL : BasicNote[] = [BasicNote.C, BasicNote.D, BasicNote.E, BasicNote.F, BasicNote.G, BasicNote.A, BasicNote.B];
    
    static valueOf(noteWithOctave:string) {
      var strNote : string = BasicNote.getNote(noteWithOctave);
      for (var item of BasicNote.ALL) {
        if (item.note == strNote) {
          return item;
        }
      }
      throw new Error("Invalid noteWithOctave: " + noteWithOctave);      
    }
    
    static getNote(noteWithOctave:string) {
      var index = noteWithOctave.search(/\d/);
      return noteWithOctave.substring(0, index);
    }
    
    static getOctave(noteWithOctave:string) {
      var index = noteWithOctave.search(/\d/);
      return +noteWithOctave.substring(index, noteWithOctave.length);
    }
}
  
export class Note {
  public baseNote : BasicNote;
  public octave : number;
  constructor(public noteWithOctave:string, public info:NoteHtml) {
    this.baseNote = BasicNote.valueOf(noteWithOctave);
    this.octave = BasicNote.getOctave(noteWithOctave);
  }
  
  hasNoteHtml() {
    return (this.info == null);
  }
      
  static valueOf(noteWithOctave:string) {
    //TODO: cosiderar a clave de sol e fá ou apenas uma das duas no parâmetro da função
    var result:Note = ClaveSol.get(noteWithOctave);
    if (result == null) {
      result = ClaveFa.get(noteWithOctave);
    }
    if (result == null) {
      result = new Note(noteWithOctave, null);
    }
    return result;
  }
  
  static get(noteWithOctave:string, notes:Note[]) {
    var result = null;
    for (var item of notes) {
      if (item.noteWithOctave == noteWithOctave) {
        return item;
      }
    }
    return result;      
  }
}

export class ClaveSol extends Note {
  //TODO: considerar a configuração do Dó central
  //TODO: criar as notas com #
  static C = new Note("C4", NoteHtml.Clave.Sol.C);
  static D = new Note("D4", NoteHtml.Clave.Sol.D); 
  static E = new Note("E4", NoteHtml.Clave.Sol.E);
  static F = new Note("F4", NoteHtml.Clave.Sol.F);
  static G = new Note("G4", NoteHtml.Clave.Sol.G);
  static ALL : ClaveSol[]  = [ClaveSol.C, ClaveSol.D, ClaveSol.E, ClaveSol.F, ClaveSol.G];
  static get(noteWithOctave:string) {
    return Note.get(noteWithOctave, ClaveSol.ALL);      
  }
}

export class ClaveFa extends Note {
  //TODO: criar as notas com #
  static C = new Note("C4", NoteHtml.Clave.Fa.C);
  static B = new Note("B3", NoteHtml.Clave.Fa.B); 
  static A = new Note("A3", NoteHtml.Clave.Fa.A);
  static G = new Note("G3", NoteHtml.Clave.Fa.G);
  static F = new Note("F3", NoteHtml.Clave.Fa.F);
  static ALL : ClaveFa[]  = [ClaveFa.C, ClaveFa.B, ClaveFa.A, ClaveFa.G, ClaveFa.F];
  static get(noteWithOctave:string) {
    return Note.get(noteWithOctave, ClaveFa.ALL);      
  }
}

export class Chord {
  static C:Chord = new Chord("C", [BasicNote.C, BasicNote.E, BasicNote.G]);
  static D:Chord = new Chord("D", [BasicNote.D, BasicNote.F, BasicNote.A]);
  
  constructor( private name:String, private notes:BasicNote[]) {
  }
  
  getNotes():BasicNote[] {
    return this.notes;
  }
  
  getName() {
    return this.name;
  }
  
  //TODO: colocar os outros acordes
  static ALL:any[] = [Chord.C, Chord.D];
}

@Injectable()
export class NoteService {
  
  static ALL_SOL_FA:Note[] = ClaveSol.ALL.concat(ClaveFa.ALL);
  
  constructor() {}
  
  getNoteFromRawMidi(rawMidiInput:string):any[] {
    var midiinput = JSON.parse(rawMidiInput);
    var on = midiinput.on;
    var rawNote = midiinput.note;
    return [Note.valueOf(rawNote), on];
  }
  
}