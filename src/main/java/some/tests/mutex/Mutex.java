package some.tests.mutex;

public class Mutex {

  private final Object monitor = new Object();
  private boolean isLocked = false;

  public void lock() {
    synchronized (this.monitor) {
      while (this.isLocked) {
        try {
          this.monitor.wait();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }

      this.isLocked = true;
    }
  }

  public void unlock() {
    synchronized (this.monitor) {
      this.isLocked = false;
      this.monitor.notify();
    }
  }
}
