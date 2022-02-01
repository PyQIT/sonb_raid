package com.raid.backend.disk;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Sector {

    private int id;
    private int sectorSize;
    private DamageType damageType;
    private List<String> data;
    private List<List<byte[]>> checkSum;

    public Sector(int id, int sectorSize) {
        this.id = id;
        this.sectorSize = sectorSize;
        damageType = DamageType.WITHOUT_DAMAGE;
        data = new ArrayList<>();
        checkSum = new ArrayList<>();
    }
}
