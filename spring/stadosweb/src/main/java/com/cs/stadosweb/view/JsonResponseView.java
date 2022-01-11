package com.cs.stadosweb.view;

import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.web.servlet.View;

/**
 * Passes along model values for the following, typically
 * used for short (ok/error) responses along with a message:
 * 	
 * "response" = 'ok' or 'error' or 'success'
 * "msg" = 'any string'
 */
public class JsonResponseView implements View {

	public String getContentType() {		
		return("text/javascript");
	}
	
	public void render(Map model, HttpServletRequest req, HttpServletResponse resp) 
		throws Exception {
		
		JSONObject response = new JSONObject();
		//response.put("request", "general");		
		response.put("response", model.get("response"));
		response.put("msg", model.get("msg"));
				
		PrintWriter writer = resp.getWriter();
		writer.print(response.toString());
		writer.flush();
		writer.close();
	}

	
	public static void main(String[] argv) {
		JsonResponseView view = new JsonResponseView();
		try {
			view.render(null, null, null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
