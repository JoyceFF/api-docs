import {Component, OnInit} from "@angular/core";
import {BsModalRef, BsModalService} from "ngx-bootstrap";
import {ApiFolderService} from "./api-folder.service";
import {DocsApiFolder} from "./model/docs-api-folder";
import {ActivatedRoute, ParamMap} from "@angular/router";

import 'rxjs/add/operator/switchMap';
import {AddApiFolderComponent} from "./add-api-folder/add-api-folder.component";
import {UpApiFolderComponent} from "./up-api-folder/up-api-folder.component";


@Component({
  styleUrls:['./api-folder.component.css'],
  templateUrl : './api-folder.component.html'
})

export class ApiFolderComponent implements OnInit{
  ngOnInit(): void {
    this.route.paramMap
      .switchMap((params: ParamMap) => {
        return params.getAll('projectId');
     }).subscribe((projectId)=>{
      this.projectId = projectId;
      this.loadApiFolder();
    });

    this.handleModalEvent();
  }

  allApiFolders:DocsApiFolder[];
  bsModalRef:BsModalRef;
  projectId:string;

  constructor(private route: ActivatedRoute,private modalService:BsModalService,private apiFolderService:ApiFolderService){}

  loadApiFolder(){
     this.apiFolderService.findApiFolder(this.projectId).then(apiFolder => this.allApiFolders = apiFolder);
  }

  delApiFolder(id:string){
    this.apiFolderService.delApiFolder(id).then(()=>this.loadApiFolder());
  }

  openAddApiFolder(){
    this.bsModalRef = this.modalService.show(AddApiFolderComponent);
    this.bsModalRef.content.projectId = this.projectId;
  }

  openUpApiFolder(apiFolder:DocsApiFolder){
    this.bsModalRef = this.modalService.show(UpApiFolderComponent);
    this.bsModalRef.content.apiFolder = apiFolder;
  }

  handleModalEvent() {
    this.modalService.onHide.subscribe((reason: string) => {
      if (this.bsModalRef != null) {
        this.loadApiFolder();
      }
    })
  }
}
