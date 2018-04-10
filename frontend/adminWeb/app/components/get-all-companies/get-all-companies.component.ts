import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { Company } from '../../../common/Company';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';


@Component({
  selector: 'app-get-all-companies',
  templateUrl: './get-all-companies.component.html',
  styleUrls: ['./get-all-companies.component.css']
})
export class GetAllCompaniesComponent implements OnInit {

  public _company: Company;
  constructor(private _webService: AdminService) {}

  ngOnInit() {
    this.getAllCompanies();
  }


  private getAllCompanies() {
    var self = this;
    return this._webService.getAllCompanies().subscribe(
      function (companies) {
        for (let c of companies)
          self._company = companies;
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

