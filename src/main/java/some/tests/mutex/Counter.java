package some.tests.mutex;

public class Counter {
  private int count = 0;

  public void increment() {
    this.count++;
  }

  public int getCount() {
    return this.count;
  }
}
