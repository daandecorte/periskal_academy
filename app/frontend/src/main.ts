import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { FaIconLibrary, FaConfig } from '@fortawesome/angular-fontawesome';
import { dom } from '@fortawesome/fontawesome-svg-core';

// Allow Font Awesome to use CSS for styling
dom.watch();

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
