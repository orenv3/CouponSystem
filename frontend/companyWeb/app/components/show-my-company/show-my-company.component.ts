import { Company } from './../../common/Company';
import { Component, OnInit } from '@angular/core';
import { CompanyRestService } from '../../services/company-rest.service';
import { Coupon } from '../../common/Coupon';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';


@Component({
  selector: 'app-show-my-company',
  templateUrl: './show-my-company.component.html',
  styleUrls: ['./show-my-company.component.css']
})
export class ShowMyCompanyComponent implements OnInit {

  public _allCoupons: Coupon[];
  public currentCompany: Company = new Company();
  constructor(private _webService: CompanyRestService) {
    this.getAllCoupons();
    this.ShowMyCompany();
  }

  ngOnInit() {}

  private getAllCoupons() {
    const self = this;
    this._allCoupons = new Array;
    return this._webService.getAllCoupons().subscribe(
      function (result) {
        for (let cup of result) {
          cup = new Coupon(cup);
          cup.endDate = new Date(cup.endDate).toLocaleDateString();
          cup.startDate = new Date(cup.startDate).toLocaleDateString();
          self._allCoupons.push(cup);
          if (cup.amount === 0) {
            swal("For your information", "The inventory of the coupon [ coupon id: " + cup.id + ", coupon title: " + cup.title + " ] is over, please update the coupon", "info")
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

  public ShowMyCompany() {

    const self = this;
    return this._webService.showMyCompany().subscribe(
      function (result) {
        self.currentCompany = new Company(result);
      }
    )
  }



}

