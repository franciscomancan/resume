package com.cs.stadosweb.dao.hibernate;

import java.util.List;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.cs.stadosweb.dao.InvoiceProductDao;
import com.cs.stadosweb.domain.impl.InvoiceProduct;

public class InvoiceProductHibernateDao extends HibernateDaoSupport implements InvoiceProductDao {

	public InvoiceProduct persist(InvoiceProduct transientInstance) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.persist(transientInstance);
			session.getTransaction().commit();
		} catch (RuntimeException re) {
			session.getTransaction().rollback();
			re.printStackTrace();
			return (null);
		}

		return (transientInstance);
	}
	
	public InvoiceProduct update(InvoiceProduct transientInstance) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.saveOrUpdate(transientInstance);
			session.getTransaction().commit();
		} catch (RuntimeException re) {
			session.getTransaction().rollback();
			re.printStackTrace();
			return (null);
		}

		return (transientInstance);
	}

	public void delete(InvoiceProduct persistentInstance) {
		try {
			getSessionFactory().getCurrentSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public InvoiceProduct findById(long id) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();

			InvoiceProduct instance = (InvoiceProduct) session.get(
					"com.cs.stadosweb.domain.impl.InvoiceProduct", id);

			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<InvoiceProduct> loadAll() throws RuntimeException {
		return (getHibernateTemplate().loadAll(InvoiceProduct.class));
	}

}
