/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.util;

/**
 *
 * @author break27
 */
public class TimeUtils {
    
    public final static class Timer {
        Thread timerThread;
        long startTime;
        volatile long deltaTime;
        volatile boolean set;
        
        public Timer() {
            reset();
        }
        
        /** Freeze the program for some time
         * @param time milliseconds
         * @return always true
         */
        public boolean freeze(long time) {
            reset();
            while(true) {
                System.out.println(deltaTime);
                if(calculate() >= time) break;
            }
            return true;
        }
        
        public void setTimeout(long timeout, Trigger trigger) {
            timerThread = new Thread(() -> {
                while (set) {
                    System.out.println(deltaTime);
                    if (calculate() >= timeout && timeout > 0) {
                        trigger.fire();
                        break;
                    }
                }
            });
            timerThread.setName("TimeUtils.Timer");
            
            reset();
            set = true;
            timerThread.start();
        }
        
        public void reset() {
            set = false;
            deltaTime = 0;
            startTime = System.currentTimeMillis();
        }
        
        public void set() {
            setTimeout(0, null);
        }
        
        public float getTime() {
            return deltaTime;
        }
        
        public float finnish() {
            set = false;
            return deltaTime;
        }
        
        private float calculate() {
            long time = System.currentTimeMillis();
            deltaTime = time - startTime;
            return deltaTime;
        }
    }
    
    public static interface Trigger {
        public void fire();
    }
}