import { Injectable } from '@angular/core';
import {baseUrl, handleError, requestOptions, url} from "../shared/app.common";
import {Http} from "@angular/http";
import {DocsModel} from "./model/docs-model";
import {DocsModelDetails} from "./model/docs-model-details";

@Injectable()
export class ModelService {

  constructor(private http: Http) { }

  getAllModel():Promise<DocsModel[]>{
    return this.http.get(url("/getAllModel"),requestOptions)
      .toPromise()
      .then(allModel => allModel.json() as DocsModel[])
      .catch(handleError);
  }

  addModel(model:DocsModel):Promise<JSON>{
    return this.http.post(url("/addModel"),JSON.stringify(model),requestOptions)
      .toPromise()
      .then(map => map.json())
      .catch(handleError);
  }


  upModel(modelId:string,model:Map<String,Object>):Promise<JSON>{

    return this.http.put(url("/updateModel/"+modelId),JSON.stringify(model),requestOptions)
      .toPromise()
      .then(map => map.json())
      .catch(handleError);
  }

  delModel(modelId:string):Promise<JSON>{
    return this.http.delete(url("/delModel/"+modelId),requestOptions)
      .toPromise()
      .then(map => map.json())
      .catch(handleError);
  }


  getModelDetailsByModelId(modelId:string):Promise<DocsModelDetails[]>{
    return this.http.get(url( "/getModelDetailsByModelId/"+modelId),requestOptions)
      .toPromise()
      .then(modelDetails => modelDetails.json() as DocsModelDetails[])
      .catch(handleError);
  }

  createOrUpdateModelDetails(modelId:string,addOrUpDetails:DocsModelDetails[],delDetails:DocsModelDetails[]){
    let body = {};
    body['addOrUpDetails']=addOrUpDetails;
    body['delDetails'] = delDetails;

    return this.http.put(url("/createOrUpdateModelDetails/"+modelId),JSON.stringify(body),requestOptions)
      .toPromise()
      .then()
      .catch(handleError);
  }

}
