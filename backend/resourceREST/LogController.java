package resourceREST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LogController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@RequestMapping("debug")
	public void debug(String debug) {
		logger.debug(debug);
	}

	@RequestMapping("info")
	public void info(String info) {
		logger.info(info);
	}

	@RequestMapping("warn2")
	public void warn(String warn) {
		logger.warn(warn);
	}

	@RequestMapping("error2")
	public void error(String error) {
		logger.warn(error);
	}

}
