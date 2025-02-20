package some.tests;

import some.tests.mutex.DeficientMutex;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    var mutex = new DeficientMutex();

    var th1 =
        new Thread(
            () -> {
              mutex.lock();
              try {
                System.out.println("Thread 1 get lock");
                Thread.sleep(10000);
                System.out.println("Thread 1 leaves the lock");
              } catch (Exception e) {
                System.out.println(e.getMessage());
              } finally {
                System.out.println("Finishing leaves lock from thread 1");
                mutex.unlock();
              }
            });

    var th2 =
        new Thread(
            () -> {
              mutex.lock();
              try {
                System.out.println("Thread 2 get lock");
                Thread.sleep(2000);
                System.out.println("Thread 2 leaves the lock");
              } catch (Exception e) {
                System.out.println(e.getMessage());
              } finally {
                System.out.println("Finishing leaves lock from thread 1");
                mutex.unlock();
              }
            });

    th1.start();
    th2.start();
    th1.join();
    th2.join();
  }
}
