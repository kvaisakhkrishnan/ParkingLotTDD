package com.example;

public class DummySecurityObserver implements SecurityObserver {
    private boolean notifiedFull = false;

    @Override
    public void notify(boolean isFull) {
        this.notifiedFull = isFull;
    }

    public boolean isNotifiedFull() {
        return notifiedFull;
    }
}

