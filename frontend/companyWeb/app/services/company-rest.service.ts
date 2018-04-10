import { Injectable } from '@angular/core';
import { Coupon } from '../common/Coupon';
import {Http} from '@angular/http';
import 'rxjs/add/operator/map';


@Injectable()
export class CompanyRestService {
  private c: Coupon = new Coupon();
  constructor(private _http: Http) {}

  createCoupon(c: Coupon) {
    return this._http.post("/OCS2/companyRes/createCoupon", c);
  }

  getAllCoupons() {
    return this._http.get("/OCS2/companyRes/getAllCoupons").map(
      function (result) {
        return result.json();
      }
    )
  }

  removeCoupon(coupon: Coupon) {
    return this._http.delete("/OCS2/companyRes/removeCoupon", {
      body: coupon
    }).map(
      function (respons) {
        console.log(respons);
      }
    )
  }

  showMyCompany() {
    return this._http.get("/OCS2/companyRes/showMyCompany").map(
      function (result) {
        return result.json();
      }
    )
  }




  getCoupon(id: number) {
    return this._http.get("/OCS2/companyRes/getCoupon/" + id).map(
      function (result) {
        return result.json();
      })
  }



  getCouponByType() {
    return this._http.get("/OCS2/companyRes/getCouponByType").map(
      function (result) {
        return result.json();
      }
    )
  }


  getCouponByMaxPrice(maxPrice: number) {
    return this._http.get("/OCS2/companyRes/getCouponByMaxPrice/" + maxPrice).map(
      function (result) {
        return result.json();
      }
    )
  }


  getCouponByDate(date: Date) {
    return this._http.get("/OCS2/companyRes/getCouponByDate/" + date).map(
      function (result) {
        return result.json();
      })
  }


  updateCoupon(coupon: Coupon, start : String, end : String) {
    return this._http.put("/OCS2/companyRes/updateCoupon/"+start + "/" +end , coupon);
  }

  howManyCouponsinDB() {
    return this._http.get("/OCS2/companyRes/howManyCouponsinDB").map(
      function (result) {
        console.log(result);
        return result.json();
      }
    )
  }



}
