package com.cs.stadosweb.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.cs.stadosweb.dao.MenuDao;
import com.cs.stadosweb.domain.impl.Menu;

public class MenuHibernateDao extends HibernateDaoSupport implements MenuDao {

	public Menu persist(Menu transientInstance) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();			
			session.persist(transientInstance);
			session.getTransaction().commit();
			return(transientInstance);
		}
		catch (RuntimeException re) {
			session.getTransaction().rollback();
			throw re;
		}
	}
	
	public Menu update(Menu transientInstance) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();			
			session.saveOrUpdate(transientInstance);
			session.getTransaction().commit();
			return(transientInstance);
		}
		catch (RuntimeException re) {
			session.getTransaction().rollback();
			throw re;
		}
	}

	public void delete(Menu persistentInstance) {
		System.out.println("deleting Menu instance");
		try {
			getSessionFactory().getCurrentSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			// log.error("delete failed", re);
			throw re;
		}
	}

	public Menu merge(Menu detachedInstance) {
		System.out.println("merging Menu instance");
		try {
			Menu result = (Menu) getSessionFactory().getCurrentSession().merge(
					detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Menu findById(long id) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			return((Menu) session.get("com.cs.stadosweb.domain.impl.Menu",id));
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findByExample(Menu instance) {
		System.out.println("finding Menu instance by example");
		try {
			List results = getSessionFactory().getCurrentSession()
					.createCriteria("com.cs.stadosweb.domain.impl.Menu").add(
							Example.create(instance)).list();
			return results;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Menu> loadAll() throws RuntimeException {
		return (getHibernateTemplate().loadAll(Menu.class));
	}

	public int addCategory(long menuId, long categoryId) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();

			Query insert = session
					.createSQLQuery("insert into stadosdb.menucategory (menuid,categoryid) "
							+ "values (" + menuId + "," + categoryId + ")");

			int updates = insert.executeUpdate();
			session.getTransaction().commit();
			return (updates);

		} catch (RuntimeException re) {
			if (session != null)
				session.getTransaction().rollback();

			throw re;
		}
	}
}
