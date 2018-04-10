import { Component, OnInit } from '@angular/core';
import { CompanyRestService } from '../../services/company-rest.service';
import {Coupon} from '../../common/Coupon';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';

@Component({
  selector: 'app-delete-coupon',
  templateUrl: './delete-coupon.component.html',
  styleUrls: ['./delete-coupon.component.css']
})
export class DeleteCouponComponent implements OnInit {

  public _coupons: Coupon[];
  public _coupon: Coupon = new Coupon();
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
          cup.endDate = new Date(cup.endDate).toLocaleDateString();
          cup.startDate = new Date(cup.startDate).toLocaleDateString();
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
  private removeCoupon() {
    this._coupon.startDate = new Date(this._coupon.startDate).getMilliseconds();
    this._coupon.endDate = new Date(this._coupon.endDate).getMilliseconds();
    return this._webService.removeCoupon(this._coupon).subscribe(
      function (response) {
        console.log(response);
        swal("The coupon deleted successfully", "please click ok to continue.", "success");
      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          // if lost conectivity after the page is loaded
          swal("Network or server problem", "please try again later", "error");
        }
      }
    )
  }

  setToDelete(cupon: Coupon) {
    swal({
        title: "The coupon: " + cupon.title + " is about to be erased , are you sure?",
        text: "Once deleted, you will not be able to recover this coupon.",
        icon: "warning",
        closeOnEsc: true,
        dangerMode: true,
        buttons: ["cancel", "delete"]
      })
      .then((willDelete) => {
        if (willDelete) {
          this._coupon = cupon;
          this.removeCoupon();
        } else {
          swal("canceled");
        }
      })

  }


}

