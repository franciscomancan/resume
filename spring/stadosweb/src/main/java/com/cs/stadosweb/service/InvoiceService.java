package com.cs.stadosweb.service;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import java.util.List;
import com.cs.stadosweb.Constant;
import com.cs.stadosweb.dao.InvoiceDao;
import com.cs.stadosweb.dao.InvoiceProductDao;
import com.cs.stadosweb.dao.ProductDao;
import com.cs.stadosweb.domain.impl.Invoice;
import com.cs.stadosweb.domain.impl.InvoiceProduct;
import com.cs.stadosweb.domain.impl.Product;
import com.cs.stadosweb.domain.impl.SystemUser;
import com.cs.stadosweb.enumtype.InvoiceProductOverride;

/**
 * Central business processing unit for all new and ongoing
 * transactions in the stados domain.  This includes both the
 * general crud ops as well as domain-specific types of calls.
 * All manipulation of the invoice model should pass through this class,
 * ensuring fair treatment of all domain objects.
 */
public class InvoiceService implements Constant {
	
	private InvoiceDao invoiceDao;
	private InvoiceProductDao invoiceProductDao;
	private ProductDao productDao;
	
	private static InvoiceService instance; 

	private InvoiceService() {}
	
	public static InvoiceService getInstance() {
		if(instance == null)
			instance = new InvoiceService();
		
		return(instance);
	}

	/** 
	 * Override an associtaed <code>InvoiceProduct</code> as 
	 * 'complementary' or 'void', used in accounting.  
	 * The appropriate InvoiceProduct argument must only include the 
	 * correct id and <code>InvoiceProductOverride</code> value, all 
	 * other passed values will be ignored (remain persistent).	
	 */
	public InvoiceProduct auditInvoiceProduct(InvoiceProduct iProduct, InvoiceProductOverride o) {
		iProduct = invoiceProductDao.findById(iProduct.getId());
		if(iProduct != null) {
			iProduct.setOverride(o);
			return(invoiceProductDao.update(iProduct));
		}
		else
			return(null);
	}
	
	public Invoice createInvoice(Invoice invoice) {		
		invoice.setInvoiceCustom(Constant.STATUS_OPEN);
		invoice.setCreatedDate(new Date());
		
		generateTotals(invoice);
		
		return (invoiceDao.persist(invoice));
	}
	
	/**
	 * The method works by merging the incoming objects to the existing/
	 * persistent invoice.  So, an update will actually be appending data.
	 */
	public Invoice updateInvoice(Invoice detatchedInvoice) {
		Invoice persistentInv = invoiceDao.findById(detatchedInvoice.getId());
		
		detatchedInvoice.setInvoiceCustom(persistentInv.getInvoiceCustom());
		detatchedInvoice.setCreatedDate(persistentInv.getCreatedDate());
		detatchedInvoice.setModifiedDate(new Date());
		detatchedInvoice.setModifiedBy("InvoiceService.updateInvoice");
		
		return(invoiceDao.merge(detatchedInvoice));
	}

	public Invoice bumpInvoiceStatus(int invoiceId) {
		return(this.bumpInvoiceStatus(invoiceId, true));
	}
	
	public Invoice bumpInvoiceStatus(int invoiceId, boolean forward) {
		Invoice transientInvoice = invoiceDao.findById(invoiceId);
		if(transientInvoice != null) {
			String status = transientInvoice.getInvoiceCustom();
			if(status.equals(Constant.STATUS_CREATED))
				transientInvoice.setInvoiceCustom(Constant.STATUS_OPEN);
			else if(status.equals(Constant.STATUS_OPEN)) {
				transientInvoice.setInvoiceCustom(Constant.STATUS_SERVED);
				transientInvoice.setExpeditedDate(new Date());
			}
			else if(status.equals(Constant.STATUS_SERVED)) {
				transientInvoice.setInvoiceCustom(Constant.STATUS_CLOSED);								
			}
			else if(status.equals(Constant.STATUS_CLOSED))
				transientInvoice.setInvoiceCustom(Constant.STATUS_PAYED);
			else
				transientInvoice.setInvoiceCustom(Constant.STATUS_CREATED);
	
			transientInvoice.setModifiedDate(new Date());
			transientInvoice.setModifiedBy(InvoiceService.class.toString());
			
			invoiceDao.persist(transientInvoice);
		}		
		
		return(transientInvoice);
	}
	
	/** 
	 * Attach a product to an order, allows duplicates.
	 */
	public Invoice addProductChild(Invoice invoice, Product productChild, String detail, Integer q) {
		Invoice persistentInvoice = invoiceDao.findById(invoice.getId());
		Product persistentProduct = productDao.findById(productChild.getId());
		if (persistentInvoice != null && persistentProduct != null) {
			InvoiceProduct ip = new InvoiceProduct(persistentInvoice, persistentProduct);
			ip.setDetail(detail);
			ip.setQuantity(q);
			persistentInvoice.getInvoiceProducts().add(ip);
			
			int sub =  persistentInvoice.getSubtotal();
			sub += persistentProduct.getPennies();
			persistentInvoice.setSubtotal(sub);

			invoiceDao.update(persistentInvoice);
		}

		return (null);
	}		
	
	public List<Invoice> getPendingUserInvoices(SystemUser user) {
		return(invoiceDao.findByUser(user, true));
	}
	
	public List<Invoice> getUserInvoices(SystemUser user) {
		return(invoiceDao.findByUser(user, false));
	}

	public Invoice getInvoice(long oId) {
		return(invoiceDao.findById(oId));
	}
	
	public int getInvoiceCount() {
		return(invoiceDao.getCount());
	}
	
	public List<Invoice> getInvoices(int first, int max) {
		return(invoiceDao.load(first, max));
	}
	
	@SuppressWarnings("unchecked")
	public List<Invoice> getInvoices(String status) {
		if(status == null)
			return(invoiceDao.loadAll());
		else
			return(invoiceDao.load(status));
	}

	/**
	 * Shared point for the general calculation of invoice
	 * monies.
	 */
	public Invoice generateTotals(Invoice invoice) {
		int sub = (invoice.getSubtotal() > 0) ? invoice.getSubtotal() : 0; 
		if(sub == 0) {
			Set<InvoiceProduct> ips = invoice.getInvoiceProducts();
			for(Iterator<InvoiceProduct> iter = ips.iterator(); iter.hasNext();) {
				sub += iter.next().getProduct().getPennies();
			}
		}
		
		//int tax = (int)Math.round(sub * SALES_TAX_MULTIPLIER);
		//invoice.setTax(tax);
		//invoice.setDue(tax + sub);
		
		return(invoice);
	}
	
	/* dependency injection */
	public void setInvoiceDao(InvoiceDao invoiceDao) {
		this.invoiceDao = invoiceDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setInvoiceProductDao(InvoiceProductDao invoiceProductDao) {
		this.invoiceProductDao = invoiceProductDao;
	}
	
	public static void main(String... strings) {}
}
