import { Company } from './../../../common/Company';
import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';

@Component({
  selector: 'app-who-is-coupon-owner',
  templateUrl: './who-is-coupon-owner.component.html',
  styleUrls: ['./who-is-coupon-owner.component.css']
})
export class WhoIsCouponOwnerComponent implements OnInit {

  public notValid: boolean = false;
  public company: Company = new Company();
  public _compID: number;
  constructor(private _webService: AdminService) {

  }

  ngOnInit() {}

  changeBoolean(b: boolean) {
    this.notValid = b;
  }

  public WhoIs_couponOwner() {

    //// this block for *ngif ///// 
    this.changeBoolean(false); // re-initialization boolean value
    if (this._compID > 0) {
      this.changeBoolean(true);
    }

    // begin of WhoIs_couponOwner()
    const self = this;
    return this._webService.WhoIs_couponOwner(this._compID).subscribe(
      function (result) {
        for (let cmp of result) {
          cmp = new Company(cmp);
          self.company = cmp;
        }
      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          if (err.status == 400) {
            swal("ID field is mandatory!", "", "error");
          } else {
            if (err.status == 500) {
              swal("The ID field is incorect!", "There is no such coupon.", "info");
            } else
              swal("Network or server problem", "please try again later", "error");
          }
        }
      }

    )
  }


}

