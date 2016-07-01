import {Page, NavController, NavParams, ViewController} from 'ionic-angular';
import {ChooseGamePage} from '../choose-game/choose-game';
import {RecordsService} from '../../providers/records-service/records-service';


/*
  Generated class for the ResultPage page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Page({
  templateUrl: 'build/pages/result/result.html',
})
export class ResultPage {
  public records;
  public isRecord : boolean = false; 
  constructor(public nav: NavController,
              public navParams: NavParams,
              public viewCtrl: ViewController,
              public recordsService: RecordsService) {
    
  }
  
  onPageLoaded() {
    this.viewCtrl.showBackButton(false);
  }
  
  onPageWillEnter() {
    new Promise((resolve, reject) => {
      this.recordsService.tryAddNewRecord(this.navParams.get("score"), "note-fa-n1", {resolve: resolve});
    }).then(data => {
      this.records =  data[0];
      this.isRecord = data[1];
    });
  }
  
  onPageDidEnter() {
    //TODO: informar que se trata de um novo record!
  }
  
              
  getScore() {
    return this.navParams.get("score");
  }
  
  goToChooseGame() {
    this.nav.setRoot(ResultPage);
    this.nav.push(ChooseGamePage);
  }
  
}
