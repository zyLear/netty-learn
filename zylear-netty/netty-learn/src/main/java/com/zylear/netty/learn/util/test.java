package com.zylear.netty.learn.util;

class A {

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

    public synchronized void show3() {
        synchronized (peopleLock) {
            System.out.println("show3");
            try {
                Thread.sleep(30000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized void show4() {
        synchronized (loginLock) {
            System.out.println("show4");
            try {
                Thread.sleep(30000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void show5() {

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
