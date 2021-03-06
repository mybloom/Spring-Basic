package hello.core.web;

import hello.core.common.MyLogger;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

	private final LogDemoService logDemoService;
	private final MyLogger myLogger; //DL이 주입된다.

	@RequestMapping("log-demo")
	@ResponseBody
	public String logDemo(HttpServletRequest request) {//자바에서 제공하는 표준 서블릿 규약.
		String requestUrl = request.getRequestURL().toString();
		myLogger.setRequestURL(requestUrl);

		myLogger.log("controller test");
		logDemoService.logic("testId");

		return "ok";
	}
}
