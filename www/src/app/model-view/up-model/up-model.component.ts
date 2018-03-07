import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {BsModalRef} from "ngx-bootstrap";
import {ModelService} from "../model.service";
import {DocsModel} from "../model/docs-model";

@Component({
  selector: 'modal-content',
  templateUrl : './up-model.component.html'
})

export class UpModelComponent{

   model:DocsModel = new DocsModel();

   constructor(private bsModalRef: BsModalRef,private modelService:ModelService){}

   saveModel(form:NgForm){
     this.modelService.upModel(this.model.id,form.value).then();
     this.bsModalRef.hide();
   }
}
