package com.cs.stadosweb.view;

import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.View;

import com.cs.stadosweb.domain.impl.Category;
import com.cs.stadosweb.domain.impl.Menu;
import com.cs.stadosweb.domain.impl.Product;

public class JsonMenuView implements View {

	public String getContentType() {		
		return("text/javascript");
	}
	
	/**
	 * Menu object map:
	 * 		jsonMenu:Object -> type, title, jsonCategories
	 * 		jsonCategories:Array of jsonCategory
	 *		jsonCategory:Object -> title, jsonProducts
	 *		jsonProducts:Array of jsonProduct 
	 *		jsonProduct:Object -> title
	 */
	public void render(Map model, HttpServletRequest req, HttpServletResponse resp) 
		throws Exception {
		
		Menu menu = (Menu)model.get("menu");
		JSONObject jsonMenu = new JSONObject();
		jsonMenu.put("type", "menu");
		jsonMenu.put("title", menu.getTitle());
		JSONArray jsonCategories = new JSONArray();
		List<Category> categories = new ArrayList<Category>(menu.getCategories());
		for(Category cat : categories) {
			JSONObject jsonCat = new JSONObject();
			jsonCat.put("title", cat.getTitle());
			JSONArray jsonProducts = new JSONArray();
			List<Product> products = new ArrayList<Product>(cat.getProducts());
			for(Product prod : products) {
				JSONObject jsonProd = new JSONObject();
				jsonProd.put("tit", prod.getTitle());
				jsonProducts.put(jsonProd);
			}
			jsonCat.put("products", jsonProducts);
			jsonCategories.put(jsonCat);
		}		
		
		jsonMenu.put("categories", jsonCategories);
		
		PrintWriter writer = resp.getWriter();
		writer.print(jsonMenu.toString());
		writer.flush();
		writer.close();
	}

	
	public static void main(String[] argv) {
		JsonMenuView view = new JsonMenuView();
		try {
			view.render(null, null, null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
