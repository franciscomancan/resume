package com.cs.stadosweb.service;

import java.util.Date;

import java.util.List;
import com.cs.stadosweb.dao.CategoryDao;
import com.cs.stadosweb.dao.MenuDao;
import com.cs.stadosweb.dao.ProductDao;
import com.cs.stadosweb.domain.impl.Category;
import com.cs.stadosweb.domain.impl.Menu;
import com.cs.stadosweb.domain.impl.Product;

public class MenuService {
	
	private ProductDao productDao;
	private MenuDao menuDao;
	private CategoryDao categoryDao;

	private static MenuService instance;
	
	private MenuService() {}
	
	public static MenuService getInstance() {
		if(instance == null)
			instance = new MenuService();
		
		return(instance);
	}
	
	public Category getCategory(int catId) {
		return(categoryDao.findById(catId));
	}
	
	public Product createProduct(Product product) {
		product.setCreatedDate(new Date());
		return(productDao.persist(product));
	}
	
	public int deleteProduct(int productId) {
		Product persistentInstance = productDao.findById(productId);
		if(persistentInstance != null) {
			productDao.delete(persistentInstance);
			return(0);
		}
		else
			return(-1);
	}
	
	public int deleteProductCategory(long productId) {
		if(productDao.removeProductCategories(productId) > 0)
			return(0);
		else
			return(-1);			
	}

	public Menu createMenu(Menu menu) {
		menu.setCreatedDate(new Date());
		return (menuDao.persist(menu));
	}
	
	public void persistMenu(Menu menu) {
		menuDao.persist(menu);
	}

	public Category createCategory(Category category) {
		category.setCreatedDate(new Date());
		categoryDao.create(category);
		return (category);
	}

	public Menu addCategoryChild(Menu menu, Category category) {
		Category persistentCategory = categoryDao.findById(category.getId());
		Menu persistentMenu = menuDao.findById(menu.getId());
		if (persistentCategory != null && persistentMenu != null) {
			//int updates = menuDao.addCategory(persistentMenu.getId(),	persistentCategory.getId());
			persistentMenu.getCategories().add(persistentCategory);
			menuDao.update(persistentMenu);
		}

		return (null);
	}

	public Category addProductChild(Category category, Product productChild) {
		Category persistentCategory = categoryDao.findById(category.getId());
		Product persistentProduct = productDao.findById(productChild.getId());
		if (persistentCategory != null && persistentProduct != null) {
			persistentProduct.setCategory(persistentCategory);
			persistentProduct = productDao.update(persistentProduct);
		}

		return(persistentProduct.getCategory());
	}

	public Product getProduct(String title) {
		return(productDao.findByTitle(title));
	}
	
	public List<Product> getAllProducts() {
		return (productDao.loadAll());
	}

	public List<Menu> getAllMenus() {
		return (menuDao.loadAll());
	}
	
	public Menu getMenu(int menuId) {
		return(menuDao.findById(menuId));
	}

	public List<Category> getAllCategories() {
		return (categoryDao.loadAll());
	}

	/* dependency injection */
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

}
