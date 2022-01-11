package com.cs.stadosweb.service;

import java.util.Date;

import java.util.List;
import com.cs.stadosweb.Constant;
import com.cs.stadosweb.dao.SystemUserDao;
import com.cs.stadosweb.domain.impl.SystemUser;

public class UserService implements Constant {

	public enum Role {OBSERVER,SERVER,MGR,SUPER}
	
	private SystemUserDao systemUserDao;

	private static UserService instance;
	
	private UserService() {}
	
	public static UserService getInstance() {
		if(instance == null)
			instance = new UserService();
		
		return(instance);
	}
	
	public SystemUser getUser(String username) {
		try {
			List result = systemUserDao.findByUsername(username);
			if (result.size() > 0)
				return ((com.cs.stadosweb.domain.impl.SystemUser) result.get(0));
			else
				return (null);
		} catch (Exception e) {
			e.printStackTrace();
			return (null);
		}
	}

	public SystemUser getUser(int userId) {
		try {
			return(systemUserDao.findById(userId));
		} catch (Exception e) {
			e.printStackTrace();
			return (null);
		}
	}
	
	/**
	 * Load all users from the system, will cache
	 * results based upon a hibernate impl.
	 */
	@SuppressWarnings("unchecked")
	public List<SystemUser> getUsers() {
		return (systemUserDao.loadAll());
	}

	/**
	 * Create a new instance of a user based on
	 * the populated argument object.
	 * 
	 * @param SystemUser (populated object)
	 * @return SystemUser (same object, implies success, null if failure)
	 */
	public SystemUser createUser(SystemUser user) {
		try {
			user.setCreatedDate(new Date());
			if(user.getType() == null || user.getType().equals(""))
				user.setType(ROLE_OBSERVER);
			else
				setRole(user);

			systemUserDao.persist(user);
		} catch (Exception e) {
			e.printStackTrace();
			return (null);
		}
		return (user);
	}

	/**
	 * Should be used only to update a user that has already
	 * been persisted.  The implementation does not check for
	 * this so a requery is not performed, but assumes the
	 * argument is passed with an appropriate ID.
	 * 
	 * @param SystemUser
	 */
	public void updateUser(SystemUser user) {
		systemUserDao.persist(setRole(user));
	}
	
	/**
	 * Re-assigns the string used to identify the
	 * given user's security role.  The represents
	 * the discriminator in the database.
	 */
	private SystemUser setRole(SystemUser usr) {
		if(usr.getType().equals("o")) {
			usr.setType(ROLE_OBSERVER);
		}
		else if(usr.getType().equals("s")) {
			usr.setType(ROLE_SERVER);
		}
		else if(usr.getType().equals("m")) {
			usr.setType(ROLE_MANAGER);
		}
		else if(usr.getType().equals("a")) {
			usr.setType(ROLE_SUPER);
		}
		
		return(usr);
	}
	
	public int deleteUser(String username) {
		try {
			SystemUser user = null;
			user = getUser(username);
			user.setDeletedDate(new Date());
			systemUserDao.persist(user);
		} catch (Exception e) {
			e.printStackTrace();
			return (-1);
		}

		return (0);
	}

	/* dependency injection */
	public void setSystemUserDao(SystemUserDao systemUserDao) {
		this.systemUserDao = systemUserDao;
	}
}
