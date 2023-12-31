package com.seblacko.rag.util.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class RowChecker {
    public static boolean rowExists(String tableName, String columnName, Object columnValue) {
//        table name should be entity name eg: Employee not employee from database table
        SessionFactory sessionFactory = InitialSessionFactory.getSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        boolean isRowExists = false;
        int rows = 0;
        try {
            transaction.begin();
            String hql = "from "+tableName+" as dep where dep."+columnName+" =:value";
            Query<Object> query = session.createQuery(hql);
            query.setParameter("value", columnValue);
            rows = query.list().size();
            transaction.commit();

        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        if (rows >= 1) {
            isRowExists = true;
        }else{
            isRowExists = false;
        }
        return isRowExists;
    }
    public static <T>T getEntityByColumn(Class<T> entity, String columnName, Object value){
        SessionFactory sessionFactory = InitialSessionFactory.getSessionFactory();
        Session session = sessionFactory.openSession();
        String hql = "from "+entity.getName()+" as tb where tb."+columnName+" =:value";
        Query<T> query = session.createQuery(hql);
        query.setParameter("value",value);
        T tb = query.uniqueResult();
        session.close();
        return tb;
    }
}
