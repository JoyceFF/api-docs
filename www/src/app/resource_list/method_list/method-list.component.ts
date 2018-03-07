import {MarkdownPreviewComponent} from "./markdown_preview/markdown-preview.component";
import {ExampleHeadersComponent} from "./example-headers/example-headers.component";
import {ExampleParamsComponent} from "./example-params/example-params.component";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {Method} from "../model/method";
import {MethodDetail} from "../model/method-detail";
import {BsModalService} from 'ngx-bootstrap/modal';
import {MaxwonParam} from "../model/param";
import {Component, OnInit} from '@angular/core';
import {DocsService} from "../../docs.service";
import {BsModalRef} from "ngx-bootstrap";
import {NgForm} from "@angular/forms";

import 'rxjs/add/operator/switchMap';
import {MarkdownGenerateComponent} from "./markdown_generate/markdown-generate.component";
import {GetAllMethodProgressComponent} from "./get_all_method_progress/get-all-method-progress.component";
import {SelectModelComponent} from "./select_model/select-model.component";
import {DocsModel} from "../../model-view/model/docs-model";

@Component({
  selector: 'method-list',
  templateUrl: './method-list.component.html',
  styleUrls: ['./method-list.component.css']
})

export class MethodListComponent implements OnInit {
  methodDetailAll: Map<string, MethodDetail> = new Map();
  selectedMethods: Map<string, boolean> = new Map();
  exampleHeaders: Map<string, string>;
  exampleParams: Map<string, string>;
  selectedMethod: Method;
  methodDetail: MethodDetail;
  methods: Method[] = [];
  paramsNames: string[];
  headersNames: string[];
  addParamStatus: boolean;
  bsModalRef: BsModalRef;

  constructor(private docsService: DocsService,
              private route: ActivatedRoute,
              private modalService: BsModalService) {
  }

  ngOnInit(): void {
    let uuid = ""

    this.route.paramMap
      .switchMap((params: ParamMap) => {
        uuid = this.docsService.uuid();
        this.openGetAllMethodDetailProgress(uuid);
        return this.docsService.getAllMethodDetail(params.get('id'), uuid)
      }).subscribe(result => {

      this.methods = result["methods"] as Method[];
      for (let obj of this.methods) {
        this.selectedMethods[obj.id] = false;
      }

      let methodDetails = result["methodDetails"] as MethodDetail[];
      for (let obj of methodDetails) {
        this.methodDetailAll[obj.id] = obj;
      }
    });

    this.handleModalEvent();
  }

  onSelectedMethod(id: string): void {
    if (this.selectedMethods[id] == true) {
      this.selectedMethods[id] = false;
    } else {
      this.selectedMethods[id] = true;
    }
  }

  onClearSelectedMethod() {
    for (let obj in this.selectedMethods) {
      this.selectedMethods[obj] = false;
    }
  }

  onCheckedAllSelectedMethod() {
    for (let obj in this.selectedMethods) {
      this.selectedMethods[obj] = true;
    }
  }

  getCheckedAllSelectedMethod() {
    let methodDetails = [];
    for (let obj in this.selectedMethods) {
      let methodDetail = this.methodDetailAll[obj];
      if (methodDetail != null && this.selectedMethods[obj]) {

        methodDetails.push(methodDetail);
      }
    }
    return methodDetails;
  }

  onSelectMethod(method: Method): void {
    this.selectedMethod = method;
    this.paramsNames = [];
    this.headersNames = [];
    this.exampleParams = null;
    this.exampleHeaders = null;

    this.methodDetail = this.methodDetailAll[method.id];
  }

  loadRequest() {
    this.docsService.getRequest(this.methodDetail.url, this.methodDetail.http, this.methodDetail.permission, this.exampleHeaders, this.exampleParams, this.methodDetail.params)
      .then(methodDetail => {
        this.methodDetail.java = methodDetail.java;
        this.methodDetail.shell = methodDetail.shell;
        this.methodDetail.result = methodDetail.result;
        this.methodDetail.javascript = methodDetail.javascript;
        this.methodDetailAll.get(this.selectedMethod.id).java = methodDetail.java;
        this.methodDetailAll.get(this.selectedMethod.id).shell = methodDetail.shell;
        this.methodDetailAll.get(this.selectedMethod.id).result = methodDetail.result;
        this.methodDetailAll.get(this.selectedMethod.id).javascript = methodDetail.javascript;
      });
  }

  handleModalEvent() {
    this.modalService.onHide.subscribe((reason: string) => {

      if (this.bsModalRef != null) {
        //params
        let pns = [];
        for (let obj in this.bsModalRef.content.exampleParams) {
          pns.push(obj);
        }
        if (pns.length > 0) {
          this.paramsNames = pns;
          this.exampleParams = this.bsModalRef.content.exampleParams;
          this.loadRequest();
        }

        //headers
        let hns = [];
        for (let obj in this.bsModalRef.content.exampleHeaders) {
          hns.push(obj);
        }

        if (hns.length > 0) {
          this.headersNames = hns;
          this.exampleHeaders = this.bsModalRef.content.exampleHeaders;
          this.loadRequest();
        }

        if (this.bsModalRef.content.selectModel != null){
          this.analysisParamDescribe(this.bsModalRef.content.selectModel,this.bsModalRef.content.replaceAll);
        }


      }
    })
  }

  openGetAllMethodDetailProgress(uuid: string) {
    this.bsModalRef = this.modalService.show(GetAllMethodProgressComponent, {
      ignoreBackdropClick: true,
      keyboard: false
    });
    this.bsModalRef.content.uuid = uuid;
  }

  openParamsModal() {
    this.bsModalRef = this.modalService.show(ExampleParamsComponent);
    this.bsModalRef.content.params = this.methodDetail.params;
  }

  openHeadersModal() {
    this.bsModalRef = this.modalService.show(ExampleHeadersComponent);
    this.bsModalRef.content.permission = this.methodDetail.permission;
  }

  openSelectModel(){
    this.bsModalRef = this.modalService.show(SelectModelComponent);
  }

  openMarkdownPreview() {
    this.bsModalRef = this.modalService.show(MarkdownPreviewComponent, {class: 'modal-lg'});

    let methodDetails = this.getCheckedAllSelectedMethod();
    this.docsService.getMarkdown(methodDetails).then(markdown => {

      this.bsModalRef.content.markdown = markdown;
    });
  }

  openMarkdownGenerate() {
    this.bsModalRef = this.modalService.show(MarkdownGenerateComponent, {class: 'modal-lg'});
    let methodDetails = this.getCheckedAllSelectedMethod();
    this.docsService.generate(methodDetails).then(fileName => {

      this.bsModalRef.content.fileName = fileName;
    });
  }

  getDefaultHeader(permission: string) {
    this.docsService.getDefaultHeader(permission)
      .then(defaultHeader => {
        let hns = [];
        for (let obj in defaultHeader) {
          hns.push(obj);
        }

        if (hns.length > 0) {
          this.headersNames = hns;
          this.exampleHeaders = defaultHeader;
        }
      });
  }

  deleteParam(name: string) {
    let index = 0;
    for (let obj of this.methodDetail.params) {
      if (obj.name == name) {
        this.methodDetail.params.splice(index, 1);
        break;
      }
      index++
    }
  }

  addParam() {
    this.addParamStatus = true;
  }

  closeParam() {
    this.addParamStatus = false;
  }

  submitParam(form: NgForm) {
    if (form.valid) {
      let formValue = form.value;
      let p: MaxwonParam = new MaxwonParam();
      p.name = formValue["name"];
      p.type = formValue["type"];
      p.describe = formValue["describe"];
      p.required = formValue["required"];
      //p.paramType = formValue["paramType"];
      p.defaultValue = formValue["defaultValue"];

      this.methodDetail.params.push(p);

      this.closeParam();
    } else {
      for (let obj in form.controls) {
        form.controls[obj].markAsTouched();
      }
    }
  }

  analysisParamDescribe(model:DocsModel,replaceAll:boolean){

    this.docsService.getModelDetails(model.id).then(modelDetails => {
      for (let obj of this.methodDetail.params) {
        if (!replaceAll){
          if (obj.describe !=null && obj.describe.trim() != "" ){
            continue;
          }
        }

        for (let obj1 of modelDetails) {
            if (obj.name == obj1.name){
              obj.describe = obj1.describe;
            }
        }
      }
    });

  }
}
