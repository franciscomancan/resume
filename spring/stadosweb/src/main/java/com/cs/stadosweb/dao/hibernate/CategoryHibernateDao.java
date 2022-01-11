package com.cs.stadosweb.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.cs.stadosweb.dao.CategoryDao;
import com.cs.stadosweb.domain.impl.Category;

public class CategoryHibernateDao extends HibernateDaoSupport implements CategoryDao {

	public void persist(Category transientInstance) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.update(transientInstance);
			session.getTransaction().commit();

		} catch (RuntimeException re) {
			if (session != null)
				session.getTransaction().rollback();
			throw re;

		}
	}

	public int addProduct(int categoryId, int productId) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			// session.update(transientInstance);

			Query insert = session
					.createSQLQuery("insert into stadosdb.productcategory (categoryid,productid) "
							+ "values (" + categoryId + "," + productId + ")");

			int updates = insert.executeUpdate();
			session.getTransaction().commit();
			return (updates);

		} catch (RuntimeException re) {
			if (session != null)
				session.getTransaction().rollback();
			throw re;
		}
	}	

	public Category create(Category transientInstance) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();

			session.saveOrUpdate(transientInstance);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null)
				session.getTransaction().rollback();

			e.printStackTrace();
			return (null);
		}
		return (transientInstance);
	}

	public void delete(Category persistentInstance) {
		try {
			getSessionFactory().getCurrentSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Category merge(Category detachedInstance) {
		try {
			Category result = (Category) getSessionFactory()
					.getCurrentSession().merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Category findById(long id) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();

			Category instance = (Category) session.get(
					"com.cs.stadosweb.domain.impl.Category", id);

			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Category> findByExample(Category instance) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();

			List results = session.createCriteria(
					"com.cs.stadosweb.domain.impl.Menu").add(
					Example.create(instance)).list();

			return results;
		} catch (Exception e) {
			if (session != null)
				session.getTransaction().rollback();

			e.printStackTrace();
			return (null);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Category> loadAll() throws RuntimeException {
		return (getHibernateTemplate().loadAll(Category.class));
	}
}
