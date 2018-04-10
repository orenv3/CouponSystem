
export class Coupon{
    
    public id:number; 
    public title:String; 
    public startDate:any; 
    public endDate:any;
    public type:any;
    public amount:number; 
    public message:String; 
    public price:number; 
    public image:String; 
       
        constructor(coupon ?: Coupon){
            if(coupon!=null){
                this.id=coupon.id;
                this.title=coupon.title;
                this.startDate=coupon.startDate;
                this.endDate=coupon.endDate;
                this.type=coupon.type;
                this.amount=coupon.amount;
                this.message=coupon.message;
                this.price=coupon.price;
                this.image=coupon.image;
            }
        }
    
    
    }