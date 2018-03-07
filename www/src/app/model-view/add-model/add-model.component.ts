import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {BsModalRef} from "ngx-bootstrap";
import {ModelService} from "../model.service";
import {DocsModel} from "../model/docs-model";

@Component({
  selector: 'modal-content',
  templateUrl : './add-model.component.html'
})

export class AddModelComponent{

   constructor(private bsModalRef: BsModalRef,private modelService:ModelService){}

   saveModel(form:NgForm){
     let model:DocsModel=new DocsModel();
     model.name = form.value["name"];
     model.describe = form.value["describe"];
     this.modelService.addModel(model).then();
     this.bsModalRef.hide();
   }
}
