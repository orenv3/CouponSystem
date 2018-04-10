
export class Customer{

    public id:number; 
    public name:String; 
    public password:String;
	public custCoupons = []; 
    
    constructor(private customer?: Customer){

        if (this.customer != null) {
            this.id = customer.id;
            this.name = customer.name;
            this.password = customer.password;
            this.custCoupons = customer.custCoupons;
        }
     }
    
}