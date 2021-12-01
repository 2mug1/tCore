package com.github.iamtakagi.tcore.task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class ScheduledExecutor {
    public void scheduleRegularly(Runnable task, LocalDateTime firstTime,
                                  Function<LocalDateTime, LocalDateTime> nextTime) {
        pendingTask = task;
        scheduleRegularly(firstTime, nextTime);
    }

    protected void scheduleRegularly(LocalDateTime firstTime,
                                     Function<LocalDateTime, LocalDateTime> nextTime) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                scheduleRegularly(nextTime.apply(firstTime), nextTime);
                pendingTask.run();
            }
        }, Date.from(firstTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    private volatile Runnable pendingTask = null;
}
