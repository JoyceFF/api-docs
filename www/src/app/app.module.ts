import { BrowserModule } from '@angular/platform-browser';
import {AppRoutingModule} from "./app-routing.module";
import { AppComponent } from './app.component';
import { NgModule } from '@angular/core';
import {NotFoundComponent} from "./shared/not-found.component";
import {SelectivePreloadingStrategy} from "./selective-preloading-strategy";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AlertModule} from "ngx-bootstrap";

@NgModule({
  declarations: [
    AppComponent,
    NotFoundComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AlertModule.forRoot(),
    AppRoutingModule,
  ],
  providers: [SelectivePreloadingStrategy],
  bootstrap: [AppComponent],
  entryComponents:[

  ]
})
export class AppModule { }
