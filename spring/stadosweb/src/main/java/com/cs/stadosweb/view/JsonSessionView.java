package com.cs.stadosweb.view;

import java.io.PrintWriter;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.web.servlet.View;

import com.cs.stadosweb.domain.impl.SystemUser;

public class JsonSessionView implements View {

	public String getContentType() {		
		return("text/javascript");
	}
	
	public void render(Map model, HttpServletRequest req, HttpServletResponse resp) 
		throws Exception {
		
		SystemUser usr = (SystemUser)model.get("user");
		JSONObject response = new JSONObject();
		//response.put("request", "general");		
		response.put("sid", (String)model.get("session"));
		response.put("uid", usr.getUsername());
		response.put("typ", usr.getType());
				
		PrintWriter writer = resp.getWriter();
		writer.print(response.toString());
		writer.flush();
		writer.close();
	}
	
	public static void main(String[] argv) {
		JsonSessionView view = new JsonSessionView();
		try {
			view.render(null, null, null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
