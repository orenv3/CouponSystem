import { PurchaseCouponComponent } from './cust-components/purchase-coupon/purchase-coupon.component';
import { CustomerRestService } from './services/customer-rest.service';
import { NgModule, Component } from '@angular/core';
import { Services } from '@angular/core/src/view';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import {HttpModule} from '@angular/http';
import {FormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router'
import { GetAllPurchasedCouponsByTypeComponent } from './cust-components/get-all-purchased-coupons-by-type/get-all-purchased-coupons-by-type.component';
import { GetAllPurchasedCouponsByMaxPriceComponent } from './cust-components/get-all-purchased-coupons-by-max-price/get-all-purchased-coupons-by-max-price.component';
import { GetCouponsTillDateComponent } from './cust-components/get-coupons-till-date/get-coupons-till-date.component';
import { ShowCustomerProfileComponent } from './cust-components/show-customer-profile/show-customer-profile.component';

@NgModule({
  declarations: [
    AppComponent,
    GetAllPurchasedCouponsByTypeComponent,
    GetAllPurchasedCouponsByMaxPriceComponent,
    GetCouponsTillDateComponent,
    ShowCustomerProfileComponent,
    PurchaseCouponComponent,
  ],
  imports: [
    BrowserModule, 
    HttpModule, 
    FormsModule,
    RouterModule.forRoot([
    {
         path:'getAllpurchaseCouponsByType',
         component: GetAllPurchasedCouponsByTypeComponent
    },
   {
     path:'getAllpurchasedCouponsByMaxPrice',
     component: GetAllPurchasedCouponsByMaxPriceComponent
   } ,
   {
     path:'purchaseCoupon',
     component:  PurchaseCouponComponent
   },
   {
     path:'getCouponsTillDate',
     component: GetCouponsTillDateComponent
   },
   {
     path:'showCustomerProfile',
     component: ShowCustomerProfileComponent, 
   },
   {//defult router
     path:'',
     component: ShowCustomerProfileComponent, 
   }
    ])
  ],
  providers: [CustomerRestService],
  bootstrap: [AppComponent]
})
export class AppModule { }
