package some.tests.mutex;

public class Mutex {

  private final Object monitor = new Object();
  private boolean isLocked = false;

  public void lock() {
    final int loopsLimit = 254;

    for (int i = 0; i < loopsLimit; i++) {
      Thread.onSpinWait();

      if (!this.isLocked) {
        this.isLocked = true;
        return;
      }
    }

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
