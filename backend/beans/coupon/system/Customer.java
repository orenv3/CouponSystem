package beans.coupon.system;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class describe a Customer class(Beans) Class Modifiers, getters setters
 * and toString
 * 
 * @author oren vinogura
 *
 */
@XmlRootElement
public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String password;
	private Collection<Coupon> custCoupons = new LinkedList<Coupon>();

	public Customer() {
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

	public void setName(String custName) {
		this.name = custName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Coupon> getCoupons() {
		return custCoupons;
	}

	public void setCoupons(Collection<Coupon> custCoupons) {
		this.custCoupons = custCoupons;
	}

	@Override
	public String toString() {
		return "Customer [customer id=" + id + ", custumer name=" + name + ", password=" + password
				+ " \ncustumer Coupons: " + custCoupons + "]";
	}

}
