package com.cs.stadosweb.dao;

import java.util.List;

import com.cs.stadosweb.domain.impl.Invoice;
import com.cs.stadosweb.domain.impl.SystemUser;

public interface InvoiceDao {

    public abstract Invoice persist(Invoice transientInstance);
    
    public abstract Invoice update(Invoice transientInstance);

    public Invoice merge(Invoice detached);
    
    public abstract void delete(Invoice persistentInstance);

    public abstract Invoice findById(long id);
    
    public abstract List<Invoice> loadAll();
    
    public abstract List<Invoice> load(int firstRec, int maxRecs);
    
    public abstract List<Invoice> load(String status);
    
	public abstract List<Invoice> findByUser(SystemUser usr, boolean pendingOnly) throws RuntimeException;

	public abstract int getCount();
}