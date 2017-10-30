import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {WatsonApp} from './watson-app';

@NgModule({
  declarations: [
    WatsonApp
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [
    WatsonApp
  ]
})
export class AppModule {
}
