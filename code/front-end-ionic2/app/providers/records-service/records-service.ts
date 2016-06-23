import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import 'rxjs/add/operator/map';
import {App, Platform, Storage, SqlStorage} from 'ionic-angular';

/*
  Generated class for the RecordsService provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
@Injectable()
export class RecordsService {
  public storage: Storage = null;
  public score;

  constructor(public platform: Platform) {
        this.platform.ready()
          .then(() => {
            this.storage = new Storage(SqlStorage);
            /*this.dummyData();
            this.load();*/
          });
        
  }
  
  /*dummyData() {
    var record = [{'score': 51}];
    this.storage.setJson('note-fa', record);
  }

  load() {
    this.storage
        .get('note-fa')
        .then((data) => {
          if (data != null)
          var local = JSON.parse(data); 
            this.score = local[0].score;
        });
  }*/
  
    
  tryAddNewRecord(score, game_label, params?: any) {
    this.storage
        .get(game_label)
        .then((data) => {
          var newScore = {'score': score};
          var myData: any[];
          var isRecord = true;
          if (data == null) {
            myData = [newScore];
          } else {
            myData = JSON.parse(data);
            myData.push(newScore);
            var sortedArray: any[] = myData.sort((n1,n2) => n2.score - n1.score);
            if (sortedArray.length > 5) {
              if (sortedArray[sortedArray.length-1] == newScore) {
                isRecord = false;
              }
              sortedArray = sortedArray.slice(0, sortedArray.length-1);
              myData = sortedArray;
            }
          }
          this.storage.setJson(game_label, myData);
          //TODO: dizer se se trata de novo record
          params.resolve([myData, isRecord, score]);
        });
  }
  
}

