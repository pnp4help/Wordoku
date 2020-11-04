/*
 * This file is responsible for calculating the time.
 * Created on: 16-10-2020
 * Author: Patva Viraj(19IT117)
 */

package Wordoku;

public class StopWatch {
    private long startTime = 0;
    private long stopTime = 0;
    private long pausedTime = 0;
    private boolean running = false;
    long elapsed = 0;

    //Method that will be executed when timer starts
    public void start() {
        this.startTime = System.currentTimeMillis() + 19800000;//Milliseconds adjusted according to IST
        pausedTime = 0;
        this.running = true;
    }

    //Method that will note the stop time
    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }

    //Method that will hold the paused time
    public void pause() {
        if (running) {
            pausedTime = System.currentTimeMillis();
            stop();
        }
    }

    //Method that will  resume counting the time
    public void resume() {
        if (!running) {
            long duration = System.currentTimeMillis() - pausedTime;
            startTime += duration;
            this.running = true;
        }
    }

    //Method that will calculate total elapsed time
    public long getElapsedTime() {
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }
}
