import { Coupon } from './Coupon';

export class Customer{

	 
    constructor(private customer ?: Customer, public id?:number, public name?:String, public password?:String, public coupons ?: Coupon[]){
        
        if(this.customer != null){
            this.id=customer.id;
            this.name=customer.name;
            this.password=customer.password;
            this.coupons = customer.coupons;

            }
        }


    }