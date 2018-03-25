package org.gammf.proxima.interfaces;

/**
 * Interface used for listening for async task results.
 */
public interface AsyncTaskListener {

    /**
     * Callback invoked when the async task has completed its work.
     * @param result the result of the async task.
     */
    void onAsyncTaskCompletion(final String result);
}
