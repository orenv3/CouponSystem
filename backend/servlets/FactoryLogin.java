package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import resourceREST.LogController;

import coupon.clients.facade.CouponClientFacade;
import general.Clients;
import general.CouponSystem;

public class FactoryLogin {

	private Map<Clients, String> urls;
	private CouponClientFacade cf = null;
	private final String loginPage = "http://ec2-18-216-14-107.us-east-2.compute.amazonaws.com:8080/OCS2/login/index.html";
	private LogController logger = new LogController();


	public FactoryLogin() {
		super();
		urls = new HashMap<>();
		urls.put(Clients.ADMIN,
				"http://ec2-18-216-14-107.us-east-2.compute.amazonaws.com:8080/OCS2/adminWeb/index.html");
		urls.put(Clients.COMPANY,
				"http://ec2-18-216-14-107.us-east-2.compute.amazonaws.com:8080/OCS2/companies/index.html");
		urls.put(Clients.CUSTOMER,
				"http://ec2-18-216-14-107.us-east-2.compute.amazonaws.com:8080/OCS2/customers/index.html");
	}

	
	public void login(String name, String password, Clients type, HttpServletRequest request,
			HttpServletResponse response) {

		boolean authenticated = false;

		switch (type) {
		case ADMIN:
			cf = CouponSystem.getInstance().login(name, password, Clients.ADMIN);
			try {
				if (cf != null) {
					request.getSession().setAttribute("facade", cf);
					authenticated = true;
					request.getSession().setAttribute("authenticated", authenticated);
					response.sendRedirect(this.getPath(type));
					logger.info("user ADMIN just logged-in");
				} else {
					logger.warn("ADMIN ClientType: try to enter with the parameters: name: " + name + " and password: "
							+ password);
					request.getSession().setAttribute("authenticated", authenticated);
					response.sendRedirect(loginPage);

				}
			} catch (IOException e) {
			}
			break;
		case COMPANY:
			cf = CouponSystem.getInstance().login(name, password, Clients.COMPANY);
			try {
				if (cf != null) {
					request.getSession().setAttribute("facade", cf);
					authenticated = true;
					request.getSession().setAttribute("authenticated", authenticated);
					response.sendRedirect(this.getPath(type));
					logger.info("company with user name: " + name + " just logged-in");
				} else {
					logger.warn(
							"COMPANY clientType: try to enter with the parameters: name: " + name + " and password: "
							+ password);
					request.getSession().setAttribute("authenticated", authenticated);
					response.sendRedirect(loginPage);
				}
			} catch (IOException e) {
			}
			break;
		case CUSTOMER:
			cf = CouponSystem.getInstance().login(name, password, Clients.CUSTOMER);
			try {
				if (cf != null) {
					response.reset();
					request.getSession().setAttribute("facade", cf);
					authenticated = true;
					request.getSession().setAttribute("authenticated", authenticated);
					response.sendRedirect(this.getPath(type));
					logger.info("customer with user name: " + name + " just logged-in");
				} else {
					logger.warn("CLIENT ClientType: try to enter with the parameters: name: " + name
							+ " and password: "
							+ password);
					request.getSession().setAttribute("authenticated", authenticated);
					response.sendRedirect(loginPage);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	private String getPath(Clients clientType) {
		switch (clientType) {
		case ADMIN:
			return this.urls.get(clientType);
		case COMPANY:
			return this.urls.get(clientType);
		case CUSTOMER:
			return this.urls.get(clientType);
		default:
			break;
		}
		return null;
	}
}
