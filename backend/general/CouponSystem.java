package general;

import coupon.clients.facade.AdminFacade;
import coupon.clients.facade.CompanyFacade;
import coupon.clients.facade.CouponClientFacade;
import coupon.clients.facade.CustomerFacade;

/**
 * Singleton of the coupon system. This class that represents the whole coupon
 * system.
 * 
 * @author oren vinogura
 *
 */
public class CouponSystem {

	private static CouponSystem instance;

	private CouponSystem() {
	}

	/**
	 * get a singelton of the coupon system.
	 * 
	 * @return instance of Coupon System.
	 */
	public synchronized static CouponSystem getInstance() {
		if (instance == null) {
			instance = new CouponSystem();
		}
		return instance;

	}

	/**
	 * login to coupon system. This method checks whether the user is in the
	 * system or not.
	 * 
	 * @param name
	 *            name to check in the database.
	 * @param password
	 *            password of the current user name to check in the database.
	 * @param facade
	 *            This is Client ENUM - have to be CUSTOMER, COMPANY or ADMIN.
	 * @return CouponClientFacade. The method returns facade client object as a
	 *         CouponClientFacade interface.
	 */
	public CouponClientFacade login(String name, String password, Clients facade) {

		switch (facade) {
		case ADMIN:
			AdminFacade admin = new AdminFacade();
			return admin.login(name, password, Clients.ADMIN);

		case COMPANY:
			CompanyFacade comp = new CompanyFacade();
			return comp.login(name, password, Clients.COMPANY);

		case CUSTOMER:
			CustomerFacade cust = new CustomerFacade();
			return cust.login(name, password, Clients.CUSTOMER);
		default:
			break;
		}
		return null;
	}

	/**
	 * This method shutting down the coupon system. First, it stops the daily
	 * task of deleting invalid coupons and after that closes all the
	 * connections via "SoftCloseConnection" singleton's method.
	 */
}
