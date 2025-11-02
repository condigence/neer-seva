import { MenuDir } from './directives/menu.dir';
import { ProductsListDir } from './directives/productslist.dir';
import { AddToCartDir } from './directives/addtocart.dir';
import { BillingDir } from './directives/billing.dir';
import { BillingCartDir } from './directives/billingcart.dir';
import { CheckOutDir } from './directives/checkout.dir';
import { ListAddressDir } from './directives/address/list/listaddressdir.component';
import { AddAddressDir } from './directives/address/add/address.component';
import { EditAddressDir } from './directives/address/edit/addressdir';
// import { AddressDir } from './directives/address/addresses/addressdir.component';
// import { EditAddressDir } from './directives/address/editAddressdir/addressdir';
// import { AddAddressDir } from './directives/address/addAddress/address.component';

export const dirConfig = [ 
    MenuDir,
    ProductsListDir,
    AddToCartDir,
    BillingDir,
    BillingCartDir,
    CheckOutDir,
    ListAddressDir,
    AddAddressDir
    ,
    EditAddressDir
];
