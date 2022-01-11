package com.cs.stadosweb.control;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import com.cs.stadosweb.domain.impl.SystemUser;
import com.cs.stadosweb.service.UserService;
import com.cs.stadosweb.view.JsonResponseView;
import com.cs.stadosweb.view.JsonSessionView;

/**
 * Front end controller for all types of client sessions (mobile, webapp, etc..),
 * typically for login or other user actions (out of session actions).
 */
public class SessionController extends MultiActionController {

	protected UserService userService;
	
	public ModelAndView register(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		if (req.getParameter("ctx") != null)
			return (new ModelAndView("register"));

		return createUser(req, resp);
	}

	public ModelAndView login(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		
		//System.out.println("\tlogin request: " + req.getRemoteAddr() + ":" + req.getRemoteHost() + ":" + req.getRemotePort());		
		String clientType = req.getParameter("ct");
		SystemUser user = userService.getUser(req.getParameter("user"));
		Map<String, Object> model = new HashMap<String, Object>();
		if(clientType != null && clientType.equals("m")) {
			if(user != null) {
				model.put("user", user);
				model.put("session", req.getSession().getId());
				return(new ModelAndView(new JsonSessionView(),model));
			}
			else {
				model.put("response", "error");
				model.put("msg", "incorrect credentials, try again");
				return(new ModelAndView(new JsonResponseView(),model));
			}
		}
		else {
			if (user != null && user.getPassword() != null && user.getPassword().equals(req.getParameter("pass"))) {
				req.getSession().setAttribute("loggedUser", user);
				model.put("loggedUser", user);
				return (new ModelAndView("home", model));
			} 
			else {			
				model.put("status", "Credentials are incorrect; try again...");
				req.getSession().removeAttribute("loggedUser");
				return (new ModelAndView("externalStatus", model));
			}
		}
	}

	public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		
		req.getSession().removeAttribute("loggedUser");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("status",
				"You've been logged out of the s.t.a.d.o.s system...");
		return (new ModelAndView("externalStatus", model));
	}
	
	public ModelAndView createUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {	
		Map<String, Object> model = new HashMap<String, Object>();
		if(req.getParameter("user") != null) {
			String pass1 = req.getParameter("pass1");
			String pass2 = req.getParameter("pass2");
			String user = req.getParameter("user");
	
			SystemUser loggedUser = (SystemUser)req.getSession().getAttribute("loggedUser");
			// perform misc. validations
			if (!isValid(pass1) || !isValid(pass2) || !isValid(user))
				model.put("status", "Not all data present; try again...");
			if (!pass1.equals(pass2))
				model.put("status", "Passwords do not match; try again...");
			if (userService.getUser(user) != null)
				model.put("status", "Username already exists; try again...");
			if (model.get("status") != null) {
				if (loggedUser == null)
					return (new ModelAndView("externalStatus", model));
				else
					return (new ModelAndView("internalStatus", model));
			}
	
			// otherwise, generate the object and persist
			SystemUser newInstance = new SystemUser();
			newInstance.setUsername(user);
			newInstance.setPassword(pass1);
			newInstance.setFirstname(req.getParameter("first"));
			newInstance.setLastname(req.getParameter("last"));
			newInstance.setDescription(req.getParameter("description"));
			newInstance.setEmail(req.getParameter("email"));
			if(req.getParameter("internal") != null)		//shortcut to checking for an existing session
				newInstance.setType(req.getParameter("type"));
			
			newInstance = userService.createUser(newInstance);
	
			if (newInstance != null) {
				model.put("status", "User successfully created: " + newInstance.getUsername());
				//req.getSession().setAttribute("loggedUser", newInstance);
				return (new ModelAndView("externalStatus", model));
			} 
			else {
				model.put("status","Error: user not created.  Contact tech support it this continues.");
				return (new ModelAndView("externalStatus", model));
			}
		}
		else {
			model.put("serviceCall","createUser");
			return(new ModelAndView("popup/userForm",model));
		}
	}
	
	public ModelAndView editUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		String userid = req.getParameter("uid");
		SystemUser transientUser = userService.getUser(Integer.parseInt(userid));
		if(req.getParameter("user") != null) {
			transientUser.setEmail(req.getParameter("email"));
			transientUser.setDescription(req.getParameter("description"));
			transientUser.setFirstname(req.getParameter("first"));
			transientUser.setLastname(req.getParameter("last"));
			transientUser.setType(req.getParameter("type"));
			
			userService.updateUser(transientUser);
			
			return(null);
		}
		else {
			model.put("user",transientUser);
			model.put("serviceCall","editUser");
			return (new ModelAndView("popup/userForm",model));
		}		
	}
	
	boolean isValid(String s) {
		return (s != null && !s.equals(""));
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
