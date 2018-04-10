import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { CompanyRestService } from '../../services/company-rest.service';
import {Coupon} from '../../common/Coupon';
import swal from 'sweetalert';

@Component({
  selector: 'app-create-coupon',
  templateUrl: './create-coupon.component.html',
  styleUrls: ['./create-coupon.component.css']
})
export class CreateCouponComponent implements OnInit {

  public coupon: Coupon = new Coupon();
  public imagWeb: String;
  public idCount: number;
  constructor(private _companyService: CompanyRestService) {
    this.coupon.amount = 10;
    this.coupon.price = 20;
    this.coupon.image = "https://www.remkes.com/wp-content/uploads/2016/04/printable-coupons.jpg";
  }

  ngOnInit() {
    this.howManyCouponsinDB();
  }

  howManyCouponsinDB() {
    const self = this;
    return this._companyService.howManyCouponsinDB().subscribe(
      function (result) {
        console.log(self.idCount);
        self.idCount = result;
      }
    )
  }

  createCoupon() {
    this.coupon.id = this.idCount;
    if (this.coupon.amount > 0) {
      return this._companyService.createCoupon(this.coupon).subscribe(
        function (response) {
          console.log(response);
          swal("The coupon created successfully!", "please click ok to continue", "success");
        },
        (err: HttpErrorResponse) => {
          if (err.error == Error) {
            swal("Something went wrong", "Please contact your manager", "error");
          } else {
            if (err.status == 500) {
              swal("All the fields are mandatory.", "please check all the parameters.", "info");
            } else {
              swal("Network or server problem", "please try again later", "error");
            }
          }
        }
      )
    } else swal("The amount value must be bigger then 1!", "", "info");
  }




}
