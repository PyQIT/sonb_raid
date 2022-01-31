package com.raid.backend.disk;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class Disk {
    private final List<Sector> sectors = new ArrayList<>();
    private final int diskId;
    private final int sectorSize;
    private final int numberOfSectors;
    private boolean isCheckSumDisk;

    public Disk(int diskId){
        this.diskId = diskId;
        this.sectorSize = 32;
        this.numberOfSectors = 128;

        for (int i = 0; i < numberOfSectors; i++) {
            Sector sector = new Sector(i, sectorSize);
            sectors.add(sector);
        }
    }

    public void clearSectors() {
        for (Sector sector : sectors) {
            sector.setData(null);
        }
    }

    public void clearSector(int id) {
        for (Sector sector : sectors) {
            if (sector.getId() == id) {
                sector.setData(null);
            }
        }
    }

    public int getFreeSpaceSize() {
        int freeSpaceSize = 0;
        for (Sector sector : sectors) {
            if (sector.getData() == null) {
                freeSpaceSize += sector.getSectorSize();
            }
        }
        return freeSpaceSize;
    }

    public int getbackendUsageSize() {
        int backendUsage = 0;
        for (Sector sector : sectors) {
            if (sector.getData() != null) {
                backendUsage += sector.getSectorSize();
            }
        }
        return backendUsage;
    }

    public int getTotalSpaceSize() {
        return sectorSize * numberOfSectors;
    }

    public int countPercentUsage() {
        return (int) ((double) getbackendUsageSize() / (double) getTotalSpaceSize() * 100);
    }

    public List<Integer> printFreeSectors() {
        List<Sector> freeSectors = new ArrayList<>();
        for (Sector sector : sectors) {
            if (sector.getData() == null && sector.getDamageType().equals(DamageType.WITHOUT_DAMAGE)) {
                freeSectors.add(sector);
            }
        }
        List<Integer> freeSectorsIds = new ArrayList<>();
        for (Sector sector : freeSectors) {
            freeSectorsIds.add(sector.getId());
        }
        return freeSectorsIds;
    }

    public List<Integer> printSectorsInUse() {
        List<Sector> sectorsInUsage = new ArrayList<>();
        for (Sector sector : sectors) {
            if (sector.getData() != null && sector.getDamageType().equals(DamageType.WITHOUT_DAMAGE)) {
                sectorsInUsage.add(sector);
            }
        }
        List<Integer> sectorsInUsageIds = new ArrayList<>();
        for (Sector sector : sectorsInUsage) {
            sectorsInUsageIds.add(sector.getId());
        }
        return sectorsInUsageIds;
    }

    public void damageSector(int sectorId, Integer damageType) throws Exception {
        Sector sectorToDamage = findSector(sectorId);
        if (damageType == DamageType.SECTOR_MULFUNCTION.ordinal()) {
            sectorToDamage.setData(null);
            sectorToDamage.setDamageType(DamageType.SECTOR_MULFUNCTION);
        } if (damageType == DamageType.VOLTAGE_SURGE.ordinal()) {
            sectorToDamage.setDamageType(DamageType.VOLTAGE_SURGE);
        } if (damageType == DamageType.VIBRATION_DAMAGE.ordinal()) {
            sectorToDamage.setDamageType(DamageType.VIBRATION_DAMAGE);
        }
    }

    public Sector findSector(int sectorId) throws Exception {
        for (Sector sector : sectors) {
            if (sector.getId() == sectorId && sector.getDamageType().equals(DamageType.WITHOUT_DAMAGE)) {
                return sector;
            }
        }
        throw new Exception("Sector with id = " + sectorId + " not exist");
    }

    public List<Integer> printPermanentlyDamagedSectors() {
        List<Integer> sectorsPermamentlyDamaged = new ArrayList<>();
        for (Sector sector : sectors) {
            if (sector.getDamageType().equals(DamageType.SECTOR_MULFUNCTION)) {
                sectorsPermamentlyDamaged.add(sector.getId());
            }
        }
        return sectorsPermamentlyDamaged;
    }

    public List<Integer> printTemporaryDamagedSectors() {
        return sectors.stream().filter(sector -> sector.getDamageType().equals(DamageType.VOLTAGE_SURGE))
                .map(Sector::getId)
                .collect(Collectors.toList());
    }

    public int writeData(byte[] writeData) throws Exception {
        for (Sector sector : sectors) {
            if ((sector.getData() == null || sector.getData().length == 0)) {
                if (sector.getDamageType().equals(DamageType.SECTOR_MULFUNCTION)) {
                    break;
                } if (sector.getDamageType().equals(DamageType.VOLTAGE_SURGE)) {
                    sector.setDamageType(DamageType.WITHOUT_DAMAGE);
                    break;
                }
                int end = Math.min(writeData.length, sectorSize);
                byte[] toSave = new byte[sector.getSectorSize()];
                System.arraycopy(writeData, 0, toSave, 0, end);
                sector.setData(toSave);
                return sector.getId();
            }
        }
        throw new Exception("backend is full or some sectors are damaged. Cannot save more data");
    }

    public byte[] readData(int id) throws Exception {
        for (Sector sector : sectors) {
            if (sector.getId() == id) {
                if (sector.getDamageType().equals(DamageType.SECTOR_MULFUNCTION)) {
                    break;
                } if (sector.getDamageType().equals(DamageType.VOLTAGE_SURGE)) {
                    sector.setDamageType(DamageType.WITHOUT_DAMAGE);
                    break;
                }
                if (sector.getData() != null && sector.getData().length > 0) {
                    return sector.getData();
                }
                throw new Exception("Sector with provided id is empty or damaged");
            }
        }
        throw new Exception("Sector with id = " + id + " can't be read");
    }
}
