import {Component} from '@angular/core';
import {NgForm} from '@angular/forms';
import {BsModalRef} from "ngx-bootstrap";
import {ProjectService} from "../project.service";
import {DocsProject} from "../model/docs-project";

@Component({
  templateUrl : './up-project.component.html'
})

export class UpProjectComponent{

   constructor(private bsModalRef: BsModalRef,private projectService:ProjectService){}

   project:DocsProject = new DocsProject();

   upProduct(form:NgForm){
     this.projectService.updateProject(this.project.id,form.value).then();
     this.bsModalRef.hide();
   }
}
