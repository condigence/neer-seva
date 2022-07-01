// import { UserType } from '../viewmodel/usertype.view.model';
// import { Role } from './Role';

export class User {
    id: string;
    name: string;
    contact: number;
    email: string;   
    isDeleted: string;
    isActive: string;
    dateCreated: string;   
    otp: number;
    token?:any;
    addressId: number;
}

