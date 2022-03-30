package la.liga.del.barrio.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	@RequestMapping("/login")
	public String login() {
		return "Login_template";
	}

	@RequestMapping("/loginError")
	public String loginError() {
		return "LoginError_template";
	}
}