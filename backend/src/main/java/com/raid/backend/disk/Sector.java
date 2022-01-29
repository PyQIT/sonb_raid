package com.raid.backend.disk;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sector {

    private int id;
    private byte[] data;
    private int sectorSize;
    private DamageType damageType;

    public Sector(int id, int sectorSize) {
        this.id = id;
        this.sectorSize = sectorSize;
        data = null;
        damageType = DamageType.WITHOUT_DAMAGE;
    }
}
