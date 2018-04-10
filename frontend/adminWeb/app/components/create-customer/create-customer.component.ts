import { Customer } from './../../../common/Customer';
import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';


@Component({
  selector: 'app-create-customer',
  templateUrl: './create-customer.component.html',
  styleUrls: ['./create-customer.component.css']
})
export class CreateCustomerComponent implements OnInit {

  public newCustomerId: number;
  public _customers: Customer[];
  public _customer: Customer = new Customer();
  constructor(private _webService: AdminService) {
    this.newCustomerId = 0;
  }

  ngOnInit() {
    this.getAllCustomers();
  }

  public createCustomer() {
    if (this._customer.name == undefined || this._customer.password == undefined || this._customer.password == "" || this._customer.name == "") {
      swal("There are mandatory fileds", "", "error")
    } else {
      swal({
        title: "You are about to create the customer " + this._customer.name,
        text: "are you sure?",
        icon: "info",
        buttons: ["cancel", "create"],
        dangerMode: true,
      }).then((willCreate) => {
        if (willCreate) {
          this._customer = new Customer(this._customer);
          this._customer.id = this.newCustomerId;
          return this._webService.createCustomer(this._customer).subscribe(
            function (response) {
              console.log(response);
              swal("Congratulations! The customer created successfully", {
                icon: "success",
              });
            }, (err: HttpErrorResponse) => {
              if (err.error == Error) {
                swal("Something went wrong", "Please contact your manager", "error");
              } else {
                if (err.status == 500) {
                  swal("At least one of the fields is incorrect!", "Empty field OR customer name duplication", "error");
                } else {
                  swal("Network or server problem", "please try again later", "error");
                }
              }
            }
          )
        }
      })
    }
  }




  private getAllCustomers() {
    var self = this;
    return this._webService.getAllCustomers().subscribe(
      function (result) {
        self._customers = result;
        self.newCustomerId = self._customers.length + 1;
      }
    )
  }


}

