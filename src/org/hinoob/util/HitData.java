package org.hinoob.util;

import org.bukkit.entity.Entity;

public class HitData {

    public long timeStamp;
    public Entity entity;

    public HitData(Entity entity, long timeStamp){
        this.entity = entity;
        this.timeStamp = timeStamp;
    }
}
