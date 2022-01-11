package com.cs.stadosweb.control;

import java.util.HashMap;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.cs.stadosweb.domain.impl.Coupon;
import com.cs.stadosweb.domain.impl.Invoice;
import com.cs.stadosweb.domain.impl.InvoiceProduct;
import com.cs.stadosweb.domain.impl.Menu;
import com.cs.stadosweb.domain.impl.Product;
import com.cs.stadosweb.domain.impl.SystemUser;
import com.cs.stadosweb.enumtype.InvoiceProductOverride;
import com.cs.stadosweb.service.MenuService;
import com.cs.stadosweb.service.InvoiceService;
import com.cs.stadosweb.service.UserService;
import com.cs.stadosweb.view.JsonMenuView;
import com.cs.stadosweb.view.JsonInvoiceView;
import com.cs.stadosweb.view.JsonProductView;
import com.cs.stadosweb.view.JsonResponseView;

/**
 *	Filter all requests made from any mobile client.
 *	The controller pushes to json-specific views but
 *	currently has to take the steps to decode json
 *	requests.
 */
public class MobileRequestController extends MultiActionController {

	MenuService menuService = null;
	InvoiceService invoiceService = null;
	UserService userService = null;
	
	/**
	 * Change the override status of a given InvoiceProduct from the mobile client.
	 * Manager or Super only functionality.
	 */
	public ModelAndView auditProduct(HttpServletRequest req, HttpServletResponse resp) {
		String ipId = req.getParameter("ipid");
		String val = req.getParameter("val");
		Map<String, Object> model = new HashMap<String, Object>(1);
		if(!isValid(ipId) || !isValid(val)) {
			model.put("response", "error");
			return(new ModelAndView(new JsonResponseView(),model));
		}
		
		InvoiceProduct  ip = new InvoiceProduct();
		ip.setId(Long.valueOf(ipId));
		if(val.equals("v"))
			ip = invoiceService.auditInvoiceProduct(ip, InvoiceProductOverride.VOID);
		else if(val.equals("c"))
			ip = invoiceService.auditInvoiceProduct(ip, InvoiceProductOverride.COMP);
		
		if(ip == null) {
			model.put("response", "error");
			return(new ModelAndView(new JsonResponseView(),model));
		}
		else {
			model.put("response", "success");
			return(new ModelAndView(new JsonResponseView(),model));
		}
	}
	
	/** Retrieve all products, doesn't have visibility of any collection relationships **/
	public ModelAndView getProducts(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> model = new HashMap<String, Object>(1);
		List<Product> products = menuService.getAllProducts();
		model.put("products", products);
		return (new ModelAndView(new JsonProductView(), model));
	}
	
	/** Retrieve all menus, used also to populate associated categories **/
	public ModelAndView getMenus(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> model = new HashMap<String, Object>(1);
		Menu menu = menuService.getAllMenus().get(0);
		model.put("menu", menu);
		return (new ModelAndView(new JsonMenuView(), model));
	}
	
	/** Used simply to check availability of stados services **/
	public ModelAndView ping(HttpServletRequest req, HttpServletResponse resp) {
		Map<String,String> model = new HashMap<String,String>(1);
		model.put("response","active");
		System.out.println("\t\tbouncing ping to " + req.getRemoteAddr());
		return(new ModelAndView(new JsonResponseView(), model));
	}
	
	public ModelAndView getRoster(HttpServletRequest req, HttpServletResponse resp) {
		return(new ModelAndView("roster"));
	}
	
	public ModelAndView getInvoice(HttpServletRequest req, HttpServletResponse resp) {
		long oid = Long.parseLong((String)req.getParameter("oid"));
		Invoice invoice = getInvoiceService().getInvoice(oid);
		
		List<Invoice> conversionList = new ArrayList<Invoice>(1);
		conversionList.add(invoice);
		Map<String,Object> model = new HashMap<String,Object>(1);
		model.put("invoices", conversionList);
		
		return(new ModelAndView(new JsonInvoiceView(), model));
	}
	/**
	 * Handle all logic for order submissions and re-submissions from the mobile client.
	 * This does the work of unmarshalling the json objects and taking the appropriate
	 * steps. This will update an invoice, indicated by the presence of an ID, otherwise 
	 * persist a new instance. 
	 * @throws Exception
	 */
	public ModelAndView submitOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String,String> model = new HashMap<String,String>(1);
		String order = req.getParameter("order");
		JSONObject jsonOrder = new JSONObject(new JSONTokener(order));
		
		Invoice invoice = new Invoice();
		invoice.setLocation(jsonOrder.getString("location"));
		boolean updating = false;
		if(jsonOrder.has("id")) {
			try {
				invoice.setId(Long.parseLong(jsonOrder.getString("id")));
				updating = true;
			}
			catch(NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}			
			
		SystemUser server = userService.getUser(req.getParameter("user"));
		if(server != null) {
			invoice.setOwner(server);		
		
				/* insert a null check (dependent upon json utils) */
			JSONArray jsonProducts = jsonOrder.getJSONArray("products");
			for(int i=0; i<jsonProducts.length(); ++i) {
				JSONObject jsonProduct = jsonProducts.getJSONObject(i);			
				Product product = menuService.getProduct(jsonProduct.getString("tit"));
				InvoiceProduct ip = new InvoiceProduct(invoice, product);
				ip.setDetail(jsonProduct.getString("dtl"));
				ip.setQuantity(jsonProduct.getInt("cnt"));
				invoice.getInvoiceProducts().add(ip);			
			}
			if(!jsonOrder.isNull("cpns")) {
				JSONArray jsonCoupons = jsonOrder.getJSONArray("cpns");
				for(int i=0; i<jsonCoupons.length(); ++i) {
					JSONObject jsonCpn = jsonCoupons.getJSONObject(i);
						/* no tracking as yet, comment models */
					Coupon cpn = new Coupon(); 		//= menuService.getProduct(jsonProduct.getString("title"));
					cpn.setCode(jsonCpn.getString("cod"));
					cpn.setCredit(Integer.parseInt(jsonCpn.getString("cdt")));
					cpn.setInvoice(invoice);
					invoice.getCoupons().add(cpn);
				}
			}			
			
			if(updating)
				invoice = invoiceService.updateInvoice(invoice);
			else
				invoice = invoiceService.createInvoice(invoice);
			
			if(invoice != null)
				model.put("response", "success");
			else
				model.put("response", "error");
		}
		else
			model.put("response", "no server paramater");
		
		return(new ModelAndView(new JsonResponseView(), model));
	}
	
	/** Retrieve the pending orders for a given user/session. **/
	public ModelAndView getPendingOrders(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>(1);		
		SystemUser user = userService.getUser(req.getParameter("user"));
		
		List<Invoice> orders =  invoiceService.getPendingUserInvoices(user);
		model.put("invoices", orders);		
		return(new ModelAndView(new JsonInvoiceView(), model));
	}

	boolean isValid(String s) {
		return (s != null && !s.equals(""));
	}
	
	private MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	private InvoiceService getInvoiceService() {
		return invoiceService;
	}

	public void setInvoiceService(InvoiceService orderService) {
		this.invoiceService = orderService;
	}

	private UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
