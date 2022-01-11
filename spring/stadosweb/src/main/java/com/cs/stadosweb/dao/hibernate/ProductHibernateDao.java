package com.cs.stadosweb.dao.hibernate;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.cs.stadosweb.dao.ProductDao;
import com.cs.stadosweb.domain.impl.Product;

public class ProductHibernateDao extends HibernateDaoSupport implements
		ProductDao {

	public Product persist(Product transientInstance) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.persist(transientInstance);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return (null);
		}
		return (transientInstance);
	}
	
	public Product update(Product transientInstance) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.saveOrUpdate(transientInstance);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return (null);
		}
		return (transientInstance);
	}

	public void delete(Product persistentInstance) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.delete(persistentInstance);
			session.getTransaction().commit();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Product findById(long id) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Product instance = (Product) session.get("com.cs.stadosweb.domain.impl.Product", id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public Product findByTitle(String title) {
		try {
			List result = getHibernateTemplate().find("from Product prod where prod.title = ? ", title);
			if(result != null && result.size() > 0)
				return((Product)result.get(0));
			else
				return(null);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findByExample(Product instance) {
		// //log.debug("finding Product instance by example");
		try {
			List results = getSessionFactory().getCurrentSession()
					.createCriteria("com.cs.stadosweb.domain.impl.Product").add(
							Example.create(instance)).list();
			// //log.debug("find by example successful, result size: "
			// + results.size());
			return results;
		} catch (RuntimeException re) {
			// //log.error("find by example failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Product> loadAll() throws RuntimeException {
		return (getHibernateTemplate().loadAll(Product.class));
	}

	public int removeProductCategories(long productId) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			
			int updates = session.createSQLQuery("delete from stadosdb.productcategory where productId = :productId")
				.setLong("productId", productId)
				.executeUpdate();
			
			session.getTransaction().commit();
			return (updates);

		} catch (RuntimeException re) {
			if (session != null)
				session.getTransaction().rollback();
			
			throw re;
		}
	}

}
