import {Page, NavController} from 'ionic-angular';
import {ChooseGamePage} from '../choose-game/choose-game';
import {ChooseChordPage} from '../choose-chord/choose-chord';
import {MidiInputService} from '../../providers/midiinput-service/midiinput-service';
import {ConnectionListerner} from '../../providers/midiinput-service/midiinput-service';
import {Router} from '@angular/router';

@Page({
  templateUrl: 'build/pages/connect-midiinput/connect-midiinput.html'
})
export class ConnectMidiInputPage implements ConnectionListerner {

  constructor(private nav: NavController,
              public midiInput: MidiInputService,
              private router:Router) {
      midiInput.setConnectionListerner(this);
      midiInput.host = window.location.host; 
  }
  
  startConnection() {
    this.midiInput.connect();
  }
  
  onConnection() {
    //this.goToChooseChordGame();
    this. goToChooseGame();
  }
  
  onClose() {}
  
  goToChooseChordGame() {
    this.nav.push(ChooseChordPage);
  }
  
  goToChooseGame() {
    this.nav.push(ChooseGamePage);
  }
}