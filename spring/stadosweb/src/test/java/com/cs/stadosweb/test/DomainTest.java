package com.cs.stadosweb.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import com.cs.stadosweb.dao.CategoryDao;
import com.cs.stadosweb.dao.InvoiceDao;
import com.cs.stadosweb.dao.ProductDao;
import com.cs.stadosweb.dao.SystemUserDao;
import com.cs.stadosweb.domain.impl.Category;
import com.cs.stadosweb.domain.impl.Invoice;
import com.cs.stadosweb.domain.impl.InvoiceProduct;
import com.cs.stadosweb.domain.impl.Product;
import com.cs.stadosweb.domain.impl.SystemUser;

public class DomainTest extends AbstractDependencyInjectionSpringContextTests {

	ProductDao productDao;
	CategoryDao categoryDao;
	InvoiceDao invoiceDao;
	SystemUserDao userDao;
	
	@Override
	protected String[] getConfigLocations() {
		String[] context = {"classpath:/test-stados-dispatcher-servlet.xml"};		
		return(context);
	}	
	
	@Override
	protected void onSetUp() throws Exception {
		productDao =  (ProductDao)applicationContext.getBean("productDao");
		categoryDao = (CategoryDao)applicationContext.getBean("categoryDao");
		invoiceDao = (InvoiceDao)applicationContext.getBean("invoiceDao");
		userDao = (SystemUserDao)applicationContext.getBean("systemUserDao");
	}
	
	public void testBogus() {
		assertTrue(true);
	}
	
	public void testProductPersistence() {
		Product p = new Product();
		Category c = new Category();
		String testProduct = "test product for stados";
		String testCategory = "test category for stados";
		
		c.setTitle(testCategory);
		c.setCreatedDate(new Date());
		categoryDao.create(c);
		
		p.setCategory(c);
		p.setPennies(1000);
		p.setCreatedDate(new Date());
		p.setTitle(testProduct);
		
		productDao.persist(p);
		
		Product retrieved = productDao.findByTitle(testProduct);
		if(retrieved == null || !retrieved.getTitle().equals(testProduct))
			fail();
	}
	
	public void testInvoicePersistence() {
		Invoice order = new Invoice();
		order.setCreatedDate(new Date());
		order.setLocation("38");
		
		int limit = 100;
		Set<InvoiceProduct> invoiceProducts = new HashSet<InvoiceProduct>();		
		Product current;
		InvoiceProduct ip;
		for(int i=0; i<limit; ++i) {
			current = new Product();
			current.setTitle("product" + i);
			current.setCreatedDate(new Date());
			current.setPennies(1050);
			productDao.persist(current);
			
			ip = new InvoiceProduct(order, current, "some detail", 1);
			invoiceProducts.add(ip);
		}
	
		Product water = new Product("water");
		productDao.persist(water);
		invoiceProducts.add(new InvoiceProduct(order, water, "",1));
		invoiceProducts.add(new InvoiceProduct(order, water, "",1));
		invoiceProducts.add(new InvoiceProduct(order, water, "",1));
		invoiceProducts.add(new InvoiceProduct(order, water, "",5));
		
		order.setInvoiceProducts(invoiceProducts);
		order = invoiceDao.persist(order);
	
	}

	public void testUserInvoiceQuery() {
		SystemUser usr = new SystemUser();
		String testId = "testerosa";
		usr.setUsername(testId);
		usr.setPassword(testId);
		userDao.persist(usr);		
		usr = userDao.findByUsername(testId).iterator().next();
		
		Product cheese = productDao.persist(new Product("cheese"));
		Product rice = productDao.persist(new Product("rice"));
		Product pork = productDao.persist(new Product("pork"));
		
		int limit = 10;
		List<Invoice> invoices = new ArrayList<Invoice>(limit);
		Set<InvoiceProduct> sampleProducts = new HashSet<InvoiceProduct>(3);
		for(int i=0; i<limit; ++i) {
			Invoice tmp = new Invoice();
			tmp.setOwner(usr);
			tmp.setLocation("bogus");
			tmp.setCustomer("johnny rocket");
			tmp.setInvoiceCustom("open");
			sampleProducts.clear();
			sampleProducts.add(new InvoiceProduct(tmp, cheese, "many", 5));
			sampleProducts.add(new InvoiceProduct(tmp, rice));
			sampleProducts.add(new InvoiceProduct(tmp, pork));
			tmp.setInvoiceProducts(sampleProducts);
			invoiceDao.persist(tmp);
		}
		
		List<Invoice> persistedInvoices = invoiceDao.findByUser(usr, true);
		System.out.println("\n\nnum returned: " + persistedInvoices.size() + "\n\n");
		if(persistedInvoices.size() != limit)
			fail();
	}	
}
