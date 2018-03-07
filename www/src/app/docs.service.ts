import {Injectable} from '@angular/core';
import { Http} from '@angular/http';

import 'rxjs/add/operator/toPromise';
import {MaxwonResource} from "./resource_list/model/resource";
import {Method} from "./resource_list/model/method";
import {MethodDetail} from "./resource_list/model/method-detail";
import {MaxwonParam} from "./resource_list/model/param";
import {MyMap} from "./resource_list/model/my-map";
import {baseUrl, handleError, requestOptions} from "./shared/app.common";
import {DocsModelDetails} from "./model-view/model/docs-model-details";
import {DocsModel} from "./model-view/model/docs-model";

@Injectable()
export class DocsService{

  constructor(private http: Http) {
    this.getResource().then();
  }
  resource:MaxwonResource[];

  getResource(): Promise<MaxwonResource[]> {
    return this.http
      .get(baseUrl + "/getAllResource?path=/Users/jiaweiwei/AppMaker")
      .toPromise()
      .then(res => {
        let result = res.json() as MaxwonResource[];
        this.resource = result;
        return result;
      })
      .catch(handleError);
  }

  getMethods(resourceId:string):Promise<Method[]>{
    return this.http.get(baseUrl+"/getAllMethod?resourceId="+resourceId)
      .toPromise()
      .then(methods => methods.json() as Method[])
      .catch(handleError);
  }

  getMethodDetail(methodId:string,header:Map<string,string>,param:Map<string,string>):Promise<MethodDetail>{
    let body = {};
    body["methodId"]=methodId;
    body["header"] = header;
    body["param"] = param;

    return this.http.post(baseUrl+"/getMethodDetail",JSON.stringify(body),requestOptions)
      .toPromise()
      .then(methodDetail => methodDetail.json() as MethodDetail)
      .catch(handleError);
  }

  getAllMethodDetail(resourceId:string,uuid:string):Promise<MethodDetail[]>{
    let body = {};
    body["resourceId"]=resourceId;
    body["uuid"] = uuid;

    return this.http.post(baseUrl+"/getAllMethodDetail",JSON.stringify(body),requestOptions)
      .toPromise()
      .then(methodDetails => methodDetails.json())
      .catch(handleError);
  }

  getAllMethodDetailProgress(uuid:string):Promise<number>{
    return this.http.get(baseUrl+"/getAllMethodDetailProgress?uuid="+uuid)
      .toPromise()
      .then(methods => Number.parseInt(methods.text()))
      .catch(handleError);
  }

  getDefaultHeader(permission:string):Promise<Map<string,string>>{
     return this.http.get(baseUrl+"/getDefaultHeader?permission="+permission)
       .toPromise()
       .then(defaultHeader => defaultHeader.json() as Map<string,string>)
       .catch(handleError);
  }

  getRequest(url:string,http:string,permission:string,header:Map<string,string>,param:Map<string,string>,params:MaxwonParam[]):Promise<MethodDetail>{
     let body = {};
     body["url"] = url;
     body["http"] = http;
     body["permission"] = permission;
     body["header"] = header;
     body["param"] = param;
     body["params"] = params;

     return this.http.post(baseUrl+"/getResult",JSON.stringify(body),requestOptions)
       .toPromise()
       .then(result => result.json() as MethodDetail)
       .catch(handleError);
  }

  getMarkdown(methodDetails:MethodDetail[]):Promise<string>{
     let body = {};
     body["methodDetails"] = methodDetails;
     return this.http.post(baseUrl + "/getMarkdown",JSON.stringify(body),requestOptions)
       .toPromise()
       .then(markdown => markdown.json()["markdown"])
       .catch(handleError);
  }

  generate(methodDetails:MethodDetail[]):Promise<string>{
    let body = {};
    body["methodDetails"] = methodDetails;
    return this.http.post(baseUrl + "/generate",JSON.stringify(body),requestOptions)
      .toPromise()
      .then(markdown => markdown.json()["fileName"])
      .catch(handleError);
  }

  getAllModel():Promise<DocsModel[]>{
    return this.http.get(baseUrl + "/getAllModel",requestOptions)
      .toPromise()
      .then(allModel => allModel.json() as DocsModel[])
      .catch(handleError);
  }

  addModel(model:DocsModel):Promise<MyMap>{
    return this.http.post(baseUrl + "/addModel",JSON.stringify(model),requestOptions)
      .toPromise()
      .then(map => map.json() as MyMap)
      .catch(handleError);
  }


  upModel(model:DocsModel):Promise<MyMap>{
    return this.http.put(baseUrl + "/updateModel",JSON.stringify(model),requestOptions)
      .toPromise()
      .then(map => map.json() as MyMap)
      .catch(handleError);
  }

  delModel(modelId:string):Promise<MyMap>{
    return this.http.delete(baseUrl + "/delModel/"+modelId,requestOptions)
      .toPromise()
      .then(map => map.json() as MyMap)
      .catch(handleError);
  }

  getModelDetails(modelId:string):Promise<DocsModelDetails[]>{
    return this.http.get(baseUrl + "/getModelDetails?modelId="+modelId,requestOptions)
      .toPromise()
      .then(modelDetails => modelDetails.json() as DocsModelDetails[])
      .catch(handleError);
  }

  upModelDetails(modelId:string,details:DocsModelDetails[]):Promise<MyMap>{
    let body = {};
    body["modelId"] = modelId;
    body["details"] = details;
    return this.http.put(baseUrl + "/updateModelDetails",JSON.stringify(body),requestOptions)
      .toPromise()
      .then(map => map.json() as MyMap)
      .catch(handleError);
  }

  uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
      s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
  }
}
