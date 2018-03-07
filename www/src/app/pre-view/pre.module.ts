import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {PreViewComponent} from "./pre-view.component";

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    PreViewComponent
  ],
  exports:[
    PreViewComponent
  ]
})
export class PreModule { }
