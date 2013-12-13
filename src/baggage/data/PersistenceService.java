/*

 * Copyright (c) 2010, 2011 Fabrice Medio.  All Rights Reserved.
 */

package baggage.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PersistenceService {
    private SessionFactory sessionFactory;

    public PersistenceService(String configFile, String mappingFile) {
        Configuration configuration = new Configuration();
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(configFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        properties.putAll(System.getProperties());
        configuration.setProperties(properties);
        configuration.addFile(mappingFile);
        sessionFactory = configuration.buildSessionFactory();
    }

    public PersistenceService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> T execute(Transaction<T> transaction) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        T tee = transaction.run(session);
        session.getTransaction().commit();
        session.flush();
        session.close();
        return tee;
    }

    public <T extends Named> T load(final Class<T> klass, final long id) {
        T tee = execute(new Transaction<T>() {
            @Override
            public T run(Session session) {
                T result = (T) session.createCriteria(klass)
                        .add(Restrictions.eq("id", id))
                        .uniqueResult();
                result.initialize();
                return result;
            }
        });
        return tee;
    }

    public <T extends Persistable> T update(final T tee) {
        return execute(new Transaction<T>() {
            @Override
            public T run(Session session) {
                session.update(tee);
                tee.initialize();
                return tee;
            }
        });
    }

    public <T extends Persistable> T create(final T tee) {
        return execute(new Transaction<T>() {
            @Override
            public T run(Session session) {
                session.save(tee);
                tee.initialize();
                return tee;
            }
        });
    }

    public <T extends Persistable> void delete(final T tee) {
        execute(new Transaction<Object>() {
            @Override
            public Object run(Session session) {
                session.delete(tee);
                return null;
            }
        });
    }
}
