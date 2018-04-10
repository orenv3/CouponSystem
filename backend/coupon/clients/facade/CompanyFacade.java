package coupon.clients.facade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import beans.coupon.system.Company;
import beans.coupon.system.Coupon;
import beans.coupon.system.CouponTypeComparator;
import connectionPool.ConnectionPool;
import dao.db.units.CompanyDB_DAO;
import dao.db.units.CouponDB_DAO;
import general.Clients;
import general.JoinTables;
import general.StaticQueries;
import myExceptions.MyCompanyExceptions;
import myExceptions.MyCouponException;

public class CompanyFacade implements CouponClientFacade {

	private Company company;
	private CompanyDB_DAO compDAO = new CompanyDB_DAO();

	public CompanyFacade() {
	}

	/**
	 * This method creates new coupon to current loged-in company(this.company).
	 * Coupon title is unique in the system. Therefor, prior to coupon's
	 * creation this method is checking whethere there is such coupon title or
	 * not. In addition to the information above, this method with synchronized
	 * block to prevent exceptions when there is more than one request to the
	 * same name in the same time. NOTE: This method throws EXCEPTION! The
	 * coupon amount must be bigger then zero.
	 * 
	 * @param coupon
	 *            A coupon object to create.
	 */
	public void createCoupon(Coupon coupon) {
		//sync to prevent duplications 
		synchronized (this.company) {
			CouponDB_DAO couponDAO = new CouponDB_DAO();
			if (StaticQueries.checkNameByUnits(Clients.COUPON, coupon, true)) {
				if (StaticQueries.checkCouponObjAmount(coupon)) {
					couponDAO.create(coupon);
					StaticQueries.Obj_Action_JoinTable(JoinTables.COMPANY_COUPON, this.company, coupon, true);
				} else
					throw new MyCouponException(
							"The amount of the coupon is 0, coupon amaount must be bigger then 1.\nSelling this coupon is invalid."
									+ "\nPlease contact the helpdesk. ");
			}

		}

	}

	/**
	 * This method removes a company's coupon from the database. Prior to the
	 * deletion of the coupon, the method checks if the coupon is existing in
	 * the DB. This method deletes the coupon from three tables: Coupon,
	 * Customer_coupon and Company_coupon.
	 * 
	 * @param coupon
	 *            the coupon to delete.
	 */
	public void removeCoupon(Coupon coupon) {
		CouponDB_DAO DAOcoupon = new CouponDB_DAO();
		Collection<Coupon> companyList = compDAO.readAllCoupons(this.company.getId());
		if (StaticQueries.checkNameByUnits(Clients.COUPON, coupon, false)) {
			for (Coupon coup2 : companyList) {
				if (coup2.getId() == coupon.getId() && coup2.getTitle().equals(coupon.getTitle())) {
					StaticQueries.Obj_Action_JoinTable(JoinTables.COMPANY_COUPON, this.company, coupon, false);
					Collection<Coupon> list = new LinkedList<>();
					list.add(coupon); //add the coupon to Collection list in order to use the method below.
					StaticQueries.deleteListFromJoin(JoinTables.CUSTOMER_COUPON, list);
					DAOcoupon.delete(coupon);
					return;
				}
			}
		} else
			throw new MyCouponException(
					"There is no such company's coupon in the database.\nPlease contact the helpdesk.");
	}

	/**
	 * This method shows all details(including list of company's coupon) of the
	 * current loged-in company.
	 * 
	 * @return Company's object with full details.
	 */
	public Company showMyCompany() {
		Collection<Coupon> l = this.getAllCoupons();
		this.company.setCoupons(l);
		return this.company;
	}

	/**
	 * This method provide a coupon object according to the coupon ID. This
	 * coupon must be owned by the current loged-in company.
	 * 
	 * @param couponID
	 *            The coupon's ID number.
	 * @return Coupon object with full details.
	 */
	public Coupon getCoupon(long couponID) {
		Collection<Coupon> list = getAllCoupons();
		for (Coupon coupon : list) {
			if (coupon.getId() == couponID) {
				return coupon;
			}
		}
		//if the method reached this point, so the company does not own this coupon or other mistakes.
		throw new MyCouponException("The company: " + this.company.getName() + "does not own this coupon. ");

	}

	/**
	 * This method creates a coupons list(objects). All those coupons are
	 * belongs to the loged-in company (The query is from the company_coupon
	 * table).
	 * 
	 * @return Collection list of Coupons (objects).
	 */
	public Collection<Coupon> getAllCoupons() {
		return compDAO.readAllCoupons(this.company.getId());
	}

	/**
	 * This method creates a coupons list of the current loged-in company. This
	 * list is sorted by type via sql query "ORDER BY" in the DB.
	 * 
	 * @return Collection list of coupons sorted by type.
	 */
	public Collection<Coupon> getCouponByType() {
		Collection<Coupon> list = new LinkedList<>();
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		String sql = "SELECT ID,Title,START_DATE,END_DATE,AMOUNT,TYPE,MESSAGE,PRICE,IMAGE FROM Company_Coupon RIGHT JOIN Coupon "
				+ "ON Company_Coupon.COUPON_ID = Coupon.ID WHERE COMP_ID=" + this.company.getId() + " ORDER BY TYPE";
		try (Statement statmnt = con.createStatement();) {
			ResultSet rst = statmnt.executeQuery(sql);
			StaticQueries.couponResultSet(rst, list);
			if (list.size() == 0) {
				throw new MyCompanyExceptions("There are no coupon for the company number: " + this.company.getId());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
		return list;

	}

	/**
	 * This method creates a coupons list of the current loged-in company. This
	 * list is sorted by type via CouponTypeComparator(Beans package).
	 * 
	 * @return Collection list of coupons sorted by type.
	 */
	public Collection<Coupon> getComparbleCouponByType() {
		LinkedList<Coupon> list = (LinkedList<Coupon>) compDAO.readAllCoupons(this.company.getId());
		Collections.sort(list, new CouponTypeComparator());
		return list;

	}

	/**
	 * This method creates a coupons list of the current loged-in company. This
	 * list is sorted by price via CouponPriceComparator(Beans package), price:
	 * low-given max price.
	 * 
	 * @param maxP
	 *            maximum price the list will reach.
	 * @return Collection list of coupons sorted by price till the given max
	 *         price.
	 */
	public Collection<Coupon> getCouponByMaxPrice(double maxP) {
		return StaticQueries.getCouponsByMaxPrice(maxP, compDAO.readAllCoupons(this.company.getId()));
	}

	/**
	 * This method creates a sort list of coupons ordered by coupons's end date.
	 * The list will end when it reach the max end date that given. Those
	 * coupons in the list are belong to the current loged-in company.
	 * 
	 * @param maxDate
	 *            This is the farthest end date that the list will reach.
	 * @return Collection sorted list by date. Date: old till the given maximum
	 *         date.
	 */
	public Collection<Coupon> getCouponByDate(Date maxDate) {
		return StaticQueries.getCouponTillMaxDate(maxDate, compDAO.readAllCoupons(this.company.getId()));
	}

	/**
	 * This method updates a given updated coupon in the database.
	 * 
	 * @param coupon
	 *            updated coupon to update in DB.
	 */
	public void updateCoupon(Coupon coupon) {
		CouponDB_DAO c = new CouponDB_DAO();
		if (StaticQueries.checkNameByUnits(Clients.COUPON, coupon, false)) {
			Collection<Company> comp = StaticQueries.getObjectsFromJoin(JoinTables.COMPANY_COUPON, coupon);
			for (Company MyCompany : comp) {//have to be one company in comp list // just checking
				if (MyCompany.getId() == this.company.getId() && comp.size() == 1) {
					if (MyCompany.getName().equals(this.company.getName()) && comp.size() == 1)
						c.update(coupon);
					else
						throw new MyCompanyExceptions(
								"There is a mismatch in company's names.\nPlease contact the support for a solution.");
				} else
					throw new MyCompanyExceptions(
							"There is a mismatch in company's id!!.\nPlease contact the support for a solution.");

			}
		}
	}

	/**
	 * This method setting the coupon validity (setting a new end date for the
	 * coupon). In addition, the method updates the coupon in DB with the new
	 * parameters.
	 * 
	 * @param coupon
	 *            The coupon before the date setting, and the update.
	 * @param year
	 *            The new year to configure in the coupon. The pattern is: 2017
	 * @param month
	 *            The new month to configure in the coupon. The pattern of
	 *            "month" is 1 for January till 12 for December.
	 * @param day
	 *            The new day to configure in the coupon. The pattern of "day"
	 *            is 1 to 31 according to the month(for example in FEB there are
	 *            only 28 days).
	 */
	public void updateEndDate(Coupon coupon, int year, int month, int day) {
		if (year >= 2017 && month <= 12 && month >= 1 && day <= 31 && day >= 1) {
			Calendar cal = Calendar.getInstance();
			--month;
			cal.set(year, month, day);
			java.util.Date endDate = cal.getTime();
			coupon.setEndDate(new java.sql.Date(endDate.getTime()));
			this.updateCoupon(coupon);

		} else
			throw new MyCouponException("The date is invalid");
	}

	/**
	 * This method setting a new coupon price. In addition, the method updates
	 * the coupon in DB with the new parameters.
	 * 
	 * @param coupon
	 *            The coupon before the price setting and the update.
	 * @param newPrice
	 *            The new price to configure in the coupon.
	 */
	public void updatePrice(Coupon coupon, double newPrice) {
		coupon.setPrice(newPrice);
		this.updateCoupon(coupon);
	}

	/**
	 * Log-in method. Via this method company can enter the coupon system. The
	 * method checks whether the name and password that given are correct via
	 * the DB.
	 * 
	 * @param name
	 *            The name of the current company (log-in information).
	 * @param password
	 *            The password of the current company (log-in information).
	 * @return A CompanyFacade if the information is valid (after setting the
	 *         current loged-in company to Facade.company). if the information
	 *         is invalid the method returns null.
	 */
	public CouponClientFacade login(String name, String password, Clients unit) {
		if (unit.equals(Clients.COMPANY))
			if (compDAO.login(name, password)) {
				Company comp = compDAO.readByName(name);
					this.setCompany(comp);
				return this;
				}
		return null;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
