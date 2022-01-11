package com.cs.stadosweb.dao.hibernate;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.cs.stadosweb.dao.InvoiceDao;
import com.cs.stadosweb.domain.impl.Invoice;
import com.cs.stadosweb.domain.impl.SystemUser;

public class InvoiceHibernateDao extends HibernateDaoSupport implements InvoiceDao {

	public Invoice persist(Invoice transientInstance) {
		Session session = null;
		try {
			session = getHibernateTemplate().getSessionFactory().getCurrentSession();
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
	
	public Invoice update(Invoice transientInstance) {
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

	public void delete(Invoice persistentInstance) {
		try {
			getSessionFactory().getCurrentSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Persists a new Invoice entity as the union of
	 * the detached argument along with any previously
	 * persisted data (for the id).
	 */
	public Invoice merge(Invoice detached) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.merge(detached);
			session.getTransaction().commit();
		} catch (RuntimeException re) {
			session.getTransaction().rollback();
			re.printStackTrace();
			return (null);
		}

		return (detached);
	}

	public Invoice findById(long id) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();

			Invoice instance = (Invoice) session.get(
					"com.cs.stadosweb.domain.impl.Invoice", id);

			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Invoice> loadAll() throws RuntimeException {
		return (getHibernateTemplate().loadAll(Invoice.class));
	}
	
	@Override
	public List<Invoice> load(int firstRec, int maxRecs) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery("from Invoice");
		q.setFirstResult(firstRec);
		q.setMaxResults(maxRecs);
		return(q.list());
	}
	
	public int getCount() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Query read = session.createQuery("select count(id) from Invoice ");
		Long rslt = (Long)read.uniqueResult();
		session.close();
		return(rslt.intValue());
		
		//System.out.println("\n\n\t\t" + rslt.getClass() + " : " + rslt);
		
		/* following is generating a socket error on the test box */
		//Criteria criteria = session.createCriteria(Invoice.class); 
		//criteria.setProjection(Projections.rowCount());		
		//return ((Integer)criteria.list().get(0)).intValue();
	}
	
	/** Implementation expects a comma-delimted status list to come through
	 * 	w/arguments; shortcut; not exposed extern directly.
	 */
	@SuppressWarnings("unchecked")
	public List<Invoice> load(String status) throws RuntimeException {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery("from Invoice inv where inv.invoiceCustom = :status ");
		q.setString("status", status);
		return(q.list());
	}
	
	public List<Invoice> findByUser(SystemUser usr, boolean pendingOnly) throws RuntimeException {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Invoice inv where inv.owner.username = :uname ";
		if(pendingOnly)
			hql += "and inv.invoiceCustom not in ('payed','closed') ";
		
		session.beginTransaction();
		Query q = session.createQuery(hql);
		q.setParameter("uname", usr.getUsername());
		return(q.list());		
	}

}
