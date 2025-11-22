import { Component, OnInit } from '@angular/core';
import { ToastService, Toast } from '../services/toast.service';

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.scss']
})
export class ToastComponent implements OnInit {
  constructor(public toastService: ToastService) {}

  ngOnInit() {
    console.log('ToastComponent initialized - simple version');
  }

  ngDoCheck() {
    // Auto-remove toasts after their delay
    this.toastService.toasts.forEach(toast => {
      if (!(toast as any).timeoutSet) {
        (toast as any).timeoutSet = true;
        setTimeout(() => {
          this.toastService.remove(toast);
        }, toast.delay || 3000);
      }
    });
  }
}
