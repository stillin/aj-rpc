package com.aj.common;

/**
 * Created by chaiaj on 2017/4/9.
 */
public interface Future {
    boolean cancel();

    /**
     * Returns <tt>true</tt> if this task was cancelled before it completed
     * normally.
     *
     * @return <tt>true</tt> if this task was cancelled before it completed
     */
    boolean isCancelled();

    /**
     * Returns <tt>true</tt> if this task completed.
     *
     */
    boolean isDone();

    /**
     *
     */
    Object get();

    /**
     *
     */
    Object get(int timeout);
}
