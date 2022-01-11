package com.cs.stadosweb.dao.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.cs.stadosweb.dao.SystemUserDao;
import com.cs.stadosweb.domain.impl.SystemUser;

public class SystemUserHibernateDao extends HibernateDaoSupport implements
		SystemUserDao {

	public SystemUser persist(SystemUser transientInstance) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.persist(transientInstance);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return(transientInstance);
	}

	public void delete(SystemUser persistentInstance) {
		getSessionFactory().getCurrentSession().delete(persistentInstance);
	}

	public SystemUser merge(SystemUser detachedInstance)
			throws RuntimeException {
		SystemUser result = (SystemUser) getSessionFactory()
				.getCurrentSession().merge(detachedInstance);

		return (result);
	}

	public SystemUser findById(long id) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
		
			return((SystemUser)session.get("com.cs.stadosweb.domain.impl.SystemUser",id));
		} 
		catch (Exception e) {
			//session.getTransaction().rollback();
			e.printStackTrace();
			return(null);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SystemUser> findByExample(SystemUser instance)
			throws RuntimeException {
		List<SystemUser> result = getSessionFactory().getCurrentSession()
				.createCriteria("com.cs.stadosweb.domain.impl.SystemUser").add(
						Example.create(instance)).list();

		return (result);
	}

	@SuppressWarnings("unchecked")
	public List<SystemUser> loadAll() throws RuntimeException {
		return (getHibernateTemplate().loadAll(SystemUser.class));
	}

	@SuppressWarnings("unchecked")
	public List<SystemUser> findByUsername(String username) throws RuntimeException {
		return (getHibernateTemplate().find(
				"from SystemUser user where user.username = ? ", username));
	}
}
