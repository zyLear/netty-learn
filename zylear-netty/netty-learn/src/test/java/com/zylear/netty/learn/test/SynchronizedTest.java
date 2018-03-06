package com.zylear.netty.learn.test;

/**
 * Created by xiezongyu on 2018/3/6.
 */
public class SynchronizedTest {

    public static void main(String[] args) throws InterruptedException {

        final Custom custom = new Custom();

//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                System.out.println(">>???");
//                custom.show1();
//            }
//        };
//        thread.start();

//        Thread thread2 = new Thread() {
//            @Override
//            public void run() {
//                a.show2();
//            }
//        };
//        thread2.start();



        Thread thread3 = new Thread() {
            @Override
            public void run() {
                custom.show3();
            }
        };
        thread3.start();


        Thread.sleep(4000);

        Thread thread4 = new Thread() {
            @Override
            public void run() {
                custom.show4();
            }
        };
        thread4.start();


        Thread thread5 = new Thread() {
            @Override
            public void run() {
                custom.show5();
            }
        };
        thread5.start();
        Thread.sleep(444444);



    }

}

class Custom {

    final Object loginLock = new Object();

    final Object peopleLock = new Object();


    public synchronized void show1() {
        System.out.println("show1");

        try {
            Thread.sleep(30000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized void show2() {
        System.out.println("show2");
        try {
            Thread.sleep(30000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void show3() {
        synchronized (peopleLock) {
            System.out.println("show3");
            try {
                Thread.sleep(30000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void show4() {
        synchronized (loginLock) {
            System.out.println("show4");
            try {
                Thread.sleep(30000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void show5() {

        synchronized (loginLock) {
            System.out.println("show5");
            try {
                Thread.sleep(30000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
