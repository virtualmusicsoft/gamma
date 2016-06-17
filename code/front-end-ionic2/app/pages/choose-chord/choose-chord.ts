import {Page, NavController, NavParams, Alert} from 'ionic-angular';
import {GameChordPage} from '../game-chord/game-chord';

@Page({
  templateUrl: 'build/pages/choose-chord/choose-chord.html'
})
export class ChooseChordPage {


  constructor(private nav: NavController,
              private navParams: NavParams) {
                
    
  }
  
  onPageDidEnter() {
    
  }

  goToGame() {
    //TODO: passar parâmetros se for o caso
    this.nav.push(GameChordPage);
  }
}