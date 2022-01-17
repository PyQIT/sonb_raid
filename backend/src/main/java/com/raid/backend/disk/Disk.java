package com.raid.backend.disk;

import com.raid.backend.dataLogic.ReadRequest;
import com.raid.backend.dataLogic.WriteRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Component
public class Disk {

    private final Logger logger;

    private final List<Sector> sectors = new ArrayList<>();

    private final int sectorSize;

    private final int numberOfSectors;



    public Disk(DiskConfig config) {
        logger = LoggerFactory.getLogger(Disk.class);
        this.sectorSize = config.getSizeOfSector();
        this.numberOfSectors = config.getNumberOfSectors();
        initSectors();
    }

    private void initSectors() {
        for (int i = 0; i < numberOfSectors; i++) {
            Sector sector = new Sector(i, sectorSize);
            sectors.add(sector);
        }
    }

    public void clearSectors() {
        for (Sector sector : sectors) {
            sector.setData(null);
        }
        logger.info("Sectors cleared");
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
            if (sector.getData() == null && sector.getDamageType().equals(DamageType.NO_DAMAGE)) {
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
            if (sector.getData() != null && sector.getDamageType().equals(DamageType.NO_DAMAGE)) {
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
        if (damageType == DamageType.PERMANENT.ordinal()) {
            sectorToDamage.setData(null);
            sectorToDamage.setDamageType(DamageType.PERMANENT);
        }
    }

    public Sector findSector(int sectorId) throws Exception {
        for (Sector sector : sectors) {
            if (sector.getId() == sectorId && sector.getDamageType().equals(DamageType.NO_DAMAGE)) {
                return sector;
            }
        }
        throw new Exception("Sector with id = " + sectorId + " not exist");
    }

    public List<Integer> printPermanentlyDamagedSectors() {
        List<Integer> sectorsPermamentlyDamaged = new ArrayList<>();
        for (Sector sector : sectors) {
            if (sector.getDamageType().equals(DamageType.PERMANENT)) {
                sectorsPermamentlyDamaged.add(sector.getId());
            }
        }
        return sectorsPermamentlyDamaged;
    }


    public ReadRequest writeData(WriteRequest request) throws Exception {
        for (Sector sector : sectors) {
            if ((sector.getData() == null || sector.getData().length == 0)) {
                if (sector.getDamageType().equals(DamageType.PERMANENT)) {
                    logger.info("sector permamently damaged");
                    break;
                }
                byte[] data = request.getData();
                int end = Math.min(data.length, sectorSize);
                byte[] toSave = new byte[sector.getSectorSize()];
                System.arraycopy(data, 0, toSave, 0, end);
                sector.setData(toSave);
                logger.info("Size of data = " + sector.getData().length + " data = " + Arrays.toString(sector.getData()) + " has been saved");
                return new ReadRequest(sector.getId());
            }
        }
        throw new Exception("backend is full or some sectors are damaged. Cannot save more data");
    }

    public WriteRequest readData(int id) throws Exception {
        for (Sector sector : sectors) {
            if (sector.getId() == id) {
                if (sector.getDamageType().equals(DamageType.PERMANENT)) {
                    break;
                }
                if (sector.getData() != null && sector.getData().length > 0) {
                    logger.info(Arrays.toString(sector.getData()) + " has been read");
                    return new WriteRequest(sector.getData());
                }
                throw new Exception("Sector with provided id is empty or damaged");
            }
        }
        throw new Exception("Sector with id = " + id + " can't be read");
    }
}
