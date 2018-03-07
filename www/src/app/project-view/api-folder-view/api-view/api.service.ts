import { Injectable } from '@angular/core';
import {Http} from "@angular/http";
import {DocsApi} from "./model/docs-api";
import {handleError, requestOptions, url} from "../../../shared/app.common";

@Injectable()
export class ApiService {

  constructor(private http:Http) {}

  addApi(api:DocsApi){
    return this.http.post(url("/addApi"),JSON.stringify(api),requestOptions)
      .toPromise()
      .catch(handleError);
  }

  updateOrCreateApi(apis:DocsApi[],delApis:DocsApi[]){
    let body = {};
    body["apis"] = apis;
    body["delApis"] = delApis;

    return this.http.put(url("/updateOrCreateApi"),JSON.stringify(body),requestOptions)
      .toPromise()
      .catch(handleError);
  }

  delApi(id:string){
    return this.http.delete(url("/delApi/"+id))
      .toPromise()
      .catch(handleError);
  }

  findApi(folderId:string):Promise<DocsApi[]>{
    return this.http.get(url("/findApi/"+folderId))
      .toPromise()
      .then(apis => apis.json() as DocsApi[])
      .catch(handleError);
  }

  getSaveApiDetails(folderId:string):Promise<string>{
    return this.http.get(url("/getSaveApiDetails/"+folderId))
      .toPromise()
      .then(details => details.text())
      .catch(handleError);
  }

  getCurlResult(curl:string):Promise<JSON>{
    let body = {};
    body['curl'] = curl;

    return this.http.post(url("/getCurlResult"),JSON.stringify(body),requestOptions)
      .toPromise()
      .then(result => result.json())
      .catch(handleError);
  }

  getJavascriptSampleByCurl(curl:string):Promise<string>{
    let body = {};
    body['curl'] = curl;

    return this.http.post(url("/getJavascriptSampleByCurl"),JSON.stringify(body),requestOptions)
      .toPromise()
      .then(javascript => javascript.text())
      .catch(handleError);
  }

  getJavaSampleByCurl(curl:string):Promise<string>{
    let body = {};
    body['curl'] = curl;

    return this.http.post(url("/getJavaSampleByCurl"),JSON.stringify(body),requestOptions)
      .toPromise()
      .then(java => java.text())
      .catch(handleError);
  }

  checkCurl(curl:string){
    let body = {};
    body['curl'] = curl;

    return this.http.post(url("/checkCurl"),JSON.stringify(body),requestOptions)
      .toPromise()
      .catch(handleError);
  }

  getMarkdownString(apis:DocsApi[]){

    return this.http.post(url("/getMarkdownString"),JSON.stringify(apis),requestOptions)
      .toPromise()
      .then(markdown => markdown.text())
      .catch(handleError);
  }

  generateMarkdown(markdown:string){
    let body = {};
    body['markdown'] = markdown;
    return this.http.post(url("/generateMarkdown"),JSON.stringify(body),requestOptions)
      .toPromise()
      .then(fileName => fileName.text())
      .catch(handleError);
  }
}
