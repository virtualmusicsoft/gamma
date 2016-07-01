/// <reference path="../../../node_modules/retyped-sockjs-client-tsd-ambient/sockjs-client.d.ts" />
/// <reference path="../../../typings/modules/stomp-websocket/stomp-websocket.d.ts" />
import {Page, NavController, NavParams, Alert} from 'ionic-angular';
import {NoteService, Note, NoteHtml, ClaveFa, ClaveSol, NoteLevel} from '../../providers/note-service/note-service';
import {MidiInputService, HandleMidiInputListerner} from '../../providers/midiinput-service/midiinput-service';
import {ResultPage} from '../result/result';
import {Observable} from 'rxjs/Rx'
import * as SockJS from 'sockjs-client';
import BaseEvent = __SockJSClient.BaseEvent;
import SockJSClass = __SockJSClient.SockJSClass;

@Page({
  templateUrl: 'build/pages/game/game.html',
  providers: [NoteService],
  selector: 'game'
})
export class GamePage implements HandleMidiInputListerner {

  
  public inputNotes:Note[] = [];
  public gameNotes:Note[] = [];
  public score:number = 0;
  public isGameRunning:boolean = true;
  
  public timeRemaining:number = 0;
  public timeTotal:number = 0;
  public incremento:number = 0;
  public updateProgressBySecond:number = 5;
  public intervalValue:number = 1000 / this.updateProgressBySecond;
  public secondsToResponse:number = 5; //TODO: personalizar via localstorange
  
  private timer;
  private lastNote:Note;
  private level:NoteLevel;
  
  private maxChallenges:number = 3;
  private counterChallenges:number = 0;
  
  private randomNotes:Note[] = [];
  
  
  constructor(private nav: NavController, 
              private noteService: NoteService,
              private navParams: NavParams,
              public midiInput: MidiInputService) {
                
    //TODO: BUG? no construtor vai iniciar só uma vez? Parece que não.
    this.level = navParams.get('level');
    
    this.dummyNotas();
    
    //TODO: cuidado deve ficar em um 
    if (this.level.sol) {
      this.randomNotes = this.randomNotes.concat(ClaveSol.ALL_BASIC_NOTES);
      if (this.level.semiNote) {
        this.randomNotes = this.randomNotes.concat(ClaveSol.ALL_BASIC_SEMI_NOTES);
      }
    }
    if (this.level.fa) {
      this.randomNotes = this.randomNotes.concat(ClaveFa.ALL_BASIC_NOTES);
      if (this.level.semiNote) {
        this.randomNotes = this.randomNotes.concat(ClaveFa.ALL_BASIC_SEMI_NOTES);
      }
    }    
    
    if (this.randomNotes.length <= 1 ) {
      throw new Error("Error: this.randomNotes.length <= 0!");
    }
    
    midiInput.setHandleMidiInputListerner(this);
  }
  
  onConnection() {}
  
  /**
   * Útil para renderizar notas
   */
  dummyNotas() {
    /*this.gameNotes.push(ClaveSol.CSharp);
    this.gameNotes.push(ClaveSol.DSharp);
    this.gameNotes.push(ClaveSol.FSharp);
    this.gameNotes.push(ClaveSol.GSharp);*/
  }
  
  getNextNote() {
    var temp:Note;
    do {
       temp = this.getPreviewNextNote();
    } while(temp == this.lastNote);
    this.lastNote = temp;
    return temp;
  }
  
  getPreviewNextNote() {
    var index = Math.floor(Math.random() * this.randomNotes.length);
    return this.randomNotes[index];
  }
  
  handleMidiInput(rawMidiInput:string):void {
    //TODO: verificar se o game está em execução!
    var midiinput = this.noteService.getNoteFromRawMidi(rawMidiInput);
    var note = midiinput[0];
    var keyOn = midiinput[1];
    if (keyOn) {
      if (note.info != null) {
        this.computeNewScore(note);
      }
    }
  }
  
  match():boolean {
    //TODO: distinguir "Do" da Clave sol com a clave de "Fá"
    if (this.gameNotes.length != this.inputNotes.length) {
      return false;
    }    
     
    for(let gameNote of this.gameNotes) {
      if (this.inputNotes.indexOf(gameNote) == -1) {
        return false;
      }
    }
    return true;
  }
  
  computeNewScore(note?:Note) {
    if (this.isGameRunning) {
      this.isGameRunning = false;
      
      if (note != null) {
        this.inputNotes.push(note);
      }
      if (this.gameNotes.length != 0) {
        let match = this.match();
        //TODO: valor do score deveria ser de acordo com o tempo gasto para respondere o tempo q teve para responder
        if (match) {
          this.score += 10;
        } else {
          this.score -= 10;
        }
      }
      
      if (this.counterChallenges >= this.maxChallenges) {
        this.stop();
        return;
      }
      
      this.gameNotes = [];
      this.gameNotes.push(this.getNextNote());
      this.inputNotes = [];
      this.counterChallenges++;
      this.reset();
    }
  }
  
  start() {
    this.initTimer();
  }
  
  reset() {
    if (this.timer != null) {
      this.timer.unsubscribe();
    }
    this.initTimer();
  }
  
  stop() {
    this.isGameRunning = false;
    if (this.timer != null) {
      this.timer.unsubscribe();
    }
    this.cleanTimer();
    this.goToResult();
  }
  
  goToResult() {
    this.nav.push(ResultPage, {score:this.score});    
  }
  
  cleanTimer() {
    this.timeTotal = this.intervalValue * this.updateProgressBySecond * this.secondsToResponse;
    this.timeRemaining = this.timeTotal;
    this.incremento = 0;
  }
  
  initTimer() {
    this.cleanTimer();    
    
    //TODO: this.incremento parece que não está servindo para NADA!
    this.timer = Observable
                          .interval(this.intervalValue)
                          .takeWhile(() => true)
                          .map((x) => this.incremento+this.intervalValue)
                          .subscribe((x) => {
                            this.incremento += this.intervalValue; 
                            this.timeRemaining -= this.intervalValue;
                            if (this.timeRemaining <= 0) {
                              this.incremento = 0;
                              this.timeRemaining = this.timeTotal;
                              this.computeNewScore();
                            }
                          });
    this.isGameRunning = true;      
  }
  
}
