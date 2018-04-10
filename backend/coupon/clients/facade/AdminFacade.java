package coupon.clients.facade;

import java.util.Collection;

import beans.coupon.system.Company;
import beans.coupon.system.Coupon;
import beans.coupon.system.Customer;
import dao.db.units.CompanyDB_DAO;
import dao.db.units.CouponDB_DAO;
import dao.db.units.CustomerDB_DAO;
import general.Clients;
import general.JoinTables;
import general.StaticQueries;
import myExceptions.MyCompanyExceptions;
import myExceptions.MyCouponException;
import myExceptions.MyCustomerException;

public class AdminFacade implements CouponClientFacade {

	public AdminFacade() {
	}

	/**
	 * This method is designed to find a coupon's owner(company) via the
	 * coupon's id. The method receives a coupon and finds the company that owns
	 * this coupon.
	 * 
	 * NOTE: This method will return EXCEPTION if there is no owner(list
	 * size=0).
	 * 
	 * @param coupon
	 *            method is searching for this coupon owner.
	 * @return Collection list of only one Company that owns this coupon.
	 */
	public Collection<Company> WhoIs_couponOwner(Coupon coupon) {
		return StaticQueries.getObjectsFromJoin(JoinTables.COMPANY_COUPON, coupon);
	}

	/**
	 * This method is designed to find who are the customers that purchased
	 * specific coupon via the coupon's id. The method receives a coupon and
	 * finds all the customers that purchased this coupon.
	 * 
	 * NOTE: This method will return EXCEPTION if there is no owner(list
	 * size=0).
	 * 
	 * @param coupon
	 *            method is searching for this coupon buyers.
	 * @return Collection list of all the customers that purchased this coupon.
	 */
	public Collection<Customer> WhoIs_purchasedTheCoupon(Coupon coupon) {
		return StaticQueries.getObjectsFromJoin(JoinTables.CUSTOMER_COUPON, coupon);
	}

	/**
	 * This method creates company object in the DB's company table. There must
	 * not be name duplication in this table. If the name of the company already
	 * exists the method throws exception.
	 * 
	 * @param company
	 *            object to create on DB.
	 */
	public void createCompany(Company company) {
		CompanyDB_DAO compDAO = new CompanyDB_DAO();
		if (StaticQueries.checkNameByUnits(Clients.COMPANY, company, true))
			compDAO.create(company);
	}

	/**
	 * The method deletes company object from the system. In order to delete
	 * company from the system there are four DB's tables to perform a deletion.
	 * The method removes the company from 'company table' and all the company's
	 * coupons either from 'company_coupon' join table and 'coupon' DB's tables.
	 * in addition, the method deletes the company's coupons also from
	 * customer_coupon join table. all the customers that use this company's
	 * coupons, wiil not be able to use them anymore. IMPORTANT: if the company
	 * is new, so the company still do not have coupons. Therefore, the method
	 * will delete the company ONLY from the DB's company table and will throw
	 * EXCEPTION after it.
	 * 
	 * @param company
	 *            the company to delete from the system.
	 */
	public void removeCompany(Company company) {
		if (StaticQueries.checkNameByUnits(Clients.COMPANY, company, false)) {
			CompanyDB_DAO comp = new CompanyDB_DAO();
			CouponDB_DAO cupn = new CouponDB_DAO();
			comp.delete(company);
			Collection<Coupon> l = comp.readAllCoupons(company.getId());
			for (Coupon coupon : l) {
				StaticQueries.Obj_Action_JoinTable(JoinTables.COMPANY_COUPON, company, coupon, false);
				cupn.delete(coupon);
			}
			StaticQueries.deleteListFromJoin(JoinTables.CUSTOMER_COUPON, l);
		}
	}

	/**
	 * The method is updating company object into DB's company table.
	 * 
	 * @param company
	 *            updated company object to update
	 */
	public void updateCompany(Company company) {
		if (StaticQueries.checkNameByUnits(Clients.COMPANY, company, false)) {
			CompanyDB_DAO compDAO = new CompanyDB_DAO();
			compDAO.update(company);
		}
	}

	/**
	 * The method provides a company object with all his details(including
	 * company coupon's list).
	 * 
	 * @param id
	 *            The method provide the company object refering to this company
	 *            id number.
	 * @return Company object with full details.
	 */
	public Company getCompany(long id) {
		CompanyDB_DAO c = new CompanyDB_DAO();
		CompanyFacade cF = new CompanyFacade();
		Company comp = c.read(id);
		cF.setCompany(comp);
		return cF.showMyCompany();
	}

	/**
	 * The method provide a list of all the companies(company objects) that
	 * exist in the DB's company table.
	 * 
	 * @return Collection list of companies
	 */
	public Collection<Company> getAllCompanies() {
		CompanyDB_DAO compDAO = new CompanyDB_DAO();
		Collection<Company> l = compDAO.readAllCompanies();
		if (l.size() == 0)
			throw new MyCompanyExceptions("No company was found in the database");
		return l;
	}

	/**
	 * This method creates customer object on DB's customer table. There must
	 * not be name duplication in this table. If the name of the customer
	 * already exists the method throws EXCEPTION.
	 * 
	 * @param customer
	 *            object to create in the DB.
	 */
	public void createCustomer(Customer customer) {
		CustomerDB_DAO custDAO = new CustomerDB_DAO();
		if (StaticQueries.checkNameByUnits(Clients.CUSTOMER, customer, true))
			custDAO.create(customer);
	}

	/**
	 * The method deletes customer object from the system. In order to delete a
	 * customer, the method removes the customer from 'customer table' and all
	 * the customer's coupons(purchase history) from 'customer_coupon' join
	 * table.
	 * 
	 * @param customer
	 *            customer object to remove from DB.
	 */
	public void removeCustomer(Customer customer) {
		CustomerDB_DAO custDAO = new CustomerDB_DAO();
		Customer c = custDAO.read(customer.getId());
		if (c.getName().equals(customer.getName()) && c.getId() == customer.getId()) {
			custDAO.delete(customer);
			Collection<Coupon> list = custDAO.readAllCoupons(customer.getId());
			for (Coupon coupon : list) {
				StaticQueries.Obj_Action_JoinTable(JoinTables.CUSTOMER_COUPON, customer, coupon, false);
			}
		}

	}

	/**
	 * The method deletes coupon object from the customer's purchased coupon
	 * list. The method removes the coupon from 'customer_coupon table' and
	 * increases the amount of the coupon in coupon table.
	 * 
	 * @param coupon
	 *            The coupon to delete.
	 * @param custID
	 *            Customer ID that would like to delete the coupon.
	 */
	public void removeCustomerCoupon(Customer customer, Coupon coupon) {
		CustomerDB_DAO DAOcust = new CustomerDB_DAO();
		CouponDB_DAO DAOcoupon = new CouponDB_DAO();
		Collection<Coupon> custList = DAOcust.readAllCoupons(customer.getId());
		if (StaticQueries.checkNameByUnits(Clients.COUPON, coupon, false)) {
			for (Coupon coup2 : custList) {
				if (coup2.getId() == coupon.getId() && coup2.getTitle().equals(coupon.getTitle())) {
					StaticQueries.Obj_Action_JoinTable(JoinTables.CUSTOMER_COUPON, customer, coupon, false);
					;
					this.increaseObjAmount(coupon);
					DAOcoupon.update(coupon);
					return;
				}
			}
		} else
			throw new MyCouponException(
					"There is no such company's coupon in the database.\nPlease contact the helpdesk.");
	}

	/**
	 * The method is updating customer in the database.
	 * 
	 * @param customer
	 *            updated customer object to update.
	 */
	public void updateCustomer(Customer customer) {
		if (StaticQueries.checkNameByUnits(Clients.CUSTOMER, customer, false)) {
			CustomerDB_DAO custDAO = new CustomerDB_DAO();
			custDAO.update(customer);
		}
	}

	/**
	 * The method provide a customer object with all his details (including
	 * customer coupon's list).
	 * 
	 * @param id
	 *            The method provide the customer object refering to this
	 *            customer id number.
	 * @return Customer object with full details.
	 */
	public Customer getCustomer(long id) {
		CustomerDB_DAO c = new CustomerDB_DAO();
		CustomerFacade cF = new CustomerFacade();
		Customer cust = c.read(id);
		cF.setCustomer(cust);
		return cF.showCustomerProfile();

	}

	/**
	 * The method provide a list of all the customers(customer objects) that
	 * exist in the DB's customer table.
	 *
	 * @return Collection list of all customers.
	 */
	public Collection<Customer> getAllCustomers() {
		CustomerDB_DAO custDAO = new CustomerDB_DAO();
		Collection<Customer> l = custDAO.readAllCustomers();
		if (l.size() == 0)
			throw new MyCustomerException("No customer was found in the database");
		return l;
	}

	/**
	 * This method sets company's password and updating it in the DB's company
	 * table.
	 * 
	 * @param company
	 *            company object before the update.
	 * @param pass
	 *            new password to set in the object.
	 */
	public void setCompanyPassword(Company company, String pass) {
		CompanyDB_DAO c = new CompanyDB_DAO();
		company.setPassword(pass);
		c.update(company);
	}

	/**
	 * This method sets company's email and updating it in the DB's company
	 * table.
	 * 
	 * @param company
	 *            company object before the update.
	 * @param email
	 *            new email to set in the object.
	 */
	public void setCompanyEmail(Company company, String email) {
		CompanyDB_DAO c = new CompanyDB_DAO();
		company.setEmail(email);
		c.update(company);

	}

	/**
	 * Log-in method. Via this method admin can enter the coupon system. The
	 * method checks whether the name and password are in the DB. However, in
	 * admin case the user and password are hard-coded.
	 * 
	 * @param name
	 *            always "admin".
	 * @param password
	 *            always "1234".
	 */
	@Override
	public CouponClientFacade login(String name, String password, Clients client) {

		if (client.equals(Clients.ADMIN))
			if (name.equals("admin") && password.equals("1234"))
				return this;

		return null;

	}

	private void increaseObjAmount(Coupon coupon) {
		int present = coupon.getAmount();
		coupon.setAmount(++present);
	}

}
