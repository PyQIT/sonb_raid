package com.raid.backend.raid;

import com.raid.backend.disk.Disk;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RAID1 {
    Disk diskOne;
    Disk diskSecond;

    RAID1(){
        diskOne = new Disk();
        diskSecond = new Disk();

        diskOne.setDiskId(1);
        diskOne.createSectors();

        diskSecond.setDiskId(2);
        diskSecond.createSectors();
    }

    @Async
    public void saveData(String data){
        diskSecond.saveText(data);
        diskOne.saveText(data);
    }

    @Async
    public void deleteData(String data){
        diskOne.deleteText(data);
        diskSecond.deleteText(data);
    }

    @Async
    public List<String> getData(){
        List<String> returnTexts = new ArrayList<>();
        returnTexts.addAll(diskOne.getTexts());

        return returnTexts;
    }

    @Async
    public int disksNumber(){
        return 2;
    }

    @Async
    public String getRaidType(){
        return "RAID1";
    }

    @Async
    public void setSectorMulfunction(int sectorId) {
        diskOne.setSectorMulfunction(sectorId);
        diskSecond.setSectorMulfunction(sectorId);
    }

    @Async
    public void setVibrationDamage(int sectorId){
        diskOne.setVibrationDamage(sectorId);
        diskSecond.setVibrationDamage(sectorId);
    }

    @Async
    public void setVoltageSurge(int sectorId){
        diskOne.setVoltageSurge(sectorId);
        diskSecond.setVoltageSurge(sectorId);
    }

    public List<Integer> freeSectors(){
        if(!diskOne.freeSectors().isEmpty())
            return diskOne.freeSectors();
        else
            return diskSecond.freeSectors();
    }

    public List<Integer> occupiedSectors(){
        if(!diskOne.occupiedSectors().isEmpty())
            return diskOne.occupiedSectors();
        else
            return diskSecond.occupiedSectors();
    }

    public List<Integer> damagedSectors(){
        if(!diskOne.damagedSectors().isEmpty())
            return diskOne.damagedSectors();
        else
            return diskSecond.damagedSectors();
    }

    public List<Integer> damagedSectorsWibrations(){
        if(!diskOne.damagedSectorsWibrations().isEmpty())
            return diskOne.damagedSectorsWibrations();
        else
            return diskSecond.damagedSectorsWibrations();
    }

    public List<Integer> damagedSectorsVoltageSurge(){
        if(!diskOne.damagedSectorsVoltageSurge().isEmpty())
            return diskOne.damagedSectorsVoltageSurge();
        else
            return diskSecond.damagedSectorsVoltageSurge();
    }

    public int diskSize(){
        return diskOne.diskSize();
    }

    public int diskSizeFree(){
        return diskOne.diskSizeFree();
    }

    public int diskUsage(){
        return diskOne.diskUsage();
    }

}
