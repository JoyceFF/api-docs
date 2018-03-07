import {Component, OnInit} from '@angular/core';
import {MaxwonParam} from "../../model/param";
import {NgForm} from '@angular/forms';
import {BsModalRef} from "ngx-bootstrap";

@Component({
  //selector: 'modal-content',
  templateUrl : './example-params.component.html'
})

export class ExampleParamsComponent{

   params : MaxwonParam[];
   exampleParams:any;

   constructor(private bsModalRef: BsModalRef){}

   saveParams(form:NgForm){
     this.bsModalRef.hide();
     this.exampleParams = form.value;
   }
}
