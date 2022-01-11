package com.cs.stadosweb.view;

import java.io.PrintWriter;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.View;

import com.cs.stadosweb.domain.impl.Product;

/**
 * Used to generate a generalized view of products, used
 * in the client to offer product lists (not specific product
 * info).  The titles are later used to create the appropriate
 * InvoiceProduct copies.
 */
public class JsonProductView implements View {

	public String getContentType() {		
		return("text/javascript");
	}
	
	public void render(Map model, HttpServletRequest req, HttpServletResponse resp) 
		throws Exception {
		
		List<Product> products = (List)model.get("products");
		JSONObject response = new JSONObject();
		response.put("request", "getProducts");
		response.put("status","success");
		JSONArray productArray = new JSONArray();
		for(Product product : products) {
			productArray.put(product.getTitle());
		}
		response.put("products", productArray);
				
		PrintWriter writer = resp.getWriter();
		writer.print(response.toString());
		writer.flush();
		writer.close();
	}
	
}
