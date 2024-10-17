package org.achl.di.entities;

/**
 * @apiNote Interface that objects that can be stored in a database should implement
 */
public interface DBObject
{
    /**
     * @apiNote Returns the name of the query each object should use to be inserted into the database
     * @return String containing the query name
     */
    String getInsertionQueryName();

    String getInsertionReadBackQueryName(String readBackField);

    /**
     * @apiNote Returns the name of the query each object should use to update itself on the database
     * @return String containing the query name
     */
    String getUpdateQueryName();
    /**
     * @apiNote Returns the name of the query each object should use to be loaded from the database
     * @return String containing the query name
     */
    String getLoadQueryName(String fieldName);
    String getDeleteQueryName(String fieldName);
}
