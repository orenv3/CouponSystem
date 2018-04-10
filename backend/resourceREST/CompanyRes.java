package resourceREST;

import java.util.Collection;
import java.util.Date;
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

import beans.coupon.system.Company;
import beans.coupon.system.Coupon;
import coupon.clients.facade.CompanyFacade;
import dao.db.units.CompanyDB_DAO;
import dao.db.units.CustomerDB_DAO;
import general.StaticQueries;

@RestController
@RequestMapping(value = "companyRes")
@CrossOrigin(origins="*")
public class CompanyRes {
	

	private LogController logger = new LogController();

	private CompanyFacade facade(HttpServletRequest request, HttpServletResponse response) {
		return (CompanyFacade) request.getSession().getAttribute("facade");
	}
	

	@RequestMapping(value="/createCoupon", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public void createCoupon(@RequestBody Coupon coupon, HttpServletRequest request, HttpServletResponse response) {
		CompanyFacade current = facade(request, response);
		logger.info(current.showMyCompany().getName() + " would like to create the coupon: " + coupon);
		current.createCoupon(coupon);
		logger.debug("The " + coupon.getTitle() + " successfully created");
	}
	
	@RequestMapping(value="/removeCoupon", method=RequestMethod.DELETE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public void removeCoupon(@RequestBody Coupon coupon, HttpServletRequest request, HttpServletResponse response) {
		CompanyFacade current = facade(request, response);
		logger.info(current.showMyCompany().getName() + " would like to delete the coupon: " + coupon);
		current.removeCoupon(coupon);
		logger.debug("The coupon: " + coupon.getTitle() + " successfully deleted");
	}

	@RequestMapping(value="/showMyCompany", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Company showMyCompany(HttpServletRequest request, HttpServletResponse response) {
		CompanyFacade current = facade(request, response);
		logger.info("showMyCompany() is implemented by company: " + current.showMyCompany().getName());
		return current.showMyCompany();
	}

	@RequestMapping(value="/getCoupon/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Coupon getCoupon(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) {
		CompanyFacade current = facade(request, response);
		logger.info("getCoupon(id: '" + id + "') is implemented by: " + current.showMyCompany().getName());
		return current.getCoupon(id);
	}

	@RequestMapping(value="/getAllCoupons", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Collection<Coupon> getAllCoupons(HttpServletRequest request, HttpServletResponse response) {
		CompanyFacade current = facade(request, response);
		logger.debug("getAllCoupons() method implemented by: " + current.showMyCompany().getName());
		return current.getAllCoupons();
	}

	@RequestMapping(value="/getCouponByType", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Collection<Coupon> getCouponByType(HttpServletRequest request, HttpServletResponse response) {
		CompanyFacade current = facade(request, response);
		logger.debug("getCouponByType() method implemented by: " + current.showMyCompany().getName());
		return current.getCouponByType();
	}

	@RequestMapping(value="/getCouponByMaxPrice/{maxPrice}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Collection<Coupon> getCouponByMaxPrice(@PathVariable("maxPrice") double maxPrice, HttpServletRequest request,
			HttpServletResponse response) {
		CompanyFacade current = facade(request, response);
		logger.debug("getCouponByMaxPrice(price: '" + maxPrice + "') method implemented by: "
				+ current.showMyCompany().getName());
		return current.getCouponByMaxPrice(maxPrice);
	}
	
	@RequestMapping(value = "/getCouponByDate/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Coupon> getCouponByDate(@PathVariable("date") String date, HttpServletRequest request,
			HttpServletResponse response) {

		CompanyFacade current = facade(request, response);
		CompanyDB_DAO compDAO = new CompanyDB_DAO();
		java.sql.Date date2web = java.sql.Date.valueOf(date);
		logger.debug("getCouponByDate(date: '" + date2web + "') method implemented by: "
				+ current.showMyCompany().getName());
		return StaticQueries.getCouponTillMaxDate(StaticQueries.convert2SQL(date2web),
				compDAO.readAllCoupons(current.showMyCompany().getId()));
	}

	
	@RequestMapping(value = "/updateCoupon/{startDate}/{endDate}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateCoupon(@RequestBody Coupon coupon, @PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate, HttpServletRequest request, HttpServletResponse response) {
		CompanyFacade current = facade(request, response);

		// convert string to util.Date
		java.sql.Date sqlEndDate = java.sql.Date.valueOf(endDate);
		java.util.Date javaEndDate = new Date(sqlEndDate.getTime());

		java.sql.Date sqlStartDate = java.sql.Date.valueOf(startDate);
		java.util.Date javaStartDate = new Date(sqlStartDate.getTime());

		coupon.setEndDate(javaEndDate);
		coupon.setStartDate(javaStartDate);

		logger.info(current.showMyCompany().getName() + " would like to update the coupon: " + coupon);
		current.updateCoupon(coupon);
		logger.debug("The " + coupon.getTitle() + " successfully updated");
	}

	@RequestMapping(value = "/howManyCouponsinDB", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public long howManyCouponsinDB(HttpServletRequest request, HttpServletResponse response) {
		if (facade(request, response) instanceof CompanyFacade && facade(request, response) != null) {
		logger.debug("howManyCouponsinDB() implemented. This is sub-method in createCoupon() method.");
		CustomerDB_DAO custDAO = new CustomerDB_DAO();
		Collection<Coupon> list = new LinkedList<>();
		list = custDAO.readAllCouponsForPresentasion();
		long max = 1;
		for (Coupon coupon : list) {
			if (coupon.getId() >= max)
				max = coupon.getId();
		}
		logger.info("howManyCouponsinDB() returned " + (1 + max));
		return max + 1;
		} else
			return -1;
	}
	
}
