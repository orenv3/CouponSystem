import { CompanyRestService } from './services/company-rest.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {RouterModule} from '@angular/router';
import { AppComponent } from './app.component';
import { CreateCouponComponent } from './components/create-coupon/create-coupon.component';
import { DeleteCouponComponent } from './components/delete-coupon/delete-coupon.component';
import { ShowMyCompanyComponent } from './components/show-my-company/show-my-company.component';
import { GetCouponByIdComponent } from './components/get-coupon-by-id/get-coupon-by-id.component';
import { GetCouponByTypeComponent } from './components/get-coupon-by-type/get-coupon-by-type.component';
import { GetCouponByMaxPriceComponent } from './components/get-coupon-by-max-price/get-coupon-by-max-price.component';
import { GetCouponByDateComponent } from './components/get-coupon-by-date/get-coupon-by-date.component';
import { UpdateCouponComponent } from './components/update-coupon/update-coupon.component';
import { Component } from '@angular/core/src/metadata/directives';
import { HttpModule } from '@angular/http';
import {FormsModule} from '@angular/forms';


@NgModule({
  declarations: [
    AppComponent,
    CreateCouponComponent,
    DeleteCouponComponent,
    ShowMyCompanyComponent,
    GetCouponByIdComponent,
    GetCouponByTypeComponent,
    GetCouponByMaxPriceComponent,
    GetCouponByDateComponent,
    UpdateCouponComponent,
  ],
  imports: [
    BrowserModule,
    HttpModule,
    FormsModule,
    RouterModule.forRoot([
      {
        path: 'createCoupon',
        component: CreateCouponComponent
      },
      {
        path:'removeCoupon',
        component: DeleteCouponComponent
      },
      {
        path: 'showMyCompany',
        component: ShowMyCompanyComponent
      },
      {
        path: 'getCoupon',
        component: GetCouponByIdComponent
      },
      {
        path:'getCouponByType',
        component: GetCouponByTypeComponent
      },
      {
        path:'getCouponByMaxPrice',
        component:GetCouponByMaxPriceComponent
      },
      {
        path:'getCouponByDate',
        component:GetCouponByDateComponent
      },
      {
        path:'updateCoupon',
        component: UpdateCouponComponent
      },
      {//defult router
        path: '',
        component: ShowMyCompanyComponent
      }
    ])
  ],
  providers: [CompanyRestService],
  bootstrap: [AppComponent]
})
export class AppModule { }
