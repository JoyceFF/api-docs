import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {baseUrl, handleError, requestOptions, url} from "../shared/app.common";
import {DocsProject} from "./model/docs-project";

@Injectable()
export class ProjectService{

  constructor(private http: Http) {
  }

  addProject(project:DocsProject){
    return this.http.post(url("/addProject"),JSON.stringify(project),requestOptions)
      .toPromise()
      .catch(handleError);
  }

  updateProject(id:string,map:Map<string,object>){
    return this.http.put(url("/updateProject/"+id),JSON.stringify(map),requestOptions)
      .toPromise()
      .catch(handleError);
  }

  delProject(id:string){
    return this.http.delete(url("/delProject/"+id))
      .toPromise()
      .catch(handleError);
  }

  findProject():Promise<DocsProject[]>{
    return this.http.get(url("/findProject"))
      .toPromise()
      .then(projects => projects.json() as DocsProject[])
      .catch(handleError);
  }

}
