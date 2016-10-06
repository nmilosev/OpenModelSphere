/*
 * Jython Database Specification API 2.0
 *
 * $Id: QueueClosedException.java 2414 2005-02-23 04:26:23Z bzimmer $
 *
 * Copyright (c) 2001 brian zimmer <bzimmer@ziclix.com>
 *
 */
package com.ziclix.python.sql.util;

/**
 * This exception is thrown when the queue is closed and an operation is attempted.
 *
 * @author brian zimmer
 * @version $Revision: 2414 $
 */
public class QueueClosedException extends RuntimeException {

    /**
     * Constructor QueueClosedException
     */
    public QueueClosedException() {
        super();
    }

    /**
     * Constructor QueueClosedException
     *
     * @param msg
     */
    public QueueClosedException(String msg) {
        super(msg);
    }
}
