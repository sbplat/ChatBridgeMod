package com.sbplat.chatbridge.events;

public class Event {
    private boolean canceled = false;

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean newCanceled) {
        canceled = newCanceled;
    }
}
