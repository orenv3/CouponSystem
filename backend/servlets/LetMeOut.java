package servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import resourceREST.LogController;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("logout")
public class LetMeOut {

	private LogController logger = new LogController();
	private String homePage = "http://ec2-18-216-14-107.us-east-2.compute.amazonaws.com:8080/OCS2/login/index.html";


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			if ((boolean) request.getSession().getAttribute("authenticated")) {
			String Fname = request.getSession().getAttribute("facade").getClass().getName();
			request.getSession().invalidate();
			response.sendRedirect(homePage);
			logger.info("user: " + Fname + " logged-out successfully.");
			} else
				response.sendError(HttpStatus.FORBIDDEN.value());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
