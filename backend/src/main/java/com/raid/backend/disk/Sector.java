package com.raid.backend.disk;


import lombok.Getter;
import lombok.Setter;

public class Sector {

    @Getter
    private int id;

    @Getter
    @Setter
    private byte[] data;

    @Getter
    private int sectorSize;

    @Getter
    @Setter
    private DamageType damageType;

    public Sector(int id, int sectorSize) {
        this.id = id;
        this.sectorSize = sectorSize;
        data = null;
        damageType = DamageType.NO_DAMAGE;
    }
}
