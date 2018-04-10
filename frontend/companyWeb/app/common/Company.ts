import { Coupon } from "./Coupon";

export class Company{
public id:number;
public name:String; 
public password:String; 
public email:String
public compCoupons : Coupon[];
    constructor(private company ?: Company){
        if(company !=null){
            this.id = company.id;
            this.name = company.name;
            this.password = company.password;
            this.email = company.email;
            this.compCoupons = company.compCoupons;
        }
    }
}