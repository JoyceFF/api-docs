import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {JsonViewComponent} from "./json-view.component";

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [JsonViewComponent],
  exports:[JsonViewComponent]
})
export class JsonModule { }
