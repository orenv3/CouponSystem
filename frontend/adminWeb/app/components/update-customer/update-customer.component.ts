import { Customer } from './../../../common/Customer';
import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';

@Component({
  selector: 'app-update-customer',
  templateUrl: './update-customer.component.html',
  styleUrls: ['./update-customer.component.css']
})
export class UpdateCustomerComponent implements OnInit {

  public customer: Customer = new Customer;
  public allCustomers: Customer[];
  constructor(private _webService: AdminService) {
    this.getAllTheCustomers();
  }

  ngOnInit() {}

  private updateCustomer() {
    const selfName = this.customer.name;
    return this._webService.updateCustomer(this.customer).subscribe(
      function (response) {
        console.log(response);
        swal("The customer " + selfName + " updated successfully", "", "info");

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


  private getAllTheCustomers() {

    const self = this;
    this.allCustomers = new Array;
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

  public setTheCustomer(c: Customer) {
    this.customer = new Customer(c);
    if (this.isEmpty(c)) {
      this.updateCustomer();
    } else {
      swal("Entering password value is mandatory!", "please enter password", "error")
    }

  }

  public isEmpty(company: Customer): boolean {
    if (company.password === "") {
      return false;
    } else {
      return true;
    }

  }


}

