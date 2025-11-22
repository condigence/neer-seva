import { Injectable, TemplateRef } from '@angular/core';

export interface Toast {
  message: string;
  classname?: string;
  delay?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  toasts: Toast[] = [];

  show(message: string, classname: string = 'bg-success text-light', delay: number = 3000) {
    console.log('ToastService.show called:', { message, classname, delay });
    this.toasts.push({ message, classname, delay });
  }

  success(message: string, delay?: number) {
    this.show(message, 'bg-success text-light', delay);
  }

  error(message: string, delay?: number) {
    this.show(message, 'bg-danger text-light', delay);
  }

  info(message: string, delay?: number) {
    this.show(message, 'bg-info text-light', delay);
  }

  warning(message: string, delay?: number) {
    this.show(message, 'bg-warning text-dark', delay);
  }

  remove(toast: Toast) {
    this.toasts = this.toasts.filter(t => t !== toast);
  }

  clear() {
    this.toasts.splice(0, this.toasts.length);
  }
}
