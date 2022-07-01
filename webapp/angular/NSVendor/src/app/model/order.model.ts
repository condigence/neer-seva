import { User } from './user.model';
import { OrderDetail } from './order-detail.model';


export class Order {
  orderId: any;
  orderDate: any;
  orderTime: any;
  orderDeliveryStatus: any;
  orderStatus: any;
  eta: any;
  orderGrandTotal: any;
  orderDetail:OrderDetail;
}
