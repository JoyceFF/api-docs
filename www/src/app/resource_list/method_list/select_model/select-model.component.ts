import {Component, OnInit} from "@angular/core";
import {BsModalRef, TypeaheadMatch} from "ngx-bootstrap";
import {DocsService} from "../../../docs.service";
import {DocsModel} from "../../../model-view/model/docs-model";

@Component({
  templateUrl : './select-model.component.html'
})
export class SelectModelComponent implements OnInit{
  ngOnInit(): void {
     this.docsService.getAllModel().then(allModel=>this.statesComplex = allModel);
  }

  constructor(private bsModalRef: BsModalRef,private docsService: DocsService){}

  selected: string;
  selectModel:DocsModel;
  statesComplex: DocsModel[];
  replaceAll:Boolean = false;

  replaceAllFun(){
    this.replaceAll = true;
    this.bsModalRef.hide();
  }
  typeAheadOnSelect(e: TypeaheadMatch){
    this.selectModel  = e.item;
  }


}
