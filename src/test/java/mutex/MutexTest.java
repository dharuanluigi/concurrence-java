package mutex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import some.tests.mutex.Counter;
import some.tests.mutex.Mutex;

@Execution(ExecutionMode.CONCURRENT)
public class MutexTest {

  @Test
  public void whenLockIsNotUsedShouldHasDataInconsistencyTest() throws InterruptedException {
    final int MAX_THREADS = 30;
    final int MAX_INCREMENTS = 1000;
    var counter = new Counter();

    Runnable task =
        () -> {
          for (var i = 0; i < MAX_INCREMENTS; i++) {
            counter.increment();
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }
        };

    var threads = new Thread[MAX_THREADS];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(task);
      threads[i].start();
    }

    for (var thread : threads) {
      thread.join();
    }

    System.out.println(
        "Should failed scenario, final counter value should be: "
            + MAX_INCREMENTS * MAX_THREADS
            + " but was: "
            + counter.getCount());

    assertTrue(
        counter.getCount() < MAX_INCREMENTS * MAX_THREADS,
        "The total value should be lesser than: "
            + MAX_INCREMENTS * MAX_THREADS
            + " because of race condition");
  }

  @Test
  public void whenMutexLockIsUsedShould_NOT_HasDataInconsistencyTest() throws InterruptedException {
    final int MAX_THREADS = 30;
    final int MAX_INCREMENTS = 1000;
    var mutex = new Mutex();
    var counter = new Counter();

    Runnable task =
        () -> {
          mutex.lock();
          for (var i = 0; i < MAX_INCREMENTS; i++) {
            counter.increment();
          }
          mutex.unlock();
        };

    var threads = new Thread[MAX_THREADS];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(task);
      threads[i].start();
    }

    for (var thread : threads) {
      thread.join();
    }

    System.out.println(
        "Final counter value should be: "
            + MAX_INCREMENTS * MAX_THREADS
            + " but was: "
            + counter.getCount());

    assertEquals(
        MAX_INCREMENTS * MAX_THREADS,
        counter.getCount(),
        "Success total should be equals, because there is lock mechanism to avoid race condition");
  }
}
