import {Page, NavController} from 'ionic-angular';
import {ChooseGamePage} from '../choose-game/choose-game';
import {ChooseChordPage} from '../choose-chord/choose-chord';
import {MidiInputService} from '../../providers/midiinput-service/midiinput-service';
import {ConnectionListerner} from '../../providers/midiinput-service/midiinput-service';

@Page({
  templateUrl: 'build/pages/connect-midiinput/connect-midiinput.html'
})
export class ConnectMidiInputPage implements ConnectionListerner {

  constructor(private nav: NavController,
              public midiInput: MidiInputService) {
      midiInput.setConnectionListerner(this);
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