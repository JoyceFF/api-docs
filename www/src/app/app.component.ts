import {Component, ViewEncapsulation} from '@angular/core';
import {MaxwonResource} from "./resource_list/model/resource";
import { Location } from '@angular/common';
import { Router} from "@angular/router";
import {alertError, alerts} from "./shared/app.common";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent{

  constructor( private router: Router, private location: Location){

  }

  alerts = alerts;

  selected: string;
  statesComplex: MaxwonResource[];
  // typeAheadLoading(e: boolean): void {
  //   this.statesComplex = this.docsService.resource;
  // }
  // typeAheadOnSelect(e: TypeaheadMatch){
  //   this.router.navigate(['/generateMarkdownComponent',e.item.id]);
  // }

}
