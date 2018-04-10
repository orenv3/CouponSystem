import { CompanyRestService } from './../../services/company-rest.service';
import { Component, OnInit } from '@angular/core';
import { Coupon } from '../../common/Coupon';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';


@Component({
  selector: 'app-get-coupon-by-max-price',
  templateUrl: './get-coupon-by-max-price.component.html',
  styleUrls: ['./get-coupon-by-max-price.component.css']
})
export class GetCouponByMaxPriceComponent implements OnInit {

  public _coupons: Coupon[];
  public maxPrice: number;
  constructor(private _webService: CompanyRestService) {}

  ngOnInit() {}

  getCouponByMaxPrice() {
    const self = this;
    this._coupons = new Array;
    return this._webService.getCouponByMaxPrice(this.maxPrice).subscribe(
      function (result) {
        for (let c of result) {
          c = new Coupon(c);
          c.startDate = new Date(c.startDate).toLocaleDateString();
          c.endDate = new Date(c.endDate).toLocaleDateString();
          self._coupons.push(c);
        }
      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          if (err.status == 400) {
            swal("Entering a price is mandatory!", "", "error");
          } else {
            if (err.status == 500) {
              swal("The price is too low!", "you need to raise the price.", "info");
            } else {
              swal("Network or server problem", "please try again later", "error");
            }
          }
        }
      }
    )
  }


}
