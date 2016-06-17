/// <reference path="../../../node_modules/retyped-sockjs-client-tsd-ambient/sockjs-client.d.ts" />
/// <reference path="../../../typings/modules/stomp-websocket/stomp-websocket.d.ts" />
import {Page, NavController, NavParams, Alert} from 'ionic-angular';
import {NoteService, Note, NoteHtml, ClaveFa, ClaveSol, Chord, BasicNote} from '../../providers/note-service/note-service';
import {MidiInputService, HandleMidiInputListerner} from '../../providers/midiinput-service/midiinput-service';
import {ResultPage} from '../result/result';
import {Observable} from 'rxjs/Rx'
import * as SockJS from 'sockjs-client';
import BaseEvent = __SockJSClient.BaseEvent;
import SockJSClass = __SockJSClient.SockJSClass;

@Page({
  templateUrl: 'build/pages/game-chord/game-chord.html',
  providers: [NoteService],
  selector: 'game'
})
export class GameChordPage implements HandleMidiInputListerner {

  
  public inputNotes:BasicNote[] = [];
  public gameChord:Chord = null;
  public score:number = 0;
  public isGameRunning:boolean = true;
  
  public timeRemaining:number = 0;
  public timeTotal:number = 0;
  public incremento:number = 0;
  public updateProgressBySecond:number = 5;
  public intervalValue:number = 1000 / this.updateProgressBySecond;
  public secondsToResponse:number = 10; //TODO: personalizar via localstorange
  
  private timer;  
  private countTotalNotes:number = 0;
  private lastChord:Chord;
  
  private maxChallenges:number = 2;
  private counterChallenges:number = 0;
  
  
  constructor(private nav: NavController, 
              private noteService: NoteService,
              private navParams: NavParams,
              public midiInput: MidiInputService) {
  }
  
  //TODO: fazer o mesmo no game (=game-note)
  onPageDidEnter() {
    this.midiInput.setHandleMidiInputListerner(this);
  }
  
  onConnection() {}
  
  getNextChord() {
    var temp:Chord;
    do {
       temp = this.getPreviewNextChord();
    } while(temp == this.lastChord);
    this.lastChord = temp;
    return temp;
  }
  
  getPreviewNextChord() {
    var index = Math.floor(Math.random() * Chord.ALL.length);
    return Chord.ALL[index];
  }
  
  handleMidiInput(rawMidiInput:string):void {
    //TODO: verificar se o game está em execução!
    var midiinput = this.noteService.getNoteFromRawMidi(rawMidiInput);
    var note = midiinput[0]
    var keyOn = midiinput[1];
    if (keyOn) {
      if (note.info != null) {
        this.computeNewScore(note);
      }else{
        //TODO: informar q não processa aquele tipo de nota
      }
    }
  }
  
  //TODO: reemplementar para refletir a realidade do game chord
  //TODO: ideia: gameNotes será somente gameNotes[0] 
  match():boolean {
    if (this.gameChord.getNotes().length != this.inputNotes.length) {
      return false;
    }    
     
    for(let gameNote of this.gameChord.getNotes()) {
      if (this.inputNotes.indexOf(gameNote) == -1) {
        return false;
      }
    }
    return true;
  }
  
  computeNewScore(note?:BasicNote) {
    if (this.isGameRunning) {
      if (note != null) {
        this.inputNotes.push(note);
        if (this.inputNotes.length < this.gameChord.getNotes().length) {
          return;
        }
      }
      
      this.isGameRunning = false;
      
      if (this.gameChord != null) {
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
      
      this.gameChord = this.getNextChord();
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
