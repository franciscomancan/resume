package com.cs.stadosweb;

public interface Constant {

    public static final String ROLE_SUPER = "super";
    public static final String ROLE_SERVER = "serve";
    public static final String ROLE_OBSERVER = "observe";
    public static final String ROLE_MANAGER = "mgr";
    
    public static final String STATUS_CREATED = "created";
    public static final String STATUS_OPEN = "open";
    public static final String STATUS_SERVED = "served";
    public static final String STATUS_CLOSED = "closed";
    public static final String STATUS_PAYED = "payed";
    	
    	/* use when time permits to migrate */
    //public enum INVOICE_STATUS {CREATED, OPEN, SERVED, CLOSED, PAYED}

    	/** Constants for the use of paging **/ 
    public static final String INVOICE_OFFSET = "iofset";
    public static final int INVOICE_PAGE_LIMIT = 10;
    
    	/** financial usages **/
    public static final float SALES_TAX_MULTIPLIER = 0.078f;
}
