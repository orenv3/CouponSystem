import { Company } from './../../../common/Company';
import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';

@Component({
  selector: 'app-update-company',
  templateUrl: './update-company.component.html',
  styleUrls: ['./update-company.component.css']
})
export class UpdateCompanyComponent implements OnInit {
  public companies: Company[];
  public company: Company = new Company();

  constructor(private _webService: AdminService) {
    this.getAllcompanies();
  }

  ngOnInit() {}

  private updateCompany() {
    const selfName = this.company.name;
    return this._webService.updateCompany(this.company).subscribe(
      function (response) {
        console.log(response);
        swal("The company " + selfName + " updated successfully", "", "info");
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

  private getAllcompanies() {

    this.companies = new Array;
    const self = this;
    return this._webService.getAllCompanies().subscribe(
      function (result) {
        for (let cmp of result) {
          cmp = new Company(cmp);
          self.companies.push(cmp);
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

  public setTheCompany(c: Company) {
    this.company = new Company(c);
    if (this.isEmpty(c)) {
      this.updateCompany();
    } else {
      swal("Entering password value is mandatory!", "please enter password", "error")
    }
  }

  public isEmpty(company: Company): boolean {
    if (company.password === "") {
      return false;
    } else {
      return true;
    }

  }

}

