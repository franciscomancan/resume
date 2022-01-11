package com.cs.stadosweb.dao;

import java.util.List;

import com.cs.stadosweb.domain.impl.SystemUser;

public interface SystemUserDao {

    public abstract SystemUser persist(SystemUser transientInstance) throws RuntimeException;

    public abstract void delete(SystemUser persistentInstance) throws RuntimeException;

    public abstract SystemUser findById(long id) throws RuntimeException;

    public abstract List<SystemUser> loadAll() throws RuntimeException;
    
    public List<SystemUser> findByUsername(String username) throws RuntimeException;
}