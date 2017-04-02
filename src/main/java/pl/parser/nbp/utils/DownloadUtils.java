package pl.parser.nbp.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;

public final class DownloadUtils
{
  /**
   * Simple method for synchronically downloading files.
   * @param strUrl - location of file to download
   * @return contents of downloaded file
   * @throws IOException - error during download
   */
  public static String download(String strUrl) throws IOException
  {
    URL url = new URL(strUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    return IOUtils.toString(connection.getInputStream());
  }
  
  /**
   * Creates new pool for downloading concurrently multiple files.
   * @return pool
   */
  public static DownloadPool getPool()
  {
    return new DownloadPool();
  }
  
  public static class DownloadPool
  {
    private final ForkJoinPool pool = new ForkJoinPool(8);
    
    private final List<Future<?>> futures = new CopyOnWriteArrayList<>();
    
    /**
     * Submits task to this pool.
     * Task will be run in background.
     * @param task
     */
    public void submit(Runnable task)
    {
      futures.add(pool.submit(task));
    }
    
    /**
     * Waits (blocks current thread) until all tasks are done.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void waitFor() throws InterruptedException, ExecutionException
    {
      for (Future<?> f : futures)
        f.get();
      pool.shutdown();
    }
  }
}
