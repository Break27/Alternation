package com.github.break27.util;

/**
 *
 * @author break27
 */
public class Timer {
    TimerThread timerThread = new TimerThread();

    long startTime;
    volatile boolean set;
    volatile long deltaTime;

    public Timer() {
        reset();
    }

    /** Freeze the program for some time
     * @param time milliseconds
     */
    public void freeze(long time) {
        reset();
        while(set = true) {
            if(calculate() >= time) {
                reset();
                break;
            }
        }
    }

    public void setTimeout(long timeout, Trigger trigger) {
        reset();
        set = true;
        timerThread = new TimerThread(timeout, trigger);
        timerThread.start();
    }

    public void interval(long interval, int attempts, Trigger trigger) {
        loop: for(int i=0; i < attempts; i++) {
            setTimeout(interval, trigger);

            while(timerThread.isAlive()) {
                if (!set) break loop;
            }
        }
    }

    public void reset() {
        timerThread.interrupt();
        set = false;
        deltaTime = 0;
        startTime = System.currentTimeMillis();
    }

    public void set() {
        set = true;
        setTimeout(0, null);
    }

    public float getTime() {
        return deltaTime;
    }

    public float finnish() {
        timerThread.interrupt();
        set = false;
        return deltaTime;
    }

    private float calculate() {
        long time = System.currentTimeMillis();
        deltaTime = time - startTime;
        return deltaTime;
    }

    public interface Trigger {
        void fire();
    }

    private class TimerThread extends Thread {
        private long timeout;
        private Trigger trigger;

        public TimerThread() {
            this(0, null);
        }

        public TimerThread(long timeout, Trigger trigger) {
            setName("TimerUtils.timer");
            this.timeout = timeout;
            this.trigger = trigger;
        }

        @Override
        public void run() {
            while(!isInterrupted()) {
                if(calculate() >= timeout && timeout > 0) {
                    trigger.fire();
                    break;
                }
            }
        }
    }
}
