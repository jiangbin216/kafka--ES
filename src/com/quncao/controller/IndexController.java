package com.quncao.controller;

import com.jfinal.core.Controller;
import com.quncao.service.MonitorService;

/**
 * IndexController
 */
public class IndexController extends Controller {
	public void index() {
        setAttr("status", MonitorService.status());
        render("index.jsp");
	}
	public void start(){
		MonitorService.start();
		renderText("=========== The Consumer Start ============");
	}
	public void stop(){
		MonitorService.stop();
		renderText("*********** The Consumer Stop **********");
	}
	public void status(){
		renderText(String.valueOf(MonitorService.status()));
	}
}





