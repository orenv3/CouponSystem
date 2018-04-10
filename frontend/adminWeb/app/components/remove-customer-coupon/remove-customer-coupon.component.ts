import { Component, OnInit } from '@angular/core';
import { Customer } from '../../../common/Customer';
import { AdminService } from '../../services/admin.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';
import { Coupon } from '../../../common/Coupon';

@Component({
  selector: 'app-remove-customer-coupon',
  templateUrl: './remove-customer-coupon.component.html',
  styleUrls: ['./remove-customer-coupon.component.css']
})
export class RemoveCustomerCouponComponent implements OnInit {

  public customer: Customer = new Customer();
  public coupon: Coupon = new Coupon();
  public allCustomers: Customer[];
  constructor(private _webService: AdminService) {}

  ngOnInit() {
    this.getAllTheCustomers();
  }

  private removeCustomerCoupon() {
    return this._webService.removeCustomerCoupon(this.coupon, this.customer.id).subscribe(
      function (response) {
        console.log(response);
        swal("The coupon deleted successfully", "please click ok and refresh the page", "success");
      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          if (err.status == 400) {} else {
            if (err.status == 500) {
              swal("Coupon ID is incorrect!", "There is no such coupon.", "error");
            } else {
              // if lost conectivity after the page is loaded
              swal("Network or server problem", "please try again later", "error");
            }
          }
        }
      }
    )
  }

  public setTheRemove(c: Customer) {
    swal({
        title: "Are you sure you want to delete the coupon: " + this.coupon.id + "?",
        text: "Once deleted, you will be able to recover it.",
        icon: "warning",
        closeOnEsc: true,
        dangerMode: true,
        buttons: ["cancel", "delete"]
      })
      .then((willDelete) => {
        if (willDelete) {
          this.customer = new Customer(c);
          this.coupon = new Coupon(this.coupon);
          this.removeCustomerCoupon();
        } else {
          swal("The customer is safe!");
        }
      })
  }



  private getAllTheCustomers() {
    const self = this;
    self.allCustomers = new Array;
    return this._webService.getAllCustomers().subscribe(
      function (result) {
        for (let cst of result) {
          cst = new Customer(cst);
          self.allCustomers.push(cst);
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

