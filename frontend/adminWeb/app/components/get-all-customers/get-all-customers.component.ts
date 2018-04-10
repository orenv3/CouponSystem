import { Customer } from './../../../common/Customer';
import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';


@Component({
  selector: 'app-get-all-customers',
  templateUrl: './get-all-customers.component.html',
  styleUrls: ['./get-all-customers.component.css']
})
export class GetAllCustomersComponent implements OnInit {

  public _customers: Customer[];
  constructor(private _webService: AdminService) {
    this.getAllCustomers();
  }

  ngOnInit() {}

  private getAllCustomers() {
    var self = this;
    return this._webService.getAllCustomers().subscribe(
      function (result) {
        self._customers = result;
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

