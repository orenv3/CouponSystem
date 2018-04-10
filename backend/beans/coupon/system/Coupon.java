package beans.coupon.system;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import general.CouponType;

/**
 * This class describe a Coupons class(Beans) Class Modifiers, getters setters
 * and toString The ENUM CouponTypes class is an inner class
 * 
 * @author Oren Vinogura
 *
 */
@XmlRootElement
public class Coupon implements Comparable<Coupon>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String title;
	private Date startDate;
	private Date endDate;
	private CouponType type;
	private int amount;
	private String message;
	private double price;
	private String image;

	public Coupon() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amaount) {
		this.amount = amaount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public CouponType getType() {
		return type;
	}

	public void setType(CouponType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Coupon [coupon id=" + id + ", title=" + title + ", start date=" + startDate + ", end date=" + endDate
				+ ", amaount=" + amount + ", type=" + type + ", message=" + message + ", price=" + price + ", image="
				+ image + "]";
	}

	@Override
	public int compareTo(Coupon o) {

		return this.getTitle().compareTo(o.getTitle());
	}

}
