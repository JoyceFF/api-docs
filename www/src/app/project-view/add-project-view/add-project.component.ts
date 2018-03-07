import {Component} from '@angular/core';
import {NgForm} from '@angular/forms';
import {BsModalRef} from "ngx-bootstrap";
import {ProjectService} from "../project.service";
import {DocsProject} from "../model/docs-project";

@Component({
  templateUrl : './add-project.component.html'
})

export class AddProjectComponent{

   constructor(private bsModalRef: BsModalRef,private projectService:ProjectService){}

   saveProduct(form:NgForm){
     let docsProject:DocsProject  = new DocsProject();
     docsProject.name = form.value["name"];
     docsProject.describe = form.value["describe"];
     this.projectService.addProject(docsProject).then();
     this.bsModalRef.hide();
   }
}
