package coupon.clients.facade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import beans.coupon.system.Coupon;
import beans.coupon.system.CouponPriceComparator;
import beans.coupon.system.CouponTypeComparator;
import beans.coupon.system.Customer;
import connectionPool.ConnectionPool;
import dao.db.units.CouponDB_DAO;
import dao.db.units.CustomerDB_DAO;
import general.Clients;
import general.JoinTables;
import general.StaticQueries;
import myExceptions.MyCouponException;
import myExceptions.MyCustomerException;

public class CustomerFacade implements CouponClientFacade {
	private Customer customer;
	private CustomerDB_DAO custDAO = new CustomerDB_DAO();

	/**
	 * This method implements customer purchase (only for current loged-in
	 * customer). prior the purchase, the method checkes the following: \\*1* if
	 * the coupon exists in the coupon table(DB). \\*2* if this customer
	 * purchased this coupon already. \\*3* if the date of the coupon is valid.
	 * \\*4* if the amount of this coupon is bigger then 0.
	 * 
	 * @param coupon
	 *            The coupon to purchase.
	 */
	public void purchaseCoupon(Coupon coupon) {
		Calendar cal = Calendar.getInstance();
		java.sql.Date today = new java.sql.Date(cal.getTimeInMillis());
		//in the below: checking whether the customer purchased this coupon already.
		if (StaticQueries.checkNameByUnits(Clients.COUPON, coupon, false)) {
			Collection<Coupon> list = custDAO.readAllCoupons(this.customer.getId());
			for (Coupon coup2 : list) {//checking if the customer have this coupon already.
				if (coup2.getTitle().equals(coupon.getTitle()))
					throw new MyCustomerException("You can only purchase one coupon of each type.");
			}
		} //in the below: checking whether the coupon's date is valid.
		if (coupon.getEndDate().equals(today) || coupon.getEndDate().before(today))
			throw new MyCouponException("This coupon date is expird.\nYou can purchase only valid coupon.");
		synchronized (this.customer) {
			//SYNC BLOCK!!!
			//to prevent amount ERRORS
			if (StaticQueries.checkCouponObjAmount(coupon)) {
				StaticQueries.Obj_Action_JoinTable(JoinTables.CUSTOMER_COUPON, this.customer, coupon, true);
				CouponDB_DAO DAOcoupon = new CouponDB_DAO();
				decreaseObjAmount(coupon);
				DAOcoupon.update(coupon);
			} else
				throw new MyCouponException("Sorry, Coupon inventory sold out!");
		}
	}

	/**
	 * This method creates a coupons list of the loged-in customer (purchase
	 * history).
	 * 
	 * @return Collection list of coupons(objects)
	 */
	public Collection<Coupon> getAllpurchaseCoupons() {
		return custDAO.readAllCoupons(this.customer.getId());
	}

	/**
	 * This method creates a coupons list of the loged-in customer (purchase
	 * history). The list sorted by type via beans.CouponTypeComparator
	 * according to String.compateTo.
	 * 
	 * @return Collection list of coupons ordered by type.
	 */
	public LinkedList<Coupon> getAllpurchaseCouponsByType() {
		LinkedList<Coupon> list = (LinkedList<Coupon>) custDAO.readAllCoupons(this.customer.getId());
		Collections.sort(list, new CouponTypeComparator());
		return list;
	}

	/**
	 * This method creates a coupons list of the loged-in customer (purchase
	 * history). The list sorted by price via beans.CouponPriceComparator -
	 * price: low-high.
	 * 
	 * @return Collection list of coupons orderd by price.
	 */
	public LinkedList<Coupon> getAllpurchaseCouponsByPrice() {
		LinkedList<Coupon> list = (LinkedList<Coupon>) custDAO.readAllCoupons(this.customer.getId());
		Collections.sort(list, new CouponPriceComparator());
		return list;
	}

	/**
	 * This method creates a coupons list of the loged-in customer (purchase
	 * history). The list sorted by price via beans.CouponPriceComparator -
	 * price: low-high. Howevere, the list stops when it reaches the given
	 * maximum price.
	 * 
	 * @param maxPrice
	 *            This is the maximum price the list will reach.
	 * @return Collection list of coupons ordered by price till the given
	 *         maximum price.
	 */
	public LinkedList<Coupon> getAllpurchaseCouponsByMaxPrice(double maxPrice) {
		return StaticQueries.getCouponsByMaxPrice(maxPrice, custDAO.readAllCoupons(this.customer.getId()));
	}

	/**
	 * This method creates a coupons list of the loged-in customer (purchase
	 * history). The list sorted by date via beans.CouponDateComperator - date:
	 * old-new. Howevere, the list stops when it reaches the given maximum date.
	 * 
	 * @param maxDate
	 *            This is the newest and the lastest date the list will reach.
	 * @return Collection list of coupons ordered by date till the given date.
	 */
	public Collection<Coupon> getCouponsTillDate(Date maxDate) {
		return StaticQueries.getCouponTillMaxDate(maxDate, custDAO.readAllCoupons(this.customer.getId()));
	}

	/**
	 * This method shows all customer's details(including purchase history) of
	 * the current loged-in customer.
	 * 
	 * @return Customer's object with full details.
	 */
	public Customer showCustomerProfile() {
		Collection<Coupon> l = this.getAllpurchaseCoupons();
		this.customer.setCoupons(l);
		return customer;
	}

	public Collection<Coupon> readAllCouponsForSelection() {
		Collection<Coupon> purchasedCoupons = custDAO.readAllCoupons(this.customer.getId());
		return custDAO.readAllCouponsForPresentasion();
	}

	/**
	 * Log-in method. Via this method customer can enter the coupon system. The
	 * method checks whether the name and password that given are correct via
	 * the DB.
	 * 
	 * @param name
	 *            The name of the current customer (log-in information).
	 * @param password
	 *            The password of the current customer (log-in information).
	 * 
	 * @return A CustomerFacade if the information is valid (after setting the
	 *         current loged-in customer to Facade.customer). if the information
	 *         is invalid the method returns null.
	 */
	@Override
	public CouponClientFacade login(String name, String password, Clients client) {
		if (client.equals(Clients.CUSTOMER))
			if (custDAO.login(name, password)) {
				ConnectionPool pool = ConnectionPool.getInstance();
				Connection con = null;
				ResultSet rst = null;
				con = pool.getConnection();
				Customer cust = new Customer();
				String sql = "SELECT * FROM Customer WHERE CUST_NAME=?";
				try (PreparedStatement statmnt = con.prepareStatement(sql);) {
					statmnt.setString(1, name);
					rst = statmnt.executeQuery();
					while (rst.next()) {
						cust.setId(rst.getLong("ID"));
						cust.setName(rst.getString("CUST_NAME"));
						cust.setPassword(rst.getString("PASSWORD"));
					}
					this.setCustomer(cust);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						rst.close();
						pool.returnConnection(con);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return this;
			}
		return null;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	//*********************** private methods *************************

	/**
	 * This method decreasing the amaount of the coupon Object.
	 * 
	 * @param coupon
	 *            coupon object before the update in order to decrease his
	 *            amount in 1;
	 */
	private void decreaseObjAmount(Coupon coupon) {
		int present = coupon.getAmount();
		coupon.setAmount(--present);
	}
}
