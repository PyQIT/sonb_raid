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
    private int sectorOccupiedSize;
    private DamageType damageType;
    private List<String> data;
    private List<List<byte[]>> checkSum;

    public Sector(int id, int sectorSize, int sectorOccupiedSize) {
        this.id = id;
        this.sectorSize = sectorSize;
        this.sectorOccupiedSize = sectorOccupiedSize;
        damageType = DamageType.WITHOUT_DAMAGE;
        data = new ArrayList<>();
        checkSum = new ArrayList<>();
    }
}
