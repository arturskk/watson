import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {AppVersion} from './app-version';

@Component({
  selector: 'ws-app-version',
  template: `
    <ng-container *ngIf="version">
      Watson! {{version.build.version}}
      (
      <a href="https://github.com/glipecki/watson/commit/{{version.git.commit.commitId}}">
        #{{version.git.commit.commitIdAbbrev}}@{{version.git.branch}}
      </a>
      )
    </ng-container>
  `
})
export class AppVersionComponent implements OnInit {

  version: AppVersion;

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.httpClient
      .get<AppVersion>('assets/version.json')
      .subscribe(version => this.version = version);
  }

}
