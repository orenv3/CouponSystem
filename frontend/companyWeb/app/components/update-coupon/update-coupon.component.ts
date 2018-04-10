import { CompanyRestService } from './../../services/company-rest.service';
import { Component, OnInit } from '@angular/core';
import { Coupon } from '../../common/Coupon';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';


@Component({
  selector: 'app-update-coupon',
  templateUrl: './update-coupon.component.html',
  styleUrls: ['./update-coupon.component.css']
})
export class UpdateCouponComponent implements OnInit {

  private _startDate: String;
  private _endDate: String;
  public _coupons: Coupon[];
  public currCoupon: Coupon = new Coupon();
  constructor(private _webService: CompanyRestService) {
    this.getAllCoupons();
  }

  ngOnInit() {}

  private getAllCoupons() {
    const self = this;
    this._coupons = new Array;
    return this._webService.getAllCoupons().subscribe(
      function (result) {
        for (let cup of result) {
          cup = new Coupon(cup);
          cup.endDate = new Date(cup.endDate).toDateString();
          cup.startDate = new Date(cup.startDate).toDateString();
          self._coupons.push(cup);
        }
      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          swal("Network or server problem", "please try again later", "error");
        }
      }

    )
  }

  private updateCoupon() {
    const self = this.currCoupon;
    return this._webService.updateCoupon(this.currCoupon, this._startDate, this._endDate).subscribe( //, this.startDate, this.endDate
      function (response) {
        console.log(response);
        swal("The coupon: " + self.title + " successfully updated", "click OK to continue", "info");
      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          swal("Network or server problem", "please try again later", "error");
        }
      }
    )
  }

  public setCoupon2Update(cup: Coupon) {
    this.currCoupon = new Coupon(cup);
    this.currCoupon.endDate = new Date(this.currCoupon.endDate);
    this.currCoupon.startDate = new Date(this.currCoupon.startDate);
    this._endDate = new Date(this.currCoupon.endDate).toISOString().substring(0, 10);
    this._startDate = new Date(this.currCoupon.startDate).toISOString().substring(0, 10);
    this.updateCoupon();
  }



}

