import { Company } from './../../../common/Company';
import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';


@Component({
  selector: 'app-remove-company',
  templateUrl: './remove-company.component.html',
  styleUrls: ['./remove-company.component.css']
})
export class RemoveCompanyComponent implements OnInit {

  public _company: Company = new Company();
  public _companies: Company[];
  constructor(private _webService: AdminService) {
    this.getAllCompanies();
  }

  ngOnInit() {}

  private removeCompany() {

    return this._webService.removeCompany(this._company).subscribe(
      function (response) {
        console.log(response);
        swal("The company deleted successfully", "please click ok to continue", "success");
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

  getAllCompanies() {

    this._companies = new Array;
    const self = this;
    this._webService.getAllCompanies().subscribe(
      function (companies) {
        if (self._companies.length === 0) {
          for (let c of companies) {
            c = new Company(c);
            self._companies.push(c);
          }
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

  public setTheCompany(cmp: Company) {
    swal({
        title: "Are you sure you want to delete the company: " + cmp.name + "?",
        text: "Once deleted, you will not be able to recover this company.",
        icon: "warning",
        closeOnEsc: true,
        dangerMode: true,
        buttons: ["cancel", "delete"]
      })
      .then((willDelete) => {
        if (willDelete) {
          this._company = new Company(cmp);
          this.removeCompany();
        } else {
          swal("The company is safe!");
        }
      })
  }

}

