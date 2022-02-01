package com.raid.backend.raid;

import com.raid.backend.disk.Disk;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RAID0 {
    Disk diskOne;
    Disk diskSecond;

    public RAID0(){
        diskOne = new Disk();
        diskSecond = new Disk();

        diskOne.setDiskId(1);
        diskOne.createSectors();

        diskSecond.setDiskId(2);
        diskSecond.createSectors();
    }

    public void saveData(String data){
        if(diskOne.getTextsNumber() > diskSecond.getTextsNumber()) {
            diskSecond.saveText(data);
        }else{
            diskOne.saveText(data);
        }
    }

    public void deleteData(String data){
        if(!diskOne.deleteText(data)){
            diskSecond.deleteText(data);
        }
    }

    public List<String> getData(){
        List<String> returnTexts = new ArrayList<>();
        returnTexts.addAll(diskOne.getTexts());
        returnTexts.addAll(diskSecond.getTexts());

        return returnTexts;
    }

    public int disksNumber(){
        return 2;
    }

    public String getRaidType(){
        return "RAID0";
    }

    public void setSectorMulfunction(int sectorId, int diskId) {
        if(diskId == 1){
            diskOne.setSectorMulfunction(sectorId);
        }else if(diskId == 2){
            diskSecond.setSectorMulfunction(sectorId);
        }
    }

    public void setVibrationDamage(int sectorId, int diskId){
        if(diskId == 1){
            diskOne.setVibrationDamage(sectorId);
        }else if(diskId == 2){
            diskSecond.setVibrationDamage(sectorId);
        }
    }

    public void setVoltageSurge(int sectorId, int diskId){
        if(diskId == 1){
            diskOne.setVoltageSurge(sectorId);
        }else if(diskId == 2){
            diskSecond.setVoltageSurge(sectorId);
        }
    }

    public List<Integer> freeSectors(int diskId){
        if(diskId == 1){
            return diskOne.freeSectors();
        }else if(diskId == 2){
            return diskSecond.freeSectors();
        }
        return null;
    }

    public List<Integer> occupiedSectors(int diskId){
        if(diskId == 1){
           return diskOne.occupiedSectors();
        }else if(diskId == 2){
           return diskSecond.occupiedSectors();
        }
        return null;
    }

    public List<Integer> damagedSectors(int diskId){
        if(diskId == 1){
           return diskOne.damagedSectors();
        }else if(diskId == 2){
           return diskSecond.damagedSectors();
        }
        return null;
    }

    public List<Integer> damagedSectorsWibrations(int diskId){
        if(diskId == 1){
           return diskOne.damagedSectorsWibrations();
        }else if(diskId == 2){
           return diskSecond.damagedSectorsWibrations();
        }
        return null;
    }

    public List<Integer> damagedSectorsVoltageSurge(int diskId){
        if(diskId == 1){
            return diskOne.damagedSectorsVoltageSurge();
        }else if(diskId == 2){
            return diskSecond.damagedSectorsVoltageSurge();
        }
        return null;
    }

    public int diskSize(int diskId){
        if(diskId == 1){
            return diskOne.diskSize();
        }else if(diskId == 2){
            return diskSecond.diskSize();
        }
        return 0;
    }

    public int diskSizeFree(int diskId){
        if(diskId == 1){
            return diskOne.diskSizeFree();
        }else if(diskId == 2){
            return diskSecond.diskSizeFree();
        }
        return 0;
    }

    public int diskUsage(int diskId){
        if(diskId == 1){
           return diskOne.diskUsage();
        }else if(diskId == 2){
           return diskSecond.diskUsage();
        }
        return 0;
    }

    public int diskUsagePercent(int diskId){
        if(diskId == 1){
            return diskOne.diskUsagePercent();
        }else if(diskId == 2){
            return diskSecond.diskUsagePercent();
        }
        return 0;
    }
}
