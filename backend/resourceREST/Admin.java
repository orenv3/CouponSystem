package resourceREST;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import beans.coupon.system.Company;
import beans.coupon.system.Coupon;
import beans.coupon.system.Customer;
import coupon.clients.facade.AdminFacade;
import dao.db.units.CouponDB_DAO;
import dao.db.units.CustomerDB_DAO;
import general.JoinTables;
import general.StaticQueries;

@CrossOrigin(origins = "*")
@RequestMapping(value = "admin")
@RestController
public class Admin {

	private LogController logger = new LogController();

	private AdminFacade facade(HttpServletRequest request, HttpServletResponse response) {
		return (AdminFacade) request.getSession().getAttribute("facade");
	}


	@RequestMapping(value = "/createCompany", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createCompany(@RequestBody Company company, HttpServletRequest request, HttpServletResponse response) {
		AdminFacade ADMIN = facade(request, response);
		logger.debug("user ADMIN would like to create company."
				+ " The fileds might be incomplete, view the following in order to check it: " + company);
		ADMIN.createCompany(company);
		if (response.getStatus() == 200) {
			logger.info("user: ADMIN created: " + company + " successfully");
		}
	}


	@RequestMapping(value = "/removeCompany", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void removeCompany(@RequestBody Company company, HttpServletRequest request, HttpServletResponse response) {
		AdminFacade ADMIN = facade(request, response);
		logger.debug("user ADMIN would like to delete company."
				+ " The fileds might be incomplete, view the following in order to check it: " + company);
		ADMIN.removeCompany(company);
		if (response.getStatus() == 200) {
			logger.info("user: ADMIN deleted: " + company + " successfully");
		}
	}


	@RequestMapping(value = "/updateCompany", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateCompany(@RequestBody Company company, HttpServletRequest request, HttpServletResponse response) {
		AdminFacade ADMIN = facade(request, response);
		logger.debug(
				"user ADMIN would like to update company."
						+ " The fileds might be incomplete, view the following in order to check it: " + company);
		ADMIN.updateCompany(company);
		if (response.getStatus() == 200) {
			logger.info("user: ADMIN updated company to: " + company + " successfully");
		}
	}

	@RequestMapping(value = "/getCompany/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Company getCompany(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) {
		AdminFacade ADMIN = facade(request, response);
		logger.debug("The user ADMIN fetched a company details by id."
				+ " The entered id might be either from the database or not. The entered id is:[ " + id + " ]");
		return ADMIN.getCompany(id);
	}

	@RequestMapping(value = "/getAllCompanies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Company> getAllCompanies(HttpServletRequest request, HttpServletResponse response) {
		AdminFacade ADMIN = facade(request, response);
		logger.info("User ADMIN use method getAllCompanies().");
		return ADMIN.getAllCompanies();
	}


	//////////////////
	@RequestMapping(value = "/createCustomer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createCustomer(@RequestBody Customer customer, HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("user ADMIN would like to create customer."
				+ " The fileds might be incomplete, view the following in order to check it: " + customer);
		AdminFacade ADMIN = facade(request, response);
		ADMIN.createCustomer(customer);
		if (response.getStatus() == 200) {
			logger.info("user: ADMIN created: " + customer + " successfully");
		}
	}

	@RequestMapping(value = "/removeCustomer", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void removeCustomer(@RequestBody Customer customer, HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("user ADMIN would like to delete customer."
				+ " The fileds might be incomplete, view the following in order to check it: " + customer);
		AdminFacade ADMIN = facade(request, response);
		ADMIN.removeCustomer(customer);
		if (response.getStatus() == 200) {
			logger.info("user: ADMIN deleted: " + customer + " successfully");
		}
	}

	@RequestMapping(value = "/removeCustomerCoupon/{customerID}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void removeCustomerCoupon(@RequestBody Coupon coupon, @PathVariable long customerID,
			HttpServletRequest request, HttpServletResponse response) {
		AdminFacade ADMIN = facade(request, response);
		CouponDB_DAO dao = new CouponDB_DAO();
		CustomerDB_DAO cust = new CustomerDB_DAO();
		Customer customer = cust.read(customerID);
		Coupon coupon2 = dao.read(coupon.getId());
		logger.debug("ADMIN would like to delete to " + customer + " the following " + coupon);
		ADMIN.removeCustomerCoupon(customer, coupon2);
		logger.info(coupon.getId() + " deleted successfully by ADMIN");
	}


	@RequestMapping(value = "/updateCustomer", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateCustomer(@RequestBody Customer customer, HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("user ADMIN would like to update customer."
				+ " The fileds might be incomplete, view the following in order to check it: " + customer);
		AdminFacade ADMIN = facade(request, response);
		ADMIN.updateCustomer(customer);
		logger.info("user: ADMIN uptaded: " + customer + " successfully");
	}

	@RequestMapping(value = "/getCustomer/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Customer getCustomer(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("The user ADMIN fetched a customer details by id."
				+ " The entered id might be either from the database or not. The entered id is:[ " + id + " ]");
		AdminFacade ADMIN = facade(request, response);
		return ADMIN.getCustomer(id);
	}

	@RequestMapping(value = "/getAllCustomers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Customer> getAllCustomers(HttpServletRequest request, HttpServletResponse response) {
		logger.info("User ADMIN use method getAllCustomers().");
		AdminFacade ADMIN = facade(request, response);
		return ADMIN.getAllCustomers();
	}

	@RequestMapping(value = "/WhoIs_couponOwner/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Company> WhoIs_couponOwner(@PathVariable("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("User ADMIN use the method WhoIs_couponOwner(id: " + id + ")");
		AdminFacade ADMIN = facade(request, response);
		CouponDB_DAO dao = new CouponDB_DAO();
		Coupon coupon = dao.read(id);
		logger.info("WhoIs_couponOwner() contains the following: " + coupon);
		return StaticQueries.getObjectsFromJoin(JoinTables.COMPANY_COUPON, coupon);
	}

	@RequestMapping(value = "/WhoIs_purchasedTheCoupon/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Customer> WhoIs_purchasedTheCoupon(@PathVariable("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("User ADMIN use the method WhoIs_purchasedTheCoupon(id: " + id + ")");
		AdminFacade ADMIN = facade(request, response);
		CouponDB_DAO dao = new CouponDB_DAO();
		Coupon coupon = dao.read(id);
		logger.info("WhoIs_purchasedTheCoupon() contains customers that own the following: " + coupon);

		return StaticQueries.getObjectsFromJoin(JoinTables.CUSTOMER_COUPON, coupon);
	}

}
