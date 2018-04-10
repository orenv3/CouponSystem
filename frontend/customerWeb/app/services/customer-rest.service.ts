import { Coupon } from './../../common/Coupon';
import { Injectable } from '@angular/core';
import {Http} from '@angular/http';
import 'rxjs/add/operator/map';
@Injectable()
export class CustomerRestService {
  private _cp: Coupon;
  constructor(private _http: Http) {}


  ////getAllpurchaseCoupons()
  getAllpurchasedCoupons() {
    return this._http.get("/OCS2/customerRes/getAllpurchaseCoupons").map(
      function (result) {
        return result.json();
      })
  }

  //get ALL Coupons from the database
  getAllCouponsFromDB() {
    return this._http.get(
      "/OCS2/customerRes/getAllCouponsFromDB()").map(
      function (result) {
        console.log(result);
        return result.json();
      })
  }

  ////getAllpurchaseCouponsByType()
  getAllpurchaseCouponsByType() {
    return this._http.get(
      "/OCS2/customerRes/getAllpurchaseCouponsByType").map(
      function (result) {
        return result.json();
      }
    )
  }

  //getAllpurchaseCouponsByMaxPrice()
  getAllpurchasedCouponsByMaxPrice(max: number) {
    return this._http.get(
      "/OCS2/customerRes/getAllpurchaseCouponsByMaxPrice/" + max).map(
      function (result) {
        return result.json();
      })
  }

  //getAllpurchaseCouponsByPrice()
  getAllpurchaseCouponsByPrice() {
    return this._http.get(
      "/OCS2/customerRes/getAllpurchaseCouponsByPrice").map(
      function (result) {
        return result.json();
      })
  }


  // purchase coupon
  purchaseCoupon(_cp: Coupon, str : String, end:String) {
    return this._http.post("/OCS2/customerRes/purchaseCoupon/"+str+"/"+end, _cp);
  }



  //getCouponsTillDate(date)
  getCouponsTillDate(d: Date) {
    return this._http.get(
      "/OCS2/customerRes/getCouponsTillDate/" + d).map(
      function (result) {
        return result.json();
      })
  }


  //get my profile
  showCustomerProfile() {
    return this._http.get(
      "/OCS2/customerRes/showCustomerProfile").map(
      function (result) {
        return result.json();
      })
  }




}


