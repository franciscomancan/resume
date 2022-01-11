package com.cs.stadosweb.dao;

import java.util.List;

import com.cs.stadosweb.domain.impl.Menu;

public interface MenuDao {

    public abstract Menu persist(Menu transientInstance);

    public abstract Menu update(Menu transientInstance);
    
    public abstract void delete(Menu persistentInstance);

    public abstract Menu findById(long id);

    public abstract List<Menu> loadAll() throws RuntimeException;

    public List<Menu> findByExample(Menu instance);
    
    public int addCategory(long menuId, long categoryId);
}