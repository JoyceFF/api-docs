import {Component, ElementRef, OnInit, Renderer2} from '@angular/core';
import {ActivatedRoute, ParamMap} from "@angular/router";
import {BsModalRef, BsModalService} from "ngx-bootstrap";
import {AddApiComponent} from "./add-api/add-api.component";

import 'rxjs/add/operator/switchMap';
import {ApiService} from "./api.service";
import {DocsApi} from "./model/docs-api";
import {DocsParam} from "./model/docs-param";
import {
  alertError, docsTypeof, findChild, ParamTypeDefaultValueMap, ParamTypeMap,
  sortParams
} from "../../../shared/app.common";
import {AddParamComponent} from "./add-param/add-param.component";
import {DocsPathParam} from "./model/docs-path-param";
import {SaveParamDetailsComponent} from "./save-param-details/save-param-details.component";
import {ParamJsonImportComponent} from "./param-json-import/param-json-import.component";
import {DocsHeader} from "./model/docs-header";
import {AddHeaderComponent} from "./add-header/add-header.component";
import {CurlImportComponent} from "./curl-import/curl-import.component";
import {SelectModelComponent} from "./select_model/select-model.component";
import {PreviewMarkdownComponent} from "./preview-markdown/preview-markdown.component";
import {DocsApiPermission} from "../../../settings-view/model/docs-api-permission";
import {SettingsService} from "../../../settings-view/settings.service";

@Component({
  selector: 'app-method-view',
  templateUrl: './api-view.component.html',
  styleUrls: ['./api-view.component.css']
})
export class ApiViewComponent implements OnInit {

  constructor(private route: ActivatedRoute, private modalService: BsModalService, private apiService: ApiService,private settingsService:SettingsService, private el: ElementRef, private renderer2: Renderer2) {
  }

  ngOnInit() {
    this.route.paramMap
      .switchMap((params: ParamMap) => {
        return params.getAll('id');
      }).subscribe((id)=>{
      this.folderId = id;
      this.loadApi();
    });
    this.loadAllApiPermission();
    this.handleModalEvent();
  }

  bsModalRef: BsModalRef;
  allApi: DocsApi[];
  deletedApis: DocsApi[] = [];
  folderId: string;
  selectedApi: DocsApi;
  checkedApis: Array<DocsApi> = [];
  https: string[] = [
    'GET',
    'POST',
    'PUT',
    'DELETE',
    'OPTIONS',
    'HEAD',
    'TRACE',
    'PATCH',
    'COPY',
    'LINK',
    'UNLINK',
    'PURGE',
    'LOCK',
    'UNLOCK',
    'PROPFIND',
    'VIEW'
  ];
  allApiPermission:DocsApiPermission[];

  loadAllApiPermission(){
    this.settingsService.getAllApiPermission().then(all => this.allApiPermission = all);
  }
  loadApi() {
    this.apiService.findApi(this.folderId).then(allApi => this.allApi = allApi);
  }

  openAddApi() {
    this.bsModalRef = this.modalService.show(AddApiComponent);
    this.bsModalRef.content.folderId = this.folderId;
    this.bsModalRef.content.allApi = this.allApi;
  }

  openAddParam(type: string, parentId?: string) {
    this.bsModalRef = this.modalService.show(AddParamComponent);
    this.bsModalRef.content.api = this.selectedApi;
    this.bsModalRef.content.type = type;
    this.bsModalRef.content.parentId = parentId;
  }

  openAddHeader() {
    this.bsModalRef = this.modalService.show(AddHeaderComponent);
    this.bsModalRef.content.api = this.selectedApi;

  }

  openSelectModel(items:any,filed:string,checkFiled:string,importHeader?:boolean,importParam?:boolean) {
    this.bsModalRef = this.modalService.show(SelectModelComponent,{class: 'modal-lg'});
    this.bsModalRef.content.checkFiled = checkFiled;
    this.bsModalRef.content.items = items;
    this.bsModalRef.content.filed = filed;
    if (importHeader == null) importHeader = false;
    if (importParam == null) importParam = false;
    this.bsModalRef.content.importHeader = importHeader;
    this.bsModalRef.content.importParam = importParam;
  }

  openParamJsonImport(type: string) {
    this.bsModalRef = this.modalService.show(ParamJsonImportComponent, {class: 'modal-lg'});
    this.bsModalRef.content.api = this.selectedApi;
    this.bsModalRef.content.type = type;
  }

  openCurlImport() {
    this.bsModalRef = this.modalService.show(CurlImportComponent,{class: 'modal-lg2'});
    this.bsModalRef.content.api = this.selectedApi;
  }

  openMarkdownPreview() {
    this.bsModalRef = this.modalService.show(PreviewMarkdownComponent, {class: 'modal-lg'});
    this.bsModalRef.content.apis = this.checkedApis;
    this.apiService.getMarkdownString(this.checkedApis).then(markdown => {
      this.bsModalRef.content.markdown = markdown;
    });
  }

  onSelectedApi(api: DocsApi) {
    this.selectedApi = api;
    this.selectedApi.params = sortParams(this.selectedApi.params);
    this.selectedApi.body = sortParams(this.selectedApi.body);
    this.selectedApi.result = sortParams(this.selectedApi.result);
    this.curlResultShow = false;
    this.javaSampleShow = false;
    this.javascriptSampleShow = false;
    this.paramJsonShow = false;
    this.bodyJsonShow = false;
  }

  onCheckedApi(api: DocsApi) {
    if (!this.checkedApis.includes(api)){
      this.checkedApis.push(api);
    }
  }

  onCheckedApiAll() {
    for (let obj of this.allApi) {
      this.onCheckedApi(obj);
    }
  }

  onClearCheckedApi() {
    this.checkedApis.splice(0);
  }

  analysisPathParam(url: string) {
    let reg = new RegExp('(?<={).*?(?=})', 'g');
    let results = url.match(reg);
    if (results != null) {
      results.forEach(result => {
        let param = new DocsPathParam;
        param["name"] = result;
        if (this.selectedApi.pathParams == null) {
          this.selectedApi.pathParams = [];
        }
        let push = true;
        for (let obj of this.selectedApi.pathParams) {
          if (obj.name == result) {
            push = false;
          }
        }

        if (push) {
          this.selectedApi.pathParams.push(param);
        }
      })
    }
  }

  editText(event: any, object: any, name: string) {
    let target = event.target;
    let text: string;
    let childNodes: any = event.target.childNodes;
    console.log(childNodes);
    if (childNodes.length > 0) {
      if (childNodes[0].nodeName == "INPUT") {
        return;
      }
      let node = childNodes[0];
      text = node.nodeValue;
      console.log(node);
      this.renderer2.removeChild(target, childNodes[0]);
    }
    const input = this.renderer2.createElement('input');
    this.renderer2.setAttribute(input, "type", "text");
    this.renderer2.setAttribute(input, "value", text);
    this.renderer2.addClass(input, 'form-control');
    this.renderer2.appendChild(target, input);

    input.focus();

    this.renderer2.listen(input, 'click', (event) => {
      event.stopPropagation();
    });

    this.renderer2.listen(input, 'blur', () => {
      object[name] = input.value;
      let parentNode = input.parentNode;
      this.renderer2.removeChild(parentNode, input);
      const text = this.renderer2.createText(input.value);
      this.renderer2.appendChild(parentNode, text);
    })
  }

  editTextarea(event: any, object: any, name: string) {
    let target = event.target;
    let text: string;
    let height: string = target.offsetHeight;
    let childNodes: any = event.target.childNodes;

    if (childNodes.length > 0) {
      if (childNodes[0].nodeName == "TEXTAREA") {
        return;
      }
      let node = childNodes[0];
      text = node.nodeValue;
      this.renderer2.removeChild(target, childNodes[0]);
    }
    const textarea = this.renderer2.createElement('textarea');
    this.renderer2.setStyle(textarea, 'width', '100%');
    this.renderer2.setStyle(textarea, 'height', height + "px");

    this.renderer2.addClass(textarea, 'form-control');
    const tText = this.renderer2.createText(text);
    this.renderer2.appendChild(textarea, tText);
    this.renderer2.appendChild(target, textarea);

    textarea.focus();

    this.renderer2.listen(textarea, 'click', (event) => {
      event.stopPropagation();
    });

    this.renderer2.listen(textarea, 'blur', () => {
      object[name] = textarea.value;
      let parentNode = textarea.parentNode;
      this.renderer2.removeChild(parentNode, textarea);
      const text = this.renderer2.createText(textarea.value);
      this.renderer2.appendChild(parentNode, text);
    });
  }

  paramType = ParamTypeMap;
  paramRequired = {'是': '是', '否': '否'};

  editSelect(event: any, object: any, name: string, options: Map<string, string>, defaultSelected?: string) {
    let target = event.target;
    let text: string;

    let childNodes: any = event.target.childNodes;
    if (childNodes.length > 0) {
      if (childNodes[0].nodeName == "OPTION") {
        return;
      }
      let node = childNodes[0];
      text = node.nodeValue;
      console.log(node);
      this.renderer2.removeChild(target, childNodes[0]);
    }
    const select = this.renderer2.createElement('select');
    this.renderer2.addClass(select, 'form-control');

    for (let opt in options) {
      const option = this.renderer2.createElement('option');
      const text = this.renderer2.createText(opt);
      if (defaultSelected == options[opt]) {
        this.renderer2.setAttribute(option, "selected", "selected");
      }
      this.renderer2.setAttribute(option, "value", options[opt]);
      this.renderer2.appendChild(option, text);
      this.renderer2.appendChild(select, option);
    }

    this.renderer2.appendChild(target, select);

    select.focus();

    this.renderer2.listen(select, 'click', (event) => {
      event.stopPropagation();
    });

    this.renderer2.listen(select, 'blur', () => {
      object[name] = select.value;
      let parentNode = select.parentNode;
      this.renderer2.removeChild(parentNode, select);
      const text = this.renderer2.createText(select.value);
      this.renderer2.appendChild(parentNode, text);
    })
  }

  updateOrCreateApi() {
    this.bsModalRef = this.modalService.show(SaveParamDetailsComponent);
    this.bsModalRef.content.folderId = this.folderId;

    this.apiService.updateOrCreateApi(this.allApi, this.deletedApis).then(()=>{
      this.loadApi();
    });
  }

  deleteApi(item: DocsApi) {
    if (item.id != null){
      this.deletedApis.push(item);
    }
    let index = 0;
    for (let obj of this.allApi) {
      if (obj == item) {
        this.allApi.splice(index, 1);
        break;
      }
      index++
    }
  }

  deleteParam(item: DocsParam) {

    let params: DocsParam[] = [];

    this.findChildAll(params, this.selectedApi.params, item);
    for (let obj of params) {
      let index = 0;
      for (let obj1 of this.selectedApi.params) {
        if (obj1.id == obj.id) {
          this.selectedApi.params.splice(index, 1);
          index = 0;
          break;
        }
        index++
      }
    }

  }

  deletePathParam(item: DocsPathParam) {
    let index = 0;

    for (let obj of this.selectedApi.pathParams) {
      if (obj.name == item.name) {
        this.selectedApi.pathParams.splice(index, 1);
        break;
      }
      index++
    }
  }

  deleteHeader(item: DocsHeader) {
    let index = 0;

    for (let obj of this.selectedApi.headers) {
      if (item == obj) {
        this.selectedApi.headers.splice(index, 1);
        break;
      }
      index++
    }
  }

  deleteBody(item: DocsParam) {
    let params: DocsParam[] = [];

    this.findChildAll(params, this.selectedApi.body, item);
    for (let obj of params) {
      let index = 0;
      for (let obj1 of this.selectedApi.body) {
        if (obj1.id == obj.id) {
          this.selectedApi.body.splice(index, 1);
          index = 0;
          break;
        }
        index++
      }
    }
  }


  deleteResult(item: DocsParam) {
    let params: DocsParam[] = [];

    this.findChildAll(params, this.selectedApi.result, item);
    for (let obj of params) {
      let index = 0;
      for (let obj1 of this.selectedApi.result) {
        if (obj1.id == obj.id) {
          this.selectedApi.result.splice(index, 1);
          index = 0;
          break;
        }
        index++
      }
    }
  }

  findChildAll(results: DocsParam[], params: DocsParam[], obj: DocsParam) {
    results.push(obj);

    let childNodes = findChild(params, obj);
    if (childNodes.length > 0) {
      for (let obj2 of childNodes) {
        this.findChildAll(results, params, obj2);
      }
    }
  }

  paramJson:any;
  paramJsonShow:boolean = false;

  previewParamJson(){
    if (this.paramJsonShow == false){
      this.paramJsonShow = true;
      let paramJson = this.formatJson(this.selectedApi.params);
      this.paramJson = JSON.stringify(JSON.parse(paramJson),null,2)
    }else{
      this.paramJsonShow = false;
    }
  }

  bodyJson:any;
  bodyJsonShow:boolean = false;

  previewBodyJson(){
    if (this.bodyJsonShow == false){
      this.bodyJsonShow = true;
      let bodyJson = this.formatJson(this.selectedApi.body);
      this.bodyJson = JSON.stringify(JSON.parse(bodyJson),null,2);
    }else{
      this.bodyJsonShow = false;
    }
  }

  resultJson:any;
  resultJsonShow:boolean = false;

  previewResultJson(){
    if (this.resultJsonShow == false){
      this.resultJsonShow = true;
      let resultJson = this.formatJson(this.selectedApi.result);
      this.resultJson = JSON.stringify(JSON.parse(resultJson),null,2)
    }else{
      this.resultJsonShow = false;
    }
  }

  curlResult:any;
  curlResultShow:boolean = false;
  previewCurlResult(){
    if (this.curlResultShow == false){
      if (this.checkCurl() ==false) return;
      this.curlResultShow = true;
      this.apiService.getCurlResult(this.selectedApi.curl).then(result =>{
        this.curlResult = JSON.stringify(result,null,2);
      }).catch(()=> this.curlResultShow = false );
    }else{
      this.curlResultShow = false;
    }
  }

  javaSample:any;
  javaSampleShow:boolean = false;
  previewJavaSample(){
    if (this.javaSampleShow == false){
      if (this.checkCurl() ==false) return;
      this.javaSampleShow = true;
      this.apiService.getJavaSampleByCurl(this.selectedApi.curl).then(result =>{
        this.javaSample = result;
      } );
    }else{
      this.javaSampleShow = false;
    }
  }

  checkCurl(){
    if (this.selectedApi.curl == null || this.selectedApi.curl == ''){
      alertError("请先导入curl示例!");
      return false;
    }
    return true;
  }

  javascriptSample:any;
  javascriptSampleShow:boolean = false;
  previewJavascriptSample(){
    if (this.javascriptSampleShow == false){
      if (this.checkCurl() ==false) return;
      this.javascriptSampleShow = true;
      this.apiService.getJavascriptSampleByCurl(this.selectedApi.curl).then(result =>{
        this.javascriptSample = result;
      } );
    }else{
      this.javascriptSampleShow = false;
    }
  }

  formatJson(params: DocsParam[]) {
    let jsonStr:string = "{";

    for (let obj of params) {
      if (obj.level == 1) {
       jsonStr += this.formatJson2(params,obj) +",";
      }
    }
    jsonStr = jsonStr.substr(0,jsonStr.length-1);
    jsonStr += "}";
    return jsonStr;
  }

  formatJson2( params: DocsParam[], obj: DocsParam,jsonStr?:string) {
    if (jsonStr == null) jsonStr = "";
    jsonStr += '\"' + obj.name + '\":';
    let childNodes = findChild(params, obj);

    if (childNodes.length > 0) {
      jsonStr += "[";
      for (let obj1 of childNodes) {
        jsonStr += "{";

        jsonStr += this.formatJson2(params,obj1);
        if (obj1 == childNodes[childNodes.length-1]){
          jsonStr += "}";
        }else{
          jsonStr += "},";
        }
      }
      jsonStr += "]";
    } else {
      let val = obj.defaultValue == null ? "" : obj.defaultValue;
      if (val.length == 0){
         val = ParamTypeDefaultValueMap[obj.type];
      }

      if (obj.type == 'String'){
        jsonStr +='\"'+val+'\"' ;
      }else{
        jsonStr += val;
      }
    }
    return jsonStr;
  }

  handleModalEvent() {
    this.modalService.onHide.subscribe((reason: string) => {
      if (this.bsModalRef != null) {
        if (this.bsModalRef.content.curlHtml != null){
          this.el.nativeElement.querySelector('#apiCurlHtml').innerHTML = this.bsModalRef.content.curlHtml;
        }
      }
    })
  }
}
