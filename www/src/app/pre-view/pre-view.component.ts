import {Component, ElementRef, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import * as Prism from 'prismjs';
import "prismjs/components/prism-bash";
import "prismjs/components/prism-json";
import "prismjs/components/prism-java";
import "prismjs/components/prism-javascript";

@Component({
  selector: 'pre-view',
  template: '<pre class="language-{{grammer}}"><code class="language-bash"></code></pre>',
  styleUrls: ['./pre-view.component.css']
})
export class PreViewComponent implements OnInit,OnChanges{
  ngOnChanges(changes: SimpleChanges): void {
    if(changes['pre'].currentValue != changes['pre'].previousValue){
      this.loadPreView();
    }
  }

  constructor(private el: ElementRef) { }

  ngOnInit() {
    this.loadPreView();
  }

  loadPreView(){
    let html = Prism.highlight(this.pre, this.getLanguages());
    this.el.nativeElement.querySelector('code').innerHTML = html;
  }

  @Input() pre:string;
  @Input() grammer: string;
  getLanguages(){
    switch (this.grammer){
      case 'base': return Prism.languages.bash;
      case 'json': return Prism.languages.json;
      case 'java': return Prism.languages.java;
      case 'javascript': return Prism.languages.javascript;
      default: return Prism.languages.javascript;
    }
  }
}
