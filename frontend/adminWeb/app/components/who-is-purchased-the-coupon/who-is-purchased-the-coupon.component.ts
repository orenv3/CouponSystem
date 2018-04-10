import { Customer } from './../../../common/Customer';
import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';

@Component({
  selector: 'app-who-is-purchased-the-coupon',
  templateUrl: './who-is-purchased-the-coupon.component.html',
  styleUrls: ['./who-is-purchased-the-coupon.component.css']
})
export class WhoIsPurchasedTheCouponComponent implements OnInit {

  public customers: Customer[];
  public custId: number;
  constructor(private _webService: AdminService) {}

  ngOnInit() {}

  WhoIs_purchasedTheCoupon() {

    const self = this;
    self.customers = new Array;
    return this._webService.WhoIs_purchasedTheCoupon(this.custId).subscribe(
      function (result) {
        for (let cst of result) {
          cst = new Customer(cst);
          self.customers.push(cst);
        }
      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          if (err.status == 400) {
            swal("Customer ID is mandatory!", "", "error");
          } else {
            if (err.status == 500) {
              swal("The ID field is incorect!", "There is no such coupon", "info");
            } else
              swal("Network or server problem", "please try again later", "error");
          }
        }
      }
    )
  }

}

