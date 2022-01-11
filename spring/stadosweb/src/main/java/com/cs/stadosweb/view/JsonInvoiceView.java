package com.cs.stadosweb.view;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.View;
import com.cs.stadosweb.domain.impl.Coupon;
import com.cs.stadosweb.domain.impl.Invoice;
import com.cs.stadosweb.domain.impl.InvoiceProduct;

/**
 * Used to generate a specific view of any number of
 * invoices, containing id information that can be used
 * by the client.
 */
public class JsonInvoiceView implements View {

	public String getContentType() {		
		return("text/javascript");
	}
	
	/**
	 * Menu object map:
	 * 		orderRequest:Object -> type (wrapper)
	 * 		jsonOrders:Array of jsonOrders
	 *		jsonOrder:Object -> id, location, products
	 *		jsonProducts:Array of invoiceProducts 
	 *		jsonProduct:Object -> title, detail, qnt
	 *		jsonCoupons:
	 *		jsonCoupon...
	 */
	public void render(Map model, HttpServletRequest req, HttpServletResponse resp) 
		throws Exception {
			
		List<Invoice> orders = (List)model.get("invoices");
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("type", "orders");
		JSONArray jsonOrders = new JSONArray();
		JSONObject jsonOrder = null;
		for(Invoice invoice: orders) {
			jsonOrder = new JSONObject();
			jsonOrder.put("id", invoice.getId());
			jsonOrder.put("loc", invoice.getLocation());
			jsonOrder.put("stat", invoice.getInvoiceCustom());
			JSONArray jsonProducts = new JSONArray();
			Set<InvoiceProduct> products = invoice.getInvoiceProducts();
			for(Iterator<InvoiceProduct> iter = products.iterator(); iter.hasNext();) {
				InvoiceProduct ip = iter.next();
				JSONObject jsonProd = new JSONObject();
				jsonProd.put("id", ip.getId());
				jsonProd.put("tit", ip.getProduct().getTitle());				
				jsonProd.put("dtl", ip.getDetail());
				jsonProd.put("cnt", ip.getQuantity());
				jsonProducts.put(jsonProd);
			}
			jsonOrder.put("products", jsonProducts);
			
			JSONArray jsonCoupons = new JSONArray();
			Set<Coupon> coupons = invoice.getCoupons();
			for(Iterator<Coupon> iter = coupons.iterator(); iter.hasNext();) {
				Coupon cpn = iter.next();
				JSONObject jsonCpn = new JSONObject();
				jsonCpn.put("cod", cpn.getCode());				
				jsonCpn.put("cdt", cpn.getCredit());
				jsonCoupons.put(jsonCpn);
			}			
			jsonOrder.put("coupons", jsonCoupons);
			
			jsonOrders.put(jsonOrder);
		}		
			
		orderRequest.put("invoices", jsonOrders);
		
		PrintWriter writer = resp.getWriter();
		writer.print(orderRequest.toString());
		writer.flush();
		writer.close();
	}

	
	public static void main(String[] argv) {
		JsonInvoiceView view = new JsonInvoiceView();
		try {
			view.render(null, null, null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
