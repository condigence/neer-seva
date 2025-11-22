import { Component } from '@angular/core';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-toast-notification',
  template: `
    <div class="toast-container">
      <div *ngFor="let toast of toastService.toasts" 
           class="toast" 
           [class.toast-success]="toast.type === 'success'"
           [class.toast-error]="toast.type === 'error'"
           [class.toast-warning]="toast.type === 'warning'"
           [class.toast-info]="toast.type === 'info'"
           [@slideIn]>
        <div class="toast-icon">
          <i class="zmdi" 
             [class.zmdi-check-circle]="toast.type === 'success'"
             [class.zmdi-alert-circle]="toast.type === 'error'"
             [class.zmdi-alert-triangle]="toast.type === 'warning'"
             [class.zmdi-info]="toast.type === 'info'"></i>
        </div>
        <div class="toast-content">
          <h6 class="toast-title">{{toast.title}}</h6>
          <p class="toast-message">{{toast.message}}</p>
        </div>
        <button class="toast-close" (click)="toastService.remove(toast)">
          <i class="zmdi zmdi-close"></i>
        </button>
      </div>
    </div>
  `,
  styles: [`
    .toast-container {
      position: fixed;
      top: 80px;
      right: 20px;
      z-index: 10000;
      display: flex;
      flex-direction: column;
      gap: 10px;
      max-width: 400px;
    }

    .toast {
      background: white;
      border-radius: 12px;
      padding: 16px;
      box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2);
      display: flex;
      align-items: flex-start;
      gap: 12px;
      animation: slideIn 0.3s ease-out;
      border-left: 4px solid;
      min-width: 320px;
    }

    @keyframes slideIn {
      from {
        transform: translateX(400px);
        opacity: 0;
      }
      to {
        transform: translateX(0);
        opacity: 1;
      }
    }

    .toast-success {
      border-left-color: #38ef7d;
    }

    .toast-error {
      border-left-color: #ff6b6b;
    }

    .toast-warning {
      border-left-color: #ffa502;
    }

    .toast-info {
      border-left-color: #667eea;
    }

    .toast-icon {
      flex-shrink: 0;
      width: 40px;
      height: 40px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .toast-success .toast-icon {
      background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
    }

    .toast-error .toast-icon {
      background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
    }

    .toast-warning .toast-icon {
      background: linear-gradient(135deg, #ffa502 0%, #ff6348 100%);
    }

    .toast-info .toast-icon {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    .toast-icon i {
      color: white;
      font-size: 1.5rem;
    }

    .toast-content {
      flex: 1;
      min-width: 0;
    }

    .toast-title {
      margin: 0 0 4px 0;
      font-size: 1rem;
      font-weight: 600;
      color: #2d3748;
    }

    .toast-message {
      margin: 0;
      font-size: 0.9rem;
      color: #718096;
      line-height: 1.4;
      word-wrap: break-word;
    }

    .toast-close {
      flex-shrink: 0;
      background: none;
      border: none;
      padding: 4px;
      cursor: pointer;
      color: #a0aec0;
      transition: all 0.2s ease;
      border-radius: 4px;
    }

    .toast-close:hover {
      background: #f7fafc;
      color: #2d3748;
    }

    .toast-close i {
      font-size: 1.2rem;
    }

    @media (max-width: 480px) {
      .toast-container {
        right: 10px;
        left: 10px;
        max-width: none;
      }

      .toast {
        min-width: auto;
      }
    }
  `]
})
export class ToastNotificationComponent {
  constructor(public toastService: ToastService) {}
}
