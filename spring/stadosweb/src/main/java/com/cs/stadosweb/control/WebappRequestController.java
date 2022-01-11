package com.cs.stadosweb.control;

import java.util.ArrayList;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.cs.stadosweb.Constant;
import com.cs.stadosweb.domain.impl.Category;
import com.cs.stadosweb.domain.impl.Invoice;
import com.cs.stadosweb.domain.impl.InvoiceProduct;
import com.cs.stadosweb.domain.impl.Menu;
import com.cs.stadosweb.domain.impl.Product;
import com.cs.stadosweb.domain.impl.SystemUser;
import com.cs.stadosweb.enumtype.InvoiceProductOverride;
import com.cs.stadosweb.service.MenuService;
import com.cs.stadosweb.service.InvoiceService;
import com.cs.stadosweb.service.UserService;

public class WebappRequestController extends MultiActionController implements Constant {

	protected UserService userService;
	protected InvoiceService invoiceService;
	protected MenuService menuService;

	public ModelAndView auditOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String ipId = req.getParameter("ipid");
		String val = req.getParameter("val");
		Map<String, Object> model = new HashMap<String, Object>(1);
		if(!isValid(ipId) || !isValid(val)) {
			model.put("status", "Incorrect parameters for audit operation");
			return(new ModelAndView("internalStatus",model));
		}
		
		InvoiceProduct  ip = new InvoiceProduct();
		ip.setId(Long.valueOf(ipId));
		if(val.equals("v"))
			ip = invoiceService.auditInvoiceProduct(ip, InvoiceProductOverride.VOID);
		else if(val.equals("c"))
			ip = invoiceService.auditInvoiceProduct(ip, InvoiceProductOverride.COMP);
		
		if(ip == null) {
			model.put("status", "Service unable to override item");
			return(new ModelAndView("internalStatus",model));
		}
			
		else
			return(null);
	}
	
	public ModelAndView viewOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		long oId = -1l;
		Invoice invoice = null;
		Map view = new HashMap<String, Object>();
		try {
			oId = Long.valueOf(req.getParameter("oid"));
			invoice = invoiceService.getInvoice(oId);
		}
		catch(RuntimeException rte) { 
			rte.printStackTrace();
			view.put("status", "Error retrieving mentioned order, check and try again. ");
			return(new ModelAndView("internalStatus", view));
		}		
		
		view.put("invoice", invoice);
		return (new ModelAndView("popup/orderProducts", view));
	}
	
	public ModelAndView observeOrders(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<Invoice> invoices = invoiceService.getInvoices(null);
		Map<String, Object> view = new HashMap<String, Object>();
		view.put("invoices", invoices);
		return (new ModelAndView("observer/allOrders", view));
	}

	public ModelAndView createOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String location = req.getParameter("location");
		//String customer = req.getParameter("customer");

		Map<String, Object> view = new HashMap<String, Object>();
		// perform misc. validations
		if (!isValid(location))
			view.put("status", "Not all data present; try again");
		if (view.get("status") != null)
			return (new ModelAndView("internalStatus", view));

		// otherwise, generate the object and persist
		Invoice newInstance = new Invoice();
		//if(customer != null)
		//	newInstance.setCustomer(customer);
		
		newInstance.setLocation(location);
		newInstance.setOwner((SystemUser)req.getSession().getAttribute("loggedUser"));
		//newInstance.setInvoiceguid(req.getLocalAddr() + "." + System.currentTimeMillis());
		newInstance = invoiceService.createInvoice(newInstance);

		if (newInstance != null)
			return(listOrders(req, resp));
		else
			view.put("status","Error: order not created.  Contact tech support it this continues.");

		return (new ModelAndView("internalStatus", view));
	}

	public ModelAndView bumpStatus(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		invoiceService.bumpInvoiceStatus(Integer.parseInt(req.getParameter("iid")));
		return(null);
	}
	
	public ModelAndView createProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();		
		String title = req.getParameter("title");				
		if(!isValid(title)) {
			model.put("status","Error: product not created.  Insufficient product information");
			return (new ModelAndView("internalStatus", model));
		}
		
		int price = 0;
		try {
			price = Integer.valueOf(req.getParameter("price"));
		}
		catch(NumberFormatException nfe) { nfe.printStackTrace(); }
		
		Product newInstance = new Product();
		newInstance.setTitle(title);
		newInstance.setDescription(req.getParameter("description"));
		newInstance.setPennies(price);
		//newInstance.getCategories().add(cat);		--> not cascading
		//newInstance.setProductcustom(req.getParameter("custom"));
		newInstance = menuService.createProduct(newInstance);

		String cid = req.getParameter("cid");
		Category cat = menuService.getCategory(Integer.valueOf(cid));
		menuService.addProductChild(cat, newInstance);
		
		if (newInstance != null) {
			return(listProducts(req,resp));
		} else {
			model.put("status","Error: product not created.  Contact tech support it this continues.");
			return (new ModelAndView("internalStatus", model));
		}
	}
	
	public ModelAndView deleteProduct(HttpServletRequest req, HttpServletResponse resp) {
		String pid = req.getParameter("pid");
		int result = menuService.deleteProduct(Integer.parseInt(pid));
		return(null);
	}

	public ModelAndView createMenu(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		if(isValid(req.getParameter("title"))) {
			Menu newInstance = new Menu();
			newInstance.setTitle(req.getParameter("title"));
			newInstance.setDescription(req.getParameter("description"));
			//newInstance.setMenucustom(req.getParameter("custom"));
	
			Menu result = menuService.createMenu(newInstance);
			if (result != null)
				model.put("status", "Menu successfully created: " + result.getTitle());
			else
				model.put("status","Error: menu not created.  Contact tech support it this continues.");

			return (new ModelAndView("internalStatus", model));
		}			
		else {
			model.put("serviceCall","createMenu");
			return(new ModelAndView("popup/menuForm",model));
		}
	}
	
	public ModelAndView editMenu(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		Integer menuId = Integer.parseInt(req.getParameter("mid"));
		Menu transientMenu = menuService.getMenu(menuId);
		String title = req.getParameter("title"); 
		if(isValid(title)) {
			transientMenu.setTitle(title);
			transientMenu.setDescription(req.getParameter("description"));
			transientMenu.setMenucustom(req.getParameter("custom"));
			//transientMenu.setActive(req.getParameter("group1"));
			
			menuService.persistMenu(transientMenu);

			return (new ModelAndView("internalStatus", model));
		}			
		else {	//no info present, get instance from id and populate view/form
			model.put("menu",transientMenu);
			model.put("serviceCall","editMenu");
			return(new ModelAndView("popup/menuForm",model));
		}
	}

	public ModelAndView createCategory(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		Category newInstance = new Category();
		newInstance.setTitle(req.getParameter("title"));
		newInstance.setDescription(req.getParameter("description"));

		Category result = menuService.createCategory(newInstance);
		if (result != null) {
			// view.put("status", "Category successfully created: " +
			// result.getTitle());
			List<Category> categories = menuService.getAllCategories();
			model.put("categories", categories);
			return (new ModelAndView("category", model));
		} else {
			model.put("status","Error: Category not created.  Contact tech support it this continues.");
			return (new ModelAndView("internalStatus", model));
		}
	}

	public ModelAndView listMenus(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		List<Menu> menus = menuService.getAllMenus();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("menus", menus);
		return (new ModelAndView("menu", model));
	}

	public ModelAndView listProducts(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<Product> products = null; //menuService.getAllProducts();
		Map<String, Object> model = new HashMap<String, Object>();		
		if (req.getParameter("ctx") == null) {
			products = menuService.getAllProducts();
			List categories = menuService.getAllCategories();
			model.put("products", products);
			model.put("categories", categories);
			return (new ModelAndView("product", model));
		}
		else if (req.getParameter("ctx").equals("category")) {	//list products attached to a given category
			if(req.getParameter("inclusive") != null) {
				Category cat = menuService.getCategory(Integer.parseInt(req.getParameter("cid")));
				products = new ArrayList<Product>(cat.getProducts());
				model.put("products", products);
				model.put("inclusive", true);
				return (new ModelAndView("popup/productList", model));
			}
			else {
				products = menuService.getAllProducts();
				model.put("products", products);
				model.put("cid", req.getParameter("cid"));
				return (new ModelAndView("popup/productList", model));
			}
		} 
		else { // ctx = order...
			products = menuService.getAllProducts();
			model.put("products", products);
			model.put("iid", req.getParameter("iid"));
			return (new ModelAndView("popup/productList", model));
		}
	}

		/* paged implementation for retrieving invoices */
	public ModelAndView listOrders(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		HttpSession sess = req.getSession();
		Map<String, Object> model = new HashMap<String, Object>();
		
		Integer offset = (Integer)sess.getAttribute(INVOICE_OFFSET);
		String page = req.getParameter("page");
		int total = invoiceService.getInvoiceCount();
		if(offset == null || page == null) {			
			offset = total - INVOICE_PAGE_LIMIT;
			sess.setAttribute(INVOICE_OFFSET,offset);
		}
		else {			
			if(page != null && page.equals("next"))
				offset = Math.max(offset-INVOICE_PAGE_LIMIT, 0);
			else
				offset = Math.min(offset+INVOICE_PAGE_LIMIT, total);			
				
			sess.setAttribute(INVOICE_OFFSET,offset);
		}
		
		if(offset == 0)
			model.put("nomore", "true");
		else if(offset >= total - INVOICE_PAGE_LIMIT)
			model.put("noless", "true");
			
		
		List<Invoice> invoices = invoiceService.getInvoices(offset,INVOICE_PAGE_LIMIT);
		Collections.reverse(invoices);
		
		model.put("invoices", invoices);
		model.put("total", total);
		return (new ModelAndView("order", model));
	}

	public ModelAndView listCategories(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<Category> categories = menuService.getAllCategories();
		Map<String, Object> view = new HashMap<String, Object>();
		view.put("categories", categories);
		if (req.getParameter("ctx") == null)
			return (new ModelAndView("category", view));

		// else, is a population list for Menu, so the menuId is present
		view.put("mid", req.getParameter("mid"));
		return (new ModelAndView("popup/categoryList", view));
	}

	public ModelAndView addCategory(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> view = new HashMap<String, Object>();
		int catId = Integer.valueOf(req.getParameter("cid"));
		int menuId = Integer.valueOf(req.getParameter("mid"));

		Menu menu = new Menu(menuId);
		Category category = new Category(catId);
		menu = menuService.addCategoryChild(menu, category);

		if (menu != null) {
			view.put("status", "Category added: " + menu.getTitle() + " >> "
					+ category.getTitle());
		} else {
			view.put("status", "Error adding category");
		}
		return (new ModelAndView("internalStatus", view));
	}

	/**
	 * Adds an association between a product and a category.  Originally developed
	 * to allow many-to-many but has later been constrained (via logic, not db) to 
	 * allow only a many-to-one, only one category for a product.  Therefore, the
	 * logic will remove an existing association before it makes the new one.
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addProductCategory(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		Map<String, Object> view = new HashMap<String, Object>();
		int catId = Integer.valueOf(req.getParameter("cid"));
		int prodId = Integer.valueOf(req.getParameter("pid"));

		Product product = new Product(prodId);
		Category category = new Category(catId);
		category = menuService.addProductChild(category, product);

		if (category != null) {
			view.put("status", "Product added: " + category.getTitle() + " >> "
					+ product.getTitle());
		} else {
			view.put("status", "Error adding product.");
		}

		return (new ModelAndView("internalStatus", view));
	}

	public ModelAndView addOrderProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> view = new HashMap<String, Object>();
		int invoiceId = Integer.valueOf(req.getParameter("iid"));
		int prodId = Integer.valueOf(req.getParameter("pid"));

		Product product = new Product(prodId);
		Invoice invoice = new Invoice(invoiceId);
		invoice = invoiceService.addProductChild(invoice, product, "", 1);

		if (invoice != null) {
			return(listOrders(req, resp));
		} else {
			view.put("status", "Error adding product.");
		}

		return (new ModelAndView("internalStatus", view));
	}

	public ModelAndView help(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		Map<String, Object> view = new HashMap<String, Object>();
		return (new ModelAndView("help", view));
	}

	public ModelAndView contact(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		Map<String, Object> view = new HashMap<String, Object>();
		return (new ModelAndView("contact", view));
	}

	public ModelAndView home(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		Map<String, Object> view = new HashMap<String, Object>();
		return (new ModelAndView("home", view));
	}

	public ModelAndView listUsers(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<SystemUser> users = userService.getUsers();
		Map<String, Object> view = new HashMap<String, Object>();
		view.put("users", users);
		return (new ModelAndView("user", view));
	}
	
	public ModelAndView viewPending(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<Invoice> invoices = 
			invoiceService.getInvoices(Constant.STATUS_OPEN);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("invoices", invoices);
		return(new ModelAndView("observer/pending",model));
	}
	
	public ModelAndView viewServed(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<Invoice> invoices = invoiceService.getInvoices(Constant.STATUS_SERVED);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("invoices", invoices);
		return(new ModelAndView("observer/served",model));
	}
	
	public ModelAndView viewAllOrders(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<Invoice> invoices = invoiceService.getInvoices(null);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("invoices", invoices);
		return(new ModelAndView("observer/all",model));
	}

	boolean isValid(String s) {
		return (s != null && !s.equals(""));
	}

	/** all access/DI methodology **/
	private UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private InvoiceService getInvoiceService() {
		return invoiceService;
	}

	public void setInvoiceService(InvoiceService orderService) {
		this.invoiceService = orderService;
	}

	private MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

}
