import {Page, NavController, NavParams, Alert} from 'ionic-angular';
import {GamePage} from '../game/game';

@Page({
  templateUrl: 'build/pages/choose-game/choose-game.html'
})
export class ChooseGamePage {
  public sol:boolean = true;
  public fa:boolean = false;
  public solfa:boolean = false;
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

  goToGame() {
    var params;
    var p_sol:boolean = this.sol;
    var p_fa:boolean = this.fa;
    
    if (this.solfa) {
      p_sol, p_fa = true;
    }
    
    new Promise((resolve, reject) => {
      this.nav.push(GamePage, {resolve: resolve, sol:p_sol, fa:p_fa});
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