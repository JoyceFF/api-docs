import { Injectable } from '@angular/core';
import {baseUrl, handleError, requestOptions, url} from "../shared/app.common";
import {Http} from "@angular/http";
import {DocsApiPermission} from "./model/docs-api-permission";
import {DocsMarkdownTemplate} from "./model/docs-markdown-template";


@Injectable()
export class SettingsService {

  constructor(private http: Http) { }

  getAllApiPermission():Promise<DocsApiPermission[]>{
    return this.http.get(url("/findAllApiPermission"),requestOptions)
      .toPromise()
      .then(all => all.json() as DocsApiPermission[])
      .catch(handleError);
  }

  createOrUpdateApiPermission(allPermission:DocsApiPermission[]){
    return this.http.post(url("/createOrUpdatePermission"),JSON.stringify(allPermission),requestOptions)
      .toPromise()
      .then()
      .catch(handleError);
  }

  delApiPermission(id:string){
    return this.http.delete(url("/deleteApiPermission/"+id),requestOptions)
      .toPromise()
      .catch(handleError);
  }

  markdownTemplate(markdownTemplate:DocsMarkdownTemplate){
    return this.http.post(url("/markdownTemplate"),JSON.stringify(markdownTemplate),requestOptions)
      .toPromise()
      .then()
      .catch(handleError);
  }

  findMarkdownTemplate(){
    return this.http.get(url("/findMarkdownTemplate"),requestOptions)
      .toPromise()
      .then(all => all.json() as DocsMarkdownTemplate)
      .catch(handleError);
  }
}
