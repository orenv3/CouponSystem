import { Company } from './../../../common/Company';
import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';

@Component({
  selector: 'app-get-company',
  templateUrl: './get-company.component.html',
  styleUrls: ['./get-company.component.css']
})
export class GetCompanyComponent implements OnInit {

  public _company: Company = new Company();
  public _idNumber: number;
  constructor(private _webService: AdminService) {}

  ngOnInit() {}

  public getCompany() {
    var self = this;
    this._webService.getCompany(this._idNumber).subscribe(
      function (result) {
        self._company = result;
      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          if (err.status == 400) {
            swal("Company ID is mandatory!", "", "error");
          } else {
            if (err.status == 500) {
              swal("Company ID not found.", "", "error");
            } else
              swal("Network or server problem", "please try again later", "error");
          }
        }
      }
    )
  }

}

