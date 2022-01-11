package com.cs.stadosweb.dao.hibernate;

/*

The hibernate implementation has been described as 'Transaction demarcation with plain JDBC'.

Since we don't have JTA and don't want to deploy it along with our application, you will usually have to 
fall back to JDBC transaction demarcation. Instead of calling the JDBC API you better use Hibernate's Transaction 
and the built-in session-per-request functionality:

Because Hibernate can't bind the "current session" to a transaction, as it does in a JTA environment, it binds it to 
the current Java thread. It is opened when getCurrentSession() is called for the first time, but in a "proxied" state 
that doesn't allow you to do anything except start a transaction. When the transaction ends, either through commit or 
roll back, the "current" Session is closed automatically. The next call to getCurrentSession() starts a new proxied Session, 
and so on. In other words, the session is bound to the thread behind the scenes, but scoped to a transaction, just like in a 
JTA environment. This thread-bound strategy works in every JSE application - note that you should use JTA and a transaction-bound 
strategy in a JEE environment (or install JTA with your JSE application, this is a modular service).

To enable the thread-bound strategy in your Hibernate configuration:

    * set hibernate.transaction.factory_class to org.hibernate.transaction.JDBCTransactionFactory
    * set hibernate.current_session_context_class to thread

This does not mean that all Hibernate Sessions are closed when a transaction is committed! Only the Session that you obtained 
with sf.getCurrentSession() is flushed and closed automatically. If you decide to use sf.openSession() and manage the Session 
yourself, you have to close() it.

*/