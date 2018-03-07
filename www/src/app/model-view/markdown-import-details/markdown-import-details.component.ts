import {Component, ElementRef, OnInit, Renderer2} from '@angular/core';
import {BsModalRef, tr} from "ngx-bootstrap";
import {ModelService} from "../model.service";
import {checkType, findTable} from "../../markdown/markdown";
import {alertError, ParamType, ParamTypeMap, type} from "../../shared/app.common";
import {DocsModel} from "../model/docs-model";
import {DocsModelDetails} from "../model/docs-model-details";

@Component({
  selector: 'app-markdown-import-details',
  templateUrl: './markdown-import-details.component.html',
  styleUrls: ['./markdown-import-details.component.css']
})
export class MarkdownImportDetailsComponent implements OnInit {

  constructor(private bsModalRef: BsModalRef, private modelService: ModelService, private el: ElementRef, private renderer2: Renderer2) {
  }

  ngOnInit() {
    this.change();
  }


  analysis() {
    this.item = findTable(this.markdown);
    if (this.item == null) return;
    for (let obj of this.item.header) {
      if (this.headers[obj] == null) {
        this.headers[obj] = 'name';
      }
    }

    for (let call of this.item.cells) {
      let callMap = {};
      for (let i = 0; i < call.length; i++) {
        let h = this.item.header[i];
        let c = call[i];

        callMap[h] = c;
      }
      this.calls.push(callMap);
    }
  }

  save() {
    if(this.check()){
      if (this.modelDetails.length == 0){
        this.preview();
      }
      this.modelService.createOrUpdateModelDetails(this.modelId,this.modelDetails,[]).then();
      this.bsModalRef.hide();
    }
  }

  preview() {
    if (this.check() && this.previewStatus == false) {
      this.previewStatus = true;
      for (let call of this.calls) {
        let details: DocsModelDetails = new DocsModelDetails();
        details.modelId = this.modelId;
        for (let obj of this.item.header) {
          let f: string = this.headers[obj];
          let v: string = call[obj];
          if (f == 'type') {
            v = type(v);
          }
          details[f] = v;
        }
        this.modelDetails.push(details);
      }
    }
  }

  check() {
    for (let obj of this.item.header) {
      let index = 0;
      let value = this.headers[obj];
      for (let obj1 in this.headers) {
        if (this.headers[obj1] == value) {
          index++;
        }
      }
      if (index > 1) {
        alertError("不能两个model列相同!");
        return false;
      }
    }

    return true;
  }

  change() {
    this.analysis();
  }

  SelectChange(call: string, event) {
    let value: string = event.target.value;
    this.headers[call] = value;
  }

  delete(header) {
    for (let i = 0; i < this.item.header.length; i++) {
      if (header == this.item.header[i]) {
        this.item.header.splice(i, 1);
        delete this.headers[header];
        return;
      }
    }
  }

  deleteDetails(item){
    for (let i = 0; i < this.modelDetails.length; i++) {
      if (item == this.modelDetails[i]) {
        this.modelDetails.splice(i, 1);
        return;
      }
    }
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


  previewStatus:boolean = false;
  modelFields = ['name', 'type', 'defaultValue', 'describe']
  modelFieldsMap = {
    'name': '名称',
    'type': '类型',
    'defaultValue': '默认值',
    'describe': "描述"
  }
  modelDetails: DocsModelDetails[] = [];
  calls: any = [];
  headers: any = {};
  modelId: string;
  item: any;
  markdown: string = '';
//   markdown: string = `|           名称           |        类型         | 说明                               |
// | :--------------------: | :---------------: | :------------------------------- |
// |           id           |        int        | 商品id (唯一)                        |
// |         title          |    string(512)    | 标题                               |
// |        subTitle        |    string(128)    | 子标题                              |
// |         detail         |       text        | 商品详情(PC端使用)                      |
// |      simpleDetail      |       text        | 商品详情(手机端端使用)                     |
// |      serialNumber      |    string(128)    | 商品序号                             |
// |         stock          |        int        | 库存                               |
// |         price          |      number       | 现价（分）                            |
// |     originalPrice      |      number       | 原价（分）                            |
// |       onlineTime       |      number       | 上线时间                             |
// |       expireTime       |      number       | 过期时间                             |
// |          pics          |  array of string  | 商品缩略图                            |
// |      stockControl      |        int        | 库存管制 0:非管制 ,1：管制                 |
// |       coverIcon        |    string(128)    | 封面图片                             |
// |       multiSpec        |       bool        | 是否开启多规格                          |
// |       customAttr       |      string       | 自定义规格属性                          |
// |        priceMap        |        map        | 多规格价格列表                          |
// |         banner         |       bool        | 是否为banner 显示                     |
// |     priorityBanner     |        int        | banner 顺序                        |
// |       bannerIcon       |    string(128)    | banner 图片                        |
// |        obvious         |       bool        | 是否显示在首页                          |
// |       obviousSeq       |        int        | 首页序号,和obvious配合使用                |
// |       recommend        |       bool        | 是否推荐                             |
// |         remark         |   string(1024)    | 备注                               |
// |      description       |    string(255)    | 描述信息                             |
// |       freightId        |    string(128)    | 运费模板ID                           |
// |       freMeasure       |        int        | 商品 重量(克)/体积(dm³)                 |
// |         valid          |       bool        | 是否有效                             |
// |       saleCount        |        int        | 销量                               |
// |     baseSaleCount      |        int        | 基础销量                             |
// |       totalSale        |        int        | 总的销量(baseSaleCount+saleCount)    |
// |     customService      |       text        | 自定义服务                            |
// |       subscript        |        int        | 角标 0: 普通 1:热卖 2:抢购 3:推荐 4:特价     |
// | integralExchangePermit |       bool        | 是否允许使用积分                         |
// | integralExchangeScale  |        int        | 积分价格比例(%)                        |
// |       categories       |   array of map    | 商品的分类列表                          |
// |       commentNum       |        int        | 已经审核的评论数量                        |
// |          tags          |  array of string  | 标签列表                             |
// |       isNeedPost       |       bool        | 是否需要邮寄                           |
// |      levelSwitch       |       bool        | 等级购买限制                           |
// |         levels         | array of level id | 等级Ids                            |
// |    isHideBalancePay    |       bool        | 是否允许余额支付                         |
// |     promotionType      |        int        | 促销类型。0：抢购 1：拼团                   |
// |      panicSwitch       |       bool        | 促销开关                             |
// |       panicBegin       |       long        | 抢购开始时间                           |
// |        panicEnd        |       long        | 抢购结束时间                           |
// |       panicCount       |        int        | 抢购商品数量                           |
// |      panicStatus       |        int        | 当前商品抢购状态.未开始(0), 正在进行(1),已过期(2); |
// |       panicPrice       |       long        | 抢购价格(分)                          |
// |       groupBegin       |       long        | 拼团开始时间                           |
// |        groupEnd        |       long        | 拼团结束时间                           |
// |      groupStatus       |        int        | 拼团状态。未开始(0), 正在进行(1),已过期(2);     |
// |       groupPrice       |       long        | 拼团价格                             |
// |      groupPerson       |        int        | 成团人数                             |
// |   groupTimeInterval    |        int        | 成团时间,单位为分钟。 如 30分钟必须成团，否则成团失败    |
// |   isHideOriginPrice    |       bool        | 是否显示原价                           |
// |       createdAt        |      number       | 商品创建时间                           |
// |        updateAt        |      number       | 更新时间                             |`;


}
