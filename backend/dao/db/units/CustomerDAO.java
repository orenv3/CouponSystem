package dao.db.units;

import java.util.Collection;

import beans.coupon.system.Coupon;
import beans.coupon.system.Customer;

/**
 * This class describe a DAO interface of Customer. There are C.R.U.D methods:
 * CREATE, READ, UPDATE, DELETE.
 * 
 * @author Vinogura Oren
 *
 */
interface CustomerDAO {

	void create(Customer customerName);

	void delete(Customer customerName);

	void update(Customer customerName);

	Customer read(long custId);

	Collection<Customer> readAllCustomers();

	Collection<Coupon> readAllCoupons(long id);

	boolean login(String custName, String password);

	Customer readByName(String custName);

}
