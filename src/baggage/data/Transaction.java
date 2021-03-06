/*
 * Copyright (c) 2010, 2011 Fabrice Medio.  All Rights Reserved.
 */

package baggage.data;

import org.hibernate.Session;

public interface Transaction<T> {
    public T run(Session session);
}
