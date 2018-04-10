import { HttpModule } from '@angular/Http';
import { AdminService } from './services/admin.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {FormsModule} from '@angular/forms';
import { RouterModule } from '@angular/router';


import { AppComponent } from './app.component';
import { CreateCompanyComponent } from './components/create-company/create-company.component';
import { WhoIsCouponOwnerComponent } from './components/who-is-coupon-owner/who-is-coupon-owner.component';
import { WhoIsPurchasedTheCouponComponent } from './components/who-is-purchased-the-coupon/who-is-purchased-the-coupon.component';
import { RemoveCompanyComponent } from './components/remove-company/remove-company.component';
import { UpdateCompanyComponent } from './components/update-company/update-company.component';
import { GetCompanyComponent } from './components/get-company/get-company.component';
import { GetAllCompaniesComponent } from './components/get-all-companies/get-all-companies.component';
import { CreateCustomerComponent } from './components/create-customer/create-customer.component';
import { RemoveCustomerComponent } from './components/remove-customer/remove-customer.component';
import { UpdateCustomerComponent } from './components/update-customer/update-customer.component';
import { GetCustomerComponent } from './components/get-customer/get-customer.component';
import { GetAllCustomersComponent } from './components/get-all-customers/get-all-customers.component';
import { RemoveCustomerCouponComponent } from './components/remove-customer-coupon/remove-customer-coupon.component';


@NgModule({
  declarations: [
    AppComponent,
    CreateCompanyComponent,
    WhoIsCouponOwnerComponent,
    WhoIsPurchasedTheCouponComponent,
    RemoveCompanyComponent,
    UpdateCompanyComponent,
    GetCompanyComponent,
    GetAllCompaniesComponent,
    CreateCustomerComponent,
    RemoveCustomerComponent,
    UpdateCustomerComponent,
    GetCustomerComponent,
    GetAllCustomersComponent,
    RemoveCustomerCouponComponent,
  ],
  imports: [
    BrowserModule, 
    HttpModule ,  
    FormsModule,
    RouterModule.forRoot([
      {
        path:'createCompany',
        component: CreateCompanyComponent
      },
      {
        path:'updateCompany',
        component: UpdateCompanyComponent
      },
      {
        path:'removeCompany',
        component: RemoveCompanyComponent
        
      },
      {
        path: 'getAllCompanies',
        component: GetAllCompaniesComponent
      },
      {
        path: 'getCompany',
        component: GetCompanyComponent
      },
      {
        path:'WhoIs_couponOwner',
        component: WhoIsCouponOwnerComponent
      },
      {
        path: 'createCustomer',
        component: CreateCustomerComponent
      },
      {
        path:'getAllCustomers',
        component: GetAllCustomersComponent
      },
      {
        path:'getCustomer',
        component: GetCustomerComponent
      },
      {
        path:'removeCustomer',
        component: RemoveCustomerComponent
      },
      {
        path: 'removeCustomerCoupon',
        component: RemoveCustomerCouponComponent
      },
      {
        path:'updateCustomer',
        component: UpdateCustomerComponent
      },
      {
        path: 'WhoIs_purchasedTheCoupon',
        component: WhoIsPurchasedTheCouponComponent
      },
      {//defult router
        path:'',
        component: CreateCompanyComponent
      }
    ])

  ],
  providers: [AdminService],
  bootstrap: [AppComponent]
})
export class AppModule { }
