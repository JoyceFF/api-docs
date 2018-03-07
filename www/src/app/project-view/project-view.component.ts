import {Component, OnInit} from "@angular/core";
import {DocsProject} from "./model/docs-project";
import {ProjectService} from "./project.service";
import {BsModalRef, BsModalService} from "ngx-bootstrap";
import {AddProjectComponent} from "./add-project-view/add-project.component";
import {UpProjectComponent} from "./up-project-view/up-project.component";
//import {BsModalService} from "ngx-bootstrap";
//import {AddProjectComponent} from "./add-project-view/add-project.component";

@Component({
  selector: 'project-view',
  templateUrl : './project-view.component.html',
  styleUrls:[ './project-view.component.css' ]
})

export class ProjectViewComponent implements OnInit{
  ngOnInit(): void {
    this.loadAllProjects();
    this.handleModalEvent();
  }

  allProjects:DocsProject[];
  bsModalRef:BsModalRef;

  constructor(private projectService:ProjectService,private modalService:BsModalService){}

  loadAllProjects(){
    this.projectService.findProject()
      .then(projects => this.allProjects = projects);
  }

  openAddProjectModal(){
     this.bsModalRef = this.modalService.show(AddProjectComponent);
  }

  openUpProjectModal(project:DocsProject){
    this.bsModalRef = this.modalService.show(UpProjectComponent);
    this.bsModalRef.content.project = project;
  }

  delProject(id:string){
    this.projectService.delProject(id).then(() => { this.loadAllProjects();});
  }

  handleModalEvent() {
    this.modalService.onHide.subscribe((reason: string) => {
      if (this.bsModalRef != null) {
         this.loadAllProjects();
      }
    })
  }
}
