import { Customer } from './../../../common/Customer';
import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';


@Component({
  selector: 'app-get-customer',
  templateUrl: './get-customer.component.html',
  styleUrls: ['./get-customer.component.css']
})
export class GetCustomerComponent implements OnInit {

  public _customer: Customer = new Customer();
  public _idCust: number;
  constructor(private _webService: AdminService) {}

  ngOnInit() {}

  public getCustomer() {

    var self = this;
    this._webService.getCustomer(this._idCust).subscribe(
      function (result) {
        self._customer = result;
      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          if (err.status == 400) {
            swal("Customer ID is mandatory!", "", "error");
          } else {
            if (err.status == 500) {
              swal("Customer ID not found.", "", "error");
            } else
              swal("Network or server problem", "please try again later", "error");
          }
        }
      }
    )
  }


}
