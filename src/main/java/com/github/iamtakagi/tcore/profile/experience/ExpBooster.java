package com.github.iamtakagi.tcore.profile.experience;

import lombok.Data;

import java.util.UUID;

@Data
public class ExpBooster {

    public static ExpBoosterJsonSerializer SERIALIZER = new ExpBoosterJsonSerializer();
    public static ExpBoosterJsonDeserializer DESERIALIZER = new ExpBoosterJsonDeserializer();

    private long addedAt;
    private long duration;
    private int increaseRate;
    private String addedReason;

    private UUID addedBy;

    private boolean removed;
    private long removedAt;
    private UUID removedBy;
    private String removedReason;

    public ExpBooster(long addedAt, long duration, int increaseRate, String addedReason){
        this.addedAt = addedAt;
        this.duration = duration;
        this.increaseRate = increaseRate;
        this.addedReason = addedReason;
    }

    public boolean isPermanent(){
        return duration == Integer.MAX_VALUE;
    }

    public boolean hasExpired(){
        return (!isPermanent()) && (System.currentTimeMillis() >= addedAt + duration);
    }

    public boolean isActive(){
        return (!removed && !hasExpired());
    }
}
