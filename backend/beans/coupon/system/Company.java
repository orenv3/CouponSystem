package beans.coupon.system;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class describe a Company class(Beans) Class Modifiers, getters setters
 * and toString
 * 
 * @author Oren Vinogura
 *
 */
@XmlRootElement
public class Company implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String password;
	private String email;
	private Collection<Coupon> compCoupons = new LinkedList<Coupon>();

	public Company() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String compName) {
		this.name = compName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Coupon> getCoupons() {
		return compCoupons;
	}

	public void setCoupons(Collection<Coupon> compCoupons) {
		this.compCoupons = compCoupons;
	}

	@Override
	public String toString() {
		return "Company [company id=" + id + ", company name=" + name + ", password=" + password + ", email=" + email
				+ "\ncompany Coupons=: " + compCoupons + "]";
	}

}
