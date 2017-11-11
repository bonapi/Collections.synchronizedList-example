//this code will give errors and unpredictable behaviour

public class Main {
  public static void main(String[] args) {

		List<Object> list = new LinkedList<>();

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < 20; i++) {
					list.add(1);
				}

			}
		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < 20; i++) {
					list.add(2);
				}

			}
		});

		t1.start();
		t2.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.print(list);
    
  }
}

//to fix this we can use Collections.synchronizedList(line 52) to wrap the list we are using

public class Main {
  public static void main(String[] args) {

    //List<Object> list = new LinkedList<>();
		List<Object> list = Collections.synchronizedList(new LinkedList<>());

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < 20; i++) {
					list.add(1);
				}

			}
		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < 20; i++) {
					list.add(2);
				}

			}
		});

		t1.start();
		t2.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.print(list);
    
  }
}
//output:
[1, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 2, 2]

//however the order in which elements are set is still unpredictable,
//to fix this we need to use synchronized blocks

public class Main {
  public static void main(String[] args) {

    //List<Object> list = new LinkedList<>();
		List<Object> list = Collections.synchronizedList(new LinkedList<>());

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {

        synchronized (list) {
          for (int i = 0; i < 20; i++) {
            list.add(1);
          }
        }

			}
		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {

				synchronized (list) {
          for (int i = 0; i < 20; i++) {
					  list.add(2);
          }
        }

			}
		});

		t1.start();
		t2.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.print(list);
    
  }
}
//output:
[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]

//and if we comment the first synchronized block t1 will add its numbers until t2 starts its synchronized block
//at which point t2 acquires the lock over the list and t1 has to wait until t2 adds all of its numbers

public class Main {
  public static void main(String[] args) {

    //List<Object> list = new LinkedList<>();
		List<Object> list = Collections.synchronizedList(new LinkedList<>());

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {

        //synchronized (list) {
          for (int i = 0; i < 20; i++) {
            list.add(1);
          }
        //}

			}
		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {

				synchronized (list) {
          for (int i = 0; i < 20; i++) {
					  list.add(2);
          }
        }

			}
		});

		t1.start();
		t2.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.print(list);
    
  }
}

//output:
[1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
//or
[1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
//or similar where all 2's are combined
