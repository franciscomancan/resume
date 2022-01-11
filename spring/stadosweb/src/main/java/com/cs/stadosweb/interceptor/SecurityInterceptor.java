package com.cs.stadosweb.interceptor;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cs.stadosweb.domain.impl.SystemUser;

public class SecurityInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

		String path = req.getPathInfo();
		if (path.endsWith("login") || path.endsWith("register") || path.startsWith("/mobile"))
			return (true);
		if(req.getSession().getAttribute("loggedUser") == null)
			throw new ServletException("Session is invalid (potentially expired)...");

		return (true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler, ModelAndView modelAndView) throws Exception {
	
		if(modelAndView != null) {
			SystemUser user = (SystemUser)request.getSession().getAttribute("loggedUser");
			if(user != null)
				((Map<String, Object>)modelAndView.getModel()).put("loggedUser",user);
		}
		
	}

}