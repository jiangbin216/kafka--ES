package com.quncao.controller;

import com.jfinal.core.Controller;
import com.quncao.service.MonitorService;

/**
 * Created by shk on 2015/11/3.
 */
public class CountController extends Controller {
    public void getRate(){
        renderText(String.valueOf(MonitorService.getRate()));
    }

}
