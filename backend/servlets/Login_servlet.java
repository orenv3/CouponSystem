package servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import general.Clients;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("login_servlet")
public class Login_servlet {


	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response, @RequestParam String name,
			@RequestParam String password, @RequestParam String clientsType) {

		Clients clientType = Clients.valueOf(clientsType);
		FactoryLogin login = new FactoryLogin();
		login.login(name, password, clientType, request, response);

	}

}
