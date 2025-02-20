package some.tests.mutex;

public class DeficientMutex {

  private volatile boolean isLocked = false;

  public void lock() {
    while (true) {
      if (!isLocked) {
        isLocked = true;
        return;
      }
    }
  }

  public void unlock() {
    this.isLocked = false;
  }
}
