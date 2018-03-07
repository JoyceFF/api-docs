import {Component, OnInit} from '@angular/core';
import {MaxwonParam} from "../../model/param";
import {NgForm} from '@angular/forms';
import {BsModalRef} from "ngx-bootstrap";

@Component({
  //selector: 'modal-content',
  templateUrl : './example-headers.component.html'
})

export class ExampleHeadersComponent{

   permission: string;
   exampleHeaders:any;

   constructor(private bsModalRef: BsModalRef){}

   saveHeaders(form:NgForm){
     this.bsModalRef.hide();
     this.exampleHeaders = form.value;
   }
}
