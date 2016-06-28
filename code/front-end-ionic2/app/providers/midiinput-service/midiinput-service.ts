import {Injectable} from '@angular/core';
import * as SockJS from 'sockjs-client';
import BaseEvent = __SockJSClient.BaseEvent;
import SockJSClass = __SockJSClient.SockJSClass;

export interface HandleMidiInputListerner {
  handleMidiInput(message:string):void;
  
}

export interface ConnectionListerner {
  onConnection():void;
  onClose():void;
}

@Injectable()
export class MidiInputService {
  
  public host:String = "localhost:8080";
  public status:String = "Disconnected";
  public count:number = -1;
  
  private client:SockJSClass;
  private mystomp:StompClient;
  
  private handleMidiInputListerner:HandleMidiInputListerner;
  private connectionListerner:ConnectionListerner;
  
  constructor() {
    this.count++;
  }
  
  reconnect() {
    //TODO: descomentar
    ///setTimeout($scope.initSockets, 10000);
  };
  
  setHandleMidiInputListerner(handleMidiInputListerner:HandleMidiInputListerner) {
    this.handleMidiInputListerner = handleMidiInputListerner;
  }
  
  setConnectionListerner(connectionListerner:ConnectionListerner) {
    this.connectionListerner = connectionListerner;
  }
  
  connect() {
    if (this.client == null) {
      this.status = "Connecting...";
      
      this.client = new SockJS('http://' + this.host + '/note');
      this.mystomp = Stomp.over(this.client);
      
      this.mystomp.connect({}, (frame) => {
        this.count++;
        this.status = "Connected";
        this.notifyConnectionListerner();        
        this.mystomp.subscribe("/topic/midiinput", (message:StompFrame) => {
          this.notifyHandleMidiInputListerner(message.body);
        });
      });
      this.client.onclose = this.reconnect;
    }
  }
  
  notifyHandleMidiInputListerner(raw:string) {
    if (this.handleMidiInputListerner != null) {
      this.handleMidiInputListerner.handleMidiInput(raw);
    }
  }
  
  notifyConnectionListerner() {
    if (this.connectionListerner != null) {
      this.connectionListerner.onConnection();
    }
  }
  
}