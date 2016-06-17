import {App, Platform, Storage, SqlStorage} from 'ionic-angular';
import {StatusBar} from 'ionic-native';
import {HomePage} from './pages/home/home';
import {ConnectMidiInputPage} from './pages/connect-midiinput/connect-midiinput';
import {MidiInputService} from './providers/midiinput-service/midiinput-service';
import {RecordsService} from './providers/records-service/records-service';


@App({
  template: '<ion-nav [root]="rootPage"></ion-nav>',
  config: {}, // http://ionicframework.com/docs/v2/api/config/Config/,
  providers: [MidiInputService, RecordsService]
})
export class MyApp {
  //rootPage: any = HomePage;
  rootPage: any = ConnectMidiInputPage;

  constructor(platform: Platform) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      StatusBar.styleDefault();
    });
  }
}
