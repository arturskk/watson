import {enableProdMode} from '@angular/core';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import 'rxjs-imports';
import {WatsonAppModule} from './app/watson-app.module';

import {environment} from './environments/environment';

if (environment.production) {
  enableProdMode();
}

platformBrowserDynamic()
  .bootstrapModule(WatsonAppModule)
  .catch(err => console.log(err));
