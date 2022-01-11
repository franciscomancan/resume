package com.cs.stadosweb.dao;

import java.util.List;

import com.cs.stadosweb.domain.impl.Product;

public interface ProductDao {

    public abstract Product persist(Product transientInstance);
    
    public abstract Product update(Product transientInstance);

    public abstract void delete(Product persistentInstance);

    public abstract Product findById(long id);

    public abstract List<Product> loadAll() throws RuntimeException;
    
    public abstract Product findByTitle(String title);
    
    public abstract int removeProductCategories(long productId);
}