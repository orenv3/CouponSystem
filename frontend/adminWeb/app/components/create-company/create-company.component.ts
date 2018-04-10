import { HttpErrorResponse } from '@angular/common/http';
import { AdminService } from './../../services/admin.service';
import { Component, OnInit } from '@angular/core';
import { Company } from '../../../common/Company';
import swal from 'sweetalert';


@Component({
  selector: 'app-create-company',
  templateUrl: './create-company.component.html',
  styleUrls: ['./create-company.component.css']
})
export class CreateCompanyComponent implements OnInit {

  public _company: Company = new Company();
  public newCompanyID: number;
  constructor(private _webService: AdminService) {
    this.newCompanyID = 2;
    this.howManyCompanies();
    this.newCompanyID = this.newCompanyID + 1;
  }

  ngOnInit() {}

  createCompany() {
    if (this._company.name == undefined || this._company.password == undefined || this._company.password == "" || this._company.name == "") {
      swal("There are mandatory fileds", "", "error")
    } else {
      swal({
          title: "You are about to create the company " + this._company.name,
          text: "are you sure?",
          icon: "info",
          buttons: ["cancel", "create"],
          dangerMode: true,
        })
        .then((willCreate) => {
          if (willCreate) {
            this._company = new Company(this._company);
            this._company.id = this.newCompanyID;
            this._webService.createCompany(this._company).subscribe(
              function (response) {
                console.log(response);
                swal("Congratulations! The company created successfully", {
                  icon: "success",
                });
              },
              (err: HttpErrorResponse) => {
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




  private howManyCompanies() {
    const self = this;
    return this._webService.getAllCompanies().subscribe(
      function (companies) {
        for (let c of companies) {
          self._company = companies;
          self.newCompanyID = self.newCompanyID + 1;
        }
        self.newCompanyID = self.newCompanyID + 1;

      }
    )
  }


}

