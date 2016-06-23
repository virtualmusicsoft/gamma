import {Page, NavController, NavParams, Alert} from 'ionic-angular';
import {GamePage} from '../game/game';
import {NoteLevel} from '../../providers/note-service/note-service';

@Page({
  templateUrl: 'build/pages/choose-game/choose-game.html'
})
export class ChooseGamePage {
  public sol:boolean = true;
  public fa:boolean = false;
  public solfa:boolean = false;
  
  public 
  
  private delayShowAlertScore:boolean = false;
  private delayScore:any;

  constructor(private nav: NavController,
              private navParams: NavParams) {
                
    
  }
  
  onPageDidEnter() {
    if (this.delayShowAlertScore) {
      this.showAlertScore();
    }
    this.delayShowAlertScore = false;
  }

  goToGame(levelRaw:String) {
    var p_level:NoteLevel = NoteLevel.valueOf(levelRaw);
    
    new Promise((resolve, reject) => {
      this.nav.push(GamePage, {resolve: resolve, level:p_level});
    }).then(score => {
      this.delayScore = score;
      this.delayShowAlertScore = true;
    });
  }
  
  showAlertScore() {
    let alert = Alert.create({
      title: 'Pontuação',
      subTitle: 'Sua pontuação: ' +this.delayScore,
      buttons: [{text: 'OK' }]
      
    });
    this.nav.present(alert);
  }
}