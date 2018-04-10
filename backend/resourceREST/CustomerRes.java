package resourceREST;

import java.sql.Date;
import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import beans.coupon.system.Coupon;
import beans.coupon.system.Customer;
import coupon.clients.facade.CustomerFacade;
import dao.db.units.CustomerDB_DAO;
import general.StaticQueries;

@RestController
@RequestMapping(value = "customerRes")
@CrossOrigin(origins = "*")
public class CustomerRes {


	private LogController logger = new LogController();
	
	private CustomerFacade facade(HttpServletRequest request, HttpServletResponse response) {
		return (CustomerFacade) request.getSession().getAttribute("facade");
	}
	

	@RequestMapping(value = "/purchaseCoupon/{startDate}/{endDate}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void purchaseCoupon(@RequestBody Coupon coupon, @PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate, HttpServletRequest request, HttpServletResponse response) {

		CustomerFacade current = facade(request, response);

		// convert string to util.Date
		java.sql.Date sqlEndDate = java.sql.Date.valueOf(endDate);
		java.util.Date javaEndDate = new Date(sqlEndDate.getTime());

		java.sql.Date sqlStartDate = java.sql.Date.valueOf(startDate);
		java.util.Date javaStartDate = new Date(sqlStartDate.getTime());

		coupon.setEndDate(javaEndDate);
		coupon.setStartDate(javaStartDate);

		logger.info(current.showCustomerProfile().getName() + " would like to purchase the coupon: " + coupon);
		current.purchaseCoupon(coupon);
		logger.debug("The " + coupon.getTitle() + " successfully purchased");
	}
	
	@RequestMapping(value="/getAllpurchaseCoupons", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Coupon> getAllpurchaseCoupons(HttpServletRequest request, HttpServletResponse response) {
		CustomerFacade current = facade(request, response);
		logger.debug("getAllpurchaseCoupons() method implemented by: " + current.showCustomerProfile().getName());
		return current.getAllpurchaseCoupons();
	}

	
	@RequestMapping(value="/getAllpurchaseCouponsByType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public LinkedList<Coupon> getAllpurchaseCouponsByType(HttpServletRequest request, HttpServletResponse response) {
		CustomerFacade current = facade(request, response);
		logger.debug("getAllpurchaseCouponsByType() method implemented by: " + current.showCustomerProfile().getName());
		return current.getAllpurchaseCouponsByType();
	}
	
	
	@RequestMapping(value="/getAllpurchaseCouponsByPrice", method= RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE )
	public LinkedList<Coupon> getAllpurchaseCouponsByPrice(HttpServletRequest request, HttpServletResponse response) {
		CustomerFacade current = facade(request, response);
		logger.debug(
				"getAllpurchaseCouponsByPrice() method implemented by: " + current.showCustomerProfile().getName());
		return current.getAllpurchaseCouponsByPrice();
	}
	
	
	@RequestMapping(value="/getAllpurchaseCouponsByMaxPrice/{maxPrice}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public LinkedList<Coupon> getAllpurchaseCouponsByMaxPrice(@PathVariable("maxPrice") double maxPrice,
			HttpServletRequest request, HttpServletResponse response) {
		CustomerFacade current = facade(request, response);
		logger.debug("getAllpurchaseCouponsByMaxPrice(price: '" + maxPrice + "') method implemented by: "
				+ current.showCustomerProfile().getName());
		return current.getAllpurchaseCouponsByMaxPrice(maxPrice);
	}

	
	@RequestMapping(value = "/getCouponsTillDate/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Coupon> getCouponsTillDate(@PathVariable("date") String date, HttpServletRequest request,
			HttpServletResponse response) {
		CustomerDB_DAO custDAO = new CustomerDB_DAO();
		Date date2web = Date.valueOf(date);
		CustomerFacade current = facade(request, response);
		logger.debug("getCouponsTillDate(date: '" + date2web + "') method implemented by: "
				+ current.showCustomerProfile().getName());
		return StaticQueries.getCouponTillMaxDate(StaticQueries.convert2SQL(date2web),
				custDAO.readAllCoupons(current.showCustomerProfile().getId()));
	}
	
	
	@RequestMapping(value="/showCustomerProfile", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Customer showCustomerProfile(HttpServletRequest request, HttpServletResponse response) {
		CustomerFacade current = facade(request, response);
		logger.info("showCustomerProfile() is implemented by customer: " + current.showCustomerProfile().getName());
		return current.showCustomerProfile();
	}


	@RequestMapping(value = "/getAllCouponsFromDB()", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Coupon> getAllCouponsFromDB(HttpServletRequest request, HttpServletResponse response) {
		CustomerFacade current = facade(request, response);
		logger.debug("getAllCouponsFromDB() implemented by " + current.showCustomerProfile().getName()
				+ ". This is sub-method in customerRes.");
		return current.readAllCouponsForSelection();
	}

}
