package com.cs.stadosweb.dao;

import java.util.List;
import com.cs.stadosweb.domain.impl.InvoiceProduct;

public interface InvoiceProductDao {

    public abstract InvoiceProduct persist(InvoiceProduct transientInstance);
    
    public abstract InvoiceProduct update(InvoiceProduct transientInstance);

    public abstract void delete(InvoiceProduct persistentInstance);

    public abstract InvoiceProduct findById(long id);
    
    public abstract List<InvoiceProduct> loadAll();
    
}