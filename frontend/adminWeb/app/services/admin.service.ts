import { Customer } from './../../common/Customer';
import { Company } from './../../common/Company';
import { Injectable } from '@angular/core';
import { Http } from '@angular/Http';
import 'rxjs/add/operator/map';
import { resolve } from 'q';
import { Coupon } from '../../common/Coupon';

@Injectable()
export class AdminService {

  constructor(private _http: Http) {}


  //create Company
  public createCompany(cmp: Company) {
    return this._http.post("/OCS2/admin/createCompany", cmp);
  }

  //create customer
  public createCustomer(cst: Customer) {
    return this._http.post("/OCS2/admin/createCustomer", cst);
  }

  //get All Companies
  public getAllCompanies() {
    // return this._http.get("/OCS2/admin/getAllCompanies").map(
    return this._http.get("http://localhost:8080/admin/getAllCompanies").map(
      function (result) {
        return result.json();
      }
    )
  }

  //get All customers
  public getAllCustomers() {
    return this._http.get("/OCS2/admin/getAllCustomers").map(
      function (result) {
        return result.json();
      }
    )
  }

  //get company by id
  public getCompany(id: number) {
    return this._http.get("/OCS2/admin/getCompany/" + id).map(
      function (result) {
        return result.json();
      }
    )
  }

  //get customer by id
  public getCustomer(id: number) {
    return this._http.get("/OCS2/admin/getCustomer/" + id).map(
      function (result) {
        return result.json();
      }
    )
  }

  //remove Company 
  public removeCompany(company: Company) {
    return this._http.delete("/OCS2/admin/removeCompany/", {
      body: company
    }).map(
      function (response) {
        console.log(response);
      }
    )
  }

  //remove customer
  public removeCustomer(customer: Customer) {
    return this._http.delete("/OCS2/admin/removeCustomer", {
      body: customer
    }).map(
      function (response) {
        console.log(response);
      }
    )
  }

  public removeCustomerCoupon(tmpCoupon: Coupon, CustomerID: number) {
    return this._http.delete("/OCS2/admin/removeCustomerCoupon/" + CustomerID, {
      body: tmpCoupon
    }).map(
      function (response) {
        console.log(response);
      }
    )
  }

  //who is the coupon owner : Company
  public WhoIs_couponOwner(id: number) {
    return this._http.get("/OCS2/admin/WhoIs_couponOwner/" + id).map(
      function (result) {
        return result.json();
      }
    )
  }

  //  Who is purchased the coupon : Customer
  public WhoIs_purchasedTheCoupon(id: number) {
    return this._http.get("/OCS2/admin/WhoIs_purchasedTheCoupon/" + id).map(
      function (result) {
        return result.json();
      }
    )
  }

  //update company
  public updateCompany(cmp: Company) {
    return this._http.put("/OCS2/admin/updateCompany", cmp);
  }

  //update customer
  public updateCustomer(cst: Customer) {
    return this._http.put("/OCS2/admin/updateCustomer", cst);
  }

}
