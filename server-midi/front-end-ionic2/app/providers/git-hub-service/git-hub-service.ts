import {Injectable} from '@angular/core';
import {Http, Headers} from '@angular/http';
import 'rxjs/add/operator/map';

@Injectable()
export class GitHubService {
  constructor(private http: Http) {
  }

  getRepos(username) {
    let repos = this.http.get(`https://api.github.com/users/${username}/repos`);
    return repos;
  }

  getDetails(repo) {
    let headers = new Headers();
    headers.append('Accept','application/vnd.github.VERSION.html');

    return this.http.get(`${repo.url}/readme`, { headers: headers });
  }
}

/*
  Generated class for the GitHubService provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.

@Injectable()
export class GitHubService {
  data: any = null;

  constructor(public http: Http) {}

  load() {
    if (this.data) {
      // already loaded data
      return Promise.resolve(this.data);
    }

    // don't have the data yet
    return new Promise(resolve => {
      // We're using Angular Http provider to request the data,
      // then on the response it'll map the JSON data to a parsed JS object.
      // Next we process the data and resolve the promise with the new data.
      this.http.get('path/to/data.json')
        .map(res => res.json())
        .subscribe(data => {
          // we've got back the raw data, now generate the core schedule data
          // and save the data for later reference
          this.data = data;
          resolve(this.data);
        });
    });
  }
}
*/