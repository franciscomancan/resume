package com.cs.stadosweb.servlet;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatusFeed extends HttpServlet {

    private static final long serialVersionUID = -5258689736300034036L;
    
    @Override
    public void init() throws ServletException {
        super.init();
    }
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req,
	    HttpServletResponse resp) throws ServletException, IOException {

		PrintWriter writer = resp.getWriter();
		if (req.getParameter("ping") != null) {			
		    writer.write("stados:active");
		    
		} 
		else {
		    Enumeration<String> atts = getServletContext().getAttributeNames();
		    StringBuilder output = new StringBuilder();
		    
		    output.append("WEB APPLICATION STATUS\n\n");
		    long freeMemory  = Runtime.getRuntime().freeMemory();
		    long totalMemory = Runtime.getRuntime().totalMemory();
		    long usedMemory  = totalMemory - freeMemory;
		    output.append("\n\t\ttotal mem:\t" + totalMemory + 
			    " bytes\n\t\tused mem:\t" + usedMemory + 
			    " bytes\n\t\tfree mem:\t" + freeMemory + " bytes");
		    
		    while (atts.hasMoreElements()) {
				String key = atts.nextElement().toString();
				Object att = getServletContext().getAttribute(key);
				output.append("\n\n\t\tkey=" + key + "\n\t\tval=" + att.toString());			
		    }
		    
//		    ApplicationContext ctx = new ClassPathXmlApplicationContext("config/beans.xml");
//	    	SessionFactory factory = (SessionFactory)ctx.getBean("sessionFactory");	    	
//	    	Statistics stats =  factory.getStatistics();
//	    	
//	    	output.append("\n\n\tHibernate stats ----------------------------------------------------------------");
//	    	output.append("\n\t\tconnectCount: " + stats.getConnectCount());
//	    	output.append("\n\t\ttransactionCount: " + stats.getTransactionCount());
//	    	output.append("\n\t\tsuccessfulTransactionCount: " + stats.getSuccessfulTransactionCount());
//	    	output.append("\n\t\tflushCount: " + stats.getFlushCount());
//	    	output.append("\n\t\t" + stats.getTransactionCount());
//	    	output.append("\n\t\t" + stats.getTransactionCount());
//	    	output.append("\n\t\t" + stats.getTransactionCount());
//	    	output.append("\n\t\t" + stats.getTransactionCount());
//	    	output.append("\n\t\t" + stats.getTransactionCount());
//	    	output.append("\n\t\t" + stats.getTransactionCount());
//	    	output.append("\n\t\t" + stats.getTransactionCount());
	    	writer.write(output.toString());
		}
	
		writer.flush();
		writer.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

	doGet(request, response);
    }

}
