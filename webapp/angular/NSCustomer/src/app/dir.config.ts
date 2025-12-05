import { MenuDir } from './directives/menu/menu.dir';
import { ProductsListDir } from './directives/productlist/productlist.dir';
import { AddToCartDir } from './directives/addToCart/addtocart.dir';
import { BillingDir } from './directives/billing/billing.dir';
import { BillingCartDir } from './directives/billingcart/billing.cart';
import { CheckOutDir } from './directives/checkout/checkout.dir';
import { ListAddressDir, DeleteAddressModalContent } from './directives/address/list/listaddressdir.component';
import { AddAddressDir } from './directives/address/add/address.component';
import { EditAddressDir, UpdateSuccessModalContent, UnsavedChangesModalContent } from './directives/address/edit/addressdir';
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
    DeleteAddressModalContent,
    AddAddressDir,
    EditAddressDir,
    UpdateSuccessModalContent,
    UnsavedChangesModalContent
];
