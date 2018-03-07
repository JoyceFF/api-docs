import { Component } from '@angular/core';

@Component({
  selector: 'demo-alert-timeout',
  templateUrl: './alert-error.component'
})
export class AlertErrorComponent {
  alerts: any = [];

  add(): void {
    this.alerts.push({
      type: 'info',
      msg: `This alert will be closed in 5 seconds (added: ${new Date().toLocaleTimeString()})`,
      timeout: 5000
    });
  }
}
