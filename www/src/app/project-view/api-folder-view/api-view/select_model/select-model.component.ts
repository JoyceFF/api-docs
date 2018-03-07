import {Component, OnInit} from "@angular/core";
import {BsModalRef, TypeaheadMatch} from "ngx-bootstrap";
import {DocsModel} from "../../../../model-view/model/docs-model";
import {ModelService} from "../../../../model-view/model.service";
import {DocsModelDetails} from "../../../../model-view/model/docs-model-details";
import {DocsHeader} from "../model/docs-header";
import {DocsParam} from "../model/docs-param";

@Component({
  templateUrl: './select-model.component.html'
})
export class SelectModelComponent implements OnInit {
  ngOnInit(): void {
    this.modelService.getAllModel().then(allModel => this.statesComplex = allModel);
  }

  constructor(private bsModalRef: BsModalRef, private modelService: ModelService) {
  }

  items: any;
  checkFiled:string;
  importHeader:boolean = false;
  importParam:boolean = false;
  filed: string;
  selected: string;
  selectModel: DocsModel;
  statesComplex: DocsModel[];
  modelDetails: DocsModelDetails[];

  replaceAllFun(replaceAll: boolean) {
    for (let obj of this.modelDetails) {
      for (let item of this.items) {
        if (item[this.checkFiled] == obj['name']) {
          if (replaceAll) {
            item[this.filed] = obj['describe'];
          } else {
            if (item[this.filed] == null || item[this.filed] == '') {
              item[this.filed] = obj['describe'];
            }
          }

        }
      }
    }
    this.bsModalRef.hide();
  }

  importFun(){
    if (this.importHeader){
      for (let obj of this.modelDetails) {
        let header:DocsHeader  = new DocsHeader();
        header.key = obj.name;
        header.value = obj.defaultValue;
        header.describe = obj.describe;
        this.items.push(header);
      }
    }else if (this.importParam){
      for (let obj of this.modelDetails) {
        let param:DocsParam  = new DocsParam();
        param.name = obj.name;
        param.defaultValue = obj.defaultValue;
        param.type = obj.type;
        param.describe = obj.describe;
        param.required = '是';
        param.level = 1;
        this.items.push(param);
      }
    }

    this.bsModalRef.hide();
  }

  typeAheadOnSelect(e: TypeaheadMatch) {
    this.selectModel = e.item;
    this.modelService.getModelDetailsByModelId(this.selectModel.id).then(docsModelDetails => this.modelDetails = docsModelDetails);
  }
}
