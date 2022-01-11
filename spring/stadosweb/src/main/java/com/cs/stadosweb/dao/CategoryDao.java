package com.cs.stadosweb.dao;

import java.util.List;

import com.cs.stadosweb.domain.impl.Category;

public interface CategoryDao {

    public abstract void persist(Category transientInstance);

    public abstract void delete(Category persistentInstance);

    public abstract Category findById(long id);
    
    public abstract List<Category> loadAll();
    
    public abstract Category create(Category instance);
    
    public List<Category> findByExample(Category instance);
    
}