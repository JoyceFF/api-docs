import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {handleError, requestOptions, url} from "../../shared/app.common";
import {DocsApiFolder} from "./model/docs-api-folder";

@Injectable()
export class ApiFolderService{

  constructor(private http: Http) {
  }

  addApiFolder(apiFolder:DocsApiFolder){
    return this.http.post(url("/addApiFolder"),JSON.stringify(apiFolder),requestOptions)
      .toPromise()
      .catch(handleError);
  }

  updateApiFolder(id:string,map:Map<string,object>){
    return this.http.put(url("/updateApiFolder/"+id),JSON.stringify(map),requestOptions)
      .toPromise()
      .catch(handleError);
  }

  delApiFolder(id:string){
    return this.http.delete(url("/delApiFolder/"+id))
      .toPromise()
      .catch(handleError);
  }

  findApiFolder(projectId:string):Promise<DocsApiFolder[]>{
    return this.http.get(url("/findApiFolder/"+projectId))
      .toPromise()
      .then(projects => projects.json() as DocsApiFolder[])
      .catch(handleError);
  }
}
