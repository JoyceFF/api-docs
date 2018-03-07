import {Component, OnInit} from "@angular/core";
import {BsModalRef, BsModalService} from "ngx-bootstrap";
import {AddModelComponent} from "./add-model/add-model.component";
import {ModelDetailsComponent} from "./model-details/model-details.component";
import {UpModelComponent} from "./up-model/up-model.component";
import {ModelService} from "./model.service";
import {DocsModel} from "./model/docs-model";
import {MarkdownImportDetailsComponent} from "./markdown-import-details/markdown-import-details.component";

@Component({
  selector: 'model-view',
  templateUrl: './model-view.component.html',
  styleUrls: ['./model-view.component.css']
})
export class ModelViewComponent implements OnInit{
  allModel:DocsModel[];
  bsModalRef:BsModalRef;

  ngOnInit(): void {
    this.getAllModel();
    this.handleModalEvent();
  }

  constructor(private modelService:ModelService,private modalService: BsModalService){}

  getAllModel(){
    this.modelService.getAllModel().then(allModel=>this.allModel = allModel);
  }

  openAddModelModal(){
   this.bsModalRef = this.modalService.show(AddModelComponent);
  }

  openUpModelModal(model:DocsModel){
    this.bsModalRef = this.modalService.show(UpModelComponent);
    this.bsModalRef.content.model = model;
  }
  openModelDetails(modelId:string){
    this.bsModalRef = this.modalService.show(ModelDetailsComponent,{class: 'modal-lg'});
    this.bsModalRef.content.modelId = modelId;
    this.modelService.getModelDetailsByModelId(modelId)
      .then(modelDetails =>  this.bsModalRef.content.modelDetails = modelDetails);
  }

  openMarkdownImportDetails(modelId:string){
     this.bsModalRef = this.modalService.show(MarkdownImportDetailsComponent);
     this.bsModalRef.content.modelId = modelId;
  }

  delModel(modelId:string){
    this.modelService.delModel(modelId).then(map =>{
      this.getAllModel();
    });

  }
  handleModalEvent() {
    this.modalService.onHide.subscribe((reason: string) => {

      if (this.bsModalRef != null) {
          this.getAllModel();
      }
    })
  }



}
