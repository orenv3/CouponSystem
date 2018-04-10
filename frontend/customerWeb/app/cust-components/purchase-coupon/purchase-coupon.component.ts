import { Coupon } from './../../../common/Coupon';
import { Component, OnInit } from '@angular/core';
import { CustomerRestService } from '../../services/customer-rest.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';

@Component({
  selector: 'app-purchase-coupon',
  templateUrl: './purchase-coupon.component.html',
  styleUrls: ['./purchase-coupon.component.css']
})
export class PurchaseCouponComponent implements OnInit {

  private _startDate: String;
  private _endDate: String;
  public _coupon: Coupon = new Coupon();
  public generalCoupons: Coupon[];
  constructor(private _webService: CustomerRestService) {}

  ngOnInit() {
    this.getAllCouponsFromDB();
  }

  private purchaseCoupon(c: Coupon) {
    this._coupon = new Coupon(c);
    this._coupon.endDate = new Date(this._coupon.endDate);
    this._coupon.startDate = new Date(this._coupon.startDate);
    this._endDate = new Date(this._coupon.endDate).toISOString().substring(0, 10);
    this._startDate = new Date(this._coupon.startDate).toISOString().substring(0, 10);
    return this._webService.purchaseCoupon(this._coupon, this._startDate, this._endDate).subscribe(
      function (response) {
        swal("You successfully purchased the coupon " + c.title, "please click ok to continue", "success");
      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          if (err.status == 500) {
            swal("You can not purchase this coupon", "Coupon name duplication OR coupon's inventory is over", "info");
          } else
            // if lost conectivity after the page is loaded
            swal("Network or server problem", "please try again later", "error");
        }
      }

    );

  }


  public setThisCoupon(cupn: Coupon) {
    swal({
        title: "Are you sure you want to purchase the coupon: " + cupn.title + "?",
        text: "Once purchased, you will need the admin support to delete it.",
        icon: "warning",
        closeOnEsc: true,
        dangerMode: true,
        buttons: ["cancel", true]
      })
      .then((willDelete) => {
        if (willDelete) {
          this._coupon = new Coupon(cupn);
          this._coupon.startDate = new Date(this._coupon.startDate);
          this._coupon.endDate = new Date(this._coupon.endDate);
          this.purchaseCoupon(this._coupon);
        } else {
          swal("canceled");
        }
      })
  }

  private getAllCouponsFromDB() {
    const self = this;
    this.generalCoupons = new Array;

    return this._webService.getAllCouponsFromDB().subscribe(
      function (result) {
        for (let cupn of result) {
          cupn.startDate = new Date(cupn.startDate).toDateString();
          cupn.endDate = new Date(cupn.endDate).toDateString();
          self.generalCoupons.push(cupn);
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



  }



