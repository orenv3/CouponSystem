import { CompanyRestService } from './../../services/company-rest.service';
import { Component, OnInit } from '@angular/core';
import { Coupon } from '../../common/Coupon';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';


@Component({
  selector: 'app-get-coupon-by-id',
  templateUrl: './get-coupon-by-id.component.html',
  styleUrls: ['./get-coupon-by-id.component.css']
})
export class GetCouponByIdComponent implements OnInit {
  public valid: boolean = false;
  public _id: number;
  public _coupon: Coupon = new Coupon();
  constructor(private _webService: CompanyRestService) {

  }

  ngOnInit() {}

  changeValue(change: boolean) {
    this.valid = change;
  }

  getCoupon() { 
    
    //// this block for *ngif ///// 
    this.changeValue(false); // re-initialization boolean value
    if (this._id > 0) {
      this.changeValue(true);
    }

    // begin of getCoupon()
    const self = this;
    return this._webService.getCoupon(this._id).subscribe(
      function (result) {
        self._coupon = new Coupon(result);
        self._coupon.startDate = new Date(self._coupon.startDate).toLocaleDateString();
        self._coupon.endDate = new Date(self._coupon.endDate).toLocaleDateString();
      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          if (err.status == 400) {
            swal("Entering an id value is mandatory!", "please enter one of your coupons id.", "error");
          } else {
            if (err.status == 500) {
              swal("The ID field is incorect", "you must own the coupon in order to view his details.", "info");
            } else {
              swal("Network or server problem", "please try again later", "error");
            }
          }
        }
      }
    )
  }

  }

