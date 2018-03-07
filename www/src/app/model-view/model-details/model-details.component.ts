import {Component, ElementRef, OnInit, Renderer2} from '@angular/core';
import {NgForm} from '@angular/forms';
import {BsModalRef} from "ngx-bootstrap";
import {ModelService} from "../model.service";
import {ParamType, ParamTypeMap} from "../../shared/app.common";
import {DocsModelDetails} from "../model/docs-model-details";

@Component({
  selector: 'modal-content',
  templateUrl: './model-details.component.html'
})

export class ModelDetailsComponent implements OnInit{
  ngOnInit(): void {

  }

  delModelDetails:DocsModelDetails[] = []
  modelDetails: DocsModelDetails[] = [];
  addModelFieldStatus: boolean;
  modelId:string;
  defaultType = "String";

  constructor(private bsModalRef: BsModalRef, private modelService: ModelService, private el: ElementRef, private renderer2: Renderer2) {
  }

  addModelField(){
    this.addModelFieldStatus = true;
  }

  getModelDetails(){
      this.modelService.getModelDetailsByModelId(this.modelId)
        .then(modelDatails => this.modelDetails = modelDatails);
  }

  close(){
    this.addModelFieldStatus = false;
  }

  delete(item){
    let index = 0;
    for (let obj of this.modelDetails) {
      if (obj == item) {
        this.modelDetails.splice(index, 1);
        if (obj.id != null){
          this.delModelDetails.push(obj);
        }
        break;
      }
      index++
    }
  }

  submit(form:NgForm){
    if (form.valid){
      let details:DocsModelDetails = new DocsModelDetails();
      details.modelId = this.modelId;
      details.describe = form.value["describe"];
      details.name = form.value["name"];
      details.type = form.value["type"];
      details.defaultValue = form.value["defaultValue"];

      this.modelDetails.push(details);

      this.addModelFieldStatus = false;
    }else{
      for (let obj in form.controls) {
        form.controls[obj].markAsTouched();
      }
    }

  }

  save(){
    this.modelService.createOrUpdateModelDetails(this.modelId,this.modelDetails,this.delModelDetails).then();
    this.bsModalRef.hide();
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
  paramTypeArray = ParamType;

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
}
