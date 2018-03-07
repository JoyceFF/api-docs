import {Component, ElementRef, OnInit, Renderer2} from '@angular/core';
import {DocsApiPermission} from "./model/docs-api-permission";
import {SettingsService} from "./settings.service";
import {NgForm} from "@angular/forms";
import {DocsMarkdownTemplate} from "./model/docs-markdown-template";
import {alertSuccess} from "../shared/app.common";

@Component({
  selector: 'app-settings-view',
  templateUrl: './settings-view.component.html',
  styleUrls: ['./settings-view.component.css']
})
export class SettingsViewComponent implements OnInit {

  constructor(private settingsService: SettingsService, private el: ElementRef, private renderer2: Renderer2) {
  }

  ngOnInit() {
    this.loadAllApiPermission();
    this.loadMarkdownTemplate()
  }

  addApiPermissionStatus: boolean;
  allApiPermission: DocsApiPermission[] = [];

  markdownApiDetailsAllSettings: any[] = [
    {id: 1, name: '接口名称'},
    {id: 2, name: '接口描述'},
    {id: 3, name: '请求地址'},
    {id: 4, name: '路径参数'},
    {id: 5, name: '请求权限'},
    {id: 6, name: '请求header'},
    {id: 7, name: '查询参数'},
    {id: 8, name: 'body'},
    {id: 9, name: '返回值'},
    {id: 10, name: 'curl示例'},
    {id: 11, name: 'java示例'},
    {id: 12, name: 'javascript示例'},
    {id: 13, name: '返回值示例'},
    {id: 14, name: '请求类型'}
  ];
  markdownApiDetails: any[] = [];

  markdownAllSettings: any[] = [
    {id: 1, name: '接口列表'},
    {id: 2, name: '接口详情'},
  ];
  markdown: any[] = [];

  saveMarkdownTemplate() {
    let markdownTemplate: DocsMarkdownTemplate = new DocsMarkdownTemplate();
    console.log(markdownTemplate)
    for (let obj of this.markdown) {
      markdownTemplate.markdown.push(obj.id);
    }

    for (let obj of this.markdownApiDetails) {
      markdownTemplate.markdownApiDetails.push(obj.id);
    }

    this.settingsService.markdownTemplate(markdownTemplate).then(() => {
      alertSuccess("保存成功！");
    });
  }

  loadMarkdownTemplate() {
    this.settingsService.findMarkdownTemplate().then(markdownTemplate => {

      if (markdownTemplate.markdown.length > 0) {
        let markdown: any = [];
        let markdownAllSettings: any = [];
        for (let obj of markdownTemplate.markdown) {
          for (let obj1 of this.markdownAllSettings) {
            if (obj1.id == obj) {
              markdown.push(obj1);
            }
          }
        }
        for (let obj of this.markdownAllSettings) {
           if (!markdown.includes(obj)){
             markdownAllSettings.push(obj);
           }
        }


        this.markdown = markdown;
        this.markdownAllSettings = markdownAllSettings;
      }

      if (markdownTemplate.markdownApiDetails.length > 0) {
        let markdownApiDetails: any = [];
        let markdownApiDetailsAllSettings: any = [];

        for (let obj of markdownTemplate.markdownApiDetails) {
          for (let obj1 of this.markdownApiDetailsAllSettings) {
            if (obj1.id == obj) {
              markdownApiDetails.push(obj1);
            }
          }
        }

        for (let obj of this.markdownApiDetailsAllSettings) {
          if (!markdownApiDetails.includes(obj)){
            markdownApiDetailsAllSettings.push(obj);
          }
        }

        this.markdownApiDetails = markdownApiDetails;
        this.markdownApiDetailsAllSettings = markdownApiDetailsAllSettings;
      }
    });
  }

  loadAllApiPermission() {
    this.settingsService.getAllApiPermission().then(all => this.allApiPermission = all);
  }


  submit(form: NgForm) {
    if (form.valid) {
      let permission: DocsApiPermission = new DocsApiPermission();
      permission.name = form.value['name'];
      permission.describe = form.value['describe'];

      let permissions: DocsApiPermission[] = [];
      permissions.push(permission);

      this.settingsService.createOrUpdateApiPermission(permissions).then(() => {
        this.addApiPermissionStatus = false;
        this.loadAllApiPermission();
      });
    } else {
      for (let obj in form.controls) {
        form.controls[obj].markAsTouched();
      }
    }
  }

  delete(id: string) {
    this.settingsService.delApiPermission(id).then(() => {
      this.loadAllApiPermission();
    });
  }

  closeAdd() {
    this.addApiPermissionStatus = false;
  }

  saveApiPermission() {
    this.settingsService.createOrUpdateApiPermission(this.allApiPermission).then(() => {
      this.loadAllApiPermission();
      alertSuccess("保存成功！");
    });
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
}
