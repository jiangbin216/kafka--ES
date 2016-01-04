package com.quncao.common;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.render.ViewType;
import com.quncao.controller.*;

/**
 * API引导式配�?
 */
public class DemoConfig extends JFinalConfig {
	
	
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		//加载少量必要配置，随后可用PropKit.get(...)获取
		PropKit.use("res/public.properties");
        me.setViewType(ViewType.JSP);
    }
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/", IndexController.class);	// 第三个参数为该Controller的视图存放路
		me.add("/count",CountController.class);
		me.add("/prop",PropController.class);
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
	}
	
	/**
	 * 配置全局拦截�?
	 */
	public void configInterceptor(Interceptors me) {
		
	}
	
	/**
	 * 配置处理�?
	 */
	public void configHandler(Handlers me)
	{
		me.add(new ContextPathHandler("contextPath"));	//contextPath为你设置的上下文路径}
	}
	
}
