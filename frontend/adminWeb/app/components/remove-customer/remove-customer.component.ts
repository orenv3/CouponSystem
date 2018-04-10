import { Customer } from './../../../common/Customer';
import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';


@Component({
  selector: 'app-remove-customer',
  templateUrl: './remove-customer.component.html',
  styleUrls: ['./remove-customer.component.css']
})
export class RemoveCustomerComponent implements OnInit {

  public customer: Customer = new Customer();
  public allCustomers: Customer[];
  constructor(private _webService: AdminService) {
    this.getAllTheCustomers();
  }

  ngOnInit() {}

  private removeCustomer() {
    return this._webService.removeCustomer(this.customer).subscribe(
      function (response) {
        console.log(response);
        swal("The customer deleted successfully", "please click ok and refresh the page", "success");
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

  public setTheCustomer(c: Customer) {
    swal({
        title: "Are you sure you want to delete the customer: " + c.name + "?",
        text: "Once deleted, you will not be able to recover this customer.",
        icon: "warning",
        closeOnEsc: true,
        dangerMode: true,
        buttons: ["cancel", "delete"]
      })
      .then((willDelete) => {
        if (willDelete) {
          this.customer = new Customer(c);
          this.removeCustomer();
        } else {
          swal("The customer is safe!");
        }
      })
  }

}

