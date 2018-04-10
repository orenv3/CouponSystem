import { Coupon } from './../../../common/Coupon';
import { Component, OnInit } from '@angular/core';
import { Customer } from '../../../common/Customer';
import { CustomerRestService } from '../../services/customer-rest.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';

@Component({
  selector: 'app-show-customer-profile',
  templateUrl: './show-customer-profile.component.html',
  styleUrls: ['./show-customer-profile.component.css']
})
export class ShowCustomerProfileComponent implements OnInit {

  public customer: Customer = new Customer();
  public coupons: Coupon[];
  constructor(private _webService: CustomerRestService) {
    this.showCustomerProfile();
  }

  ngOnInit() {}

  showCustomerProfile() {
    const self = this;
    this.coupons = new Array;
    this._webService.showCustomerProfile().subscribe(
      function (response) {
        self.customer = new Customer(response);
        for (let cp of self.customer.coupons) {
          cp.startDate = new Date(cp.startDate).toLocaleDateString();
          cp.endDate = new Date(cp.endDate).toLocaleDateString();
          self.coupons.push(cp);
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

