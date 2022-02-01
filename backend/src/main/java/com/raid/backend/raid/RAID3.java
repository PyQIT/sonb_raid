package com.raid.backend.raid;

import com.raid.backend.disk.Disk;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class RAID3 {
    Disk diskOne;
    Disk diskSecond;
    Disk diskThird;
    Disk diskFourth;

    List<List<byte[]>> splitDataTmp = new ArrayList<>();

    RAID3(){
        diskOne = new Disk();
        diskSecond = new Disk();
        diskThird = new Disk();
        diskFourth = new Disk();

        diskOne.setDiskId(1);
        diskOne.createSectors();

        diskSecond.setDiskId(2);
        diskSecond.createSectors();

        diskThird.setDiskId(3);
        diskThird.createSectors();

        diskFourth.setDiskId(4);
        diskFourth.createSectors();
    }

    @Async
    public void saveData(String data) {
        if(diskThird.getTextsNumber() == diskOne.getTextsNumber() && diskThird.getTextsNumber() == diskSecond.getTextsNumber()) {
            diskOne.saveText(data);
            List<byte[]> tmp = new ArrayList<>();
            tmp.add(data.getBytes(StandardCharsets.UTF_8));
            splitDataTmp.add(tmp);

        }else if(diskOne.getTextsNumber() > diskSecond.getTextsNumber() && diskOne.getTextsNumber() > diskThird.getTextsNumber()) {
            if(diskSecond.getTextsNumber() > diskThird.getTextsNumber()) {
                diskThird.saveText(data);
            }else{
                diskSecond.getTextsNumber();
                List<byte[]> tmp = new ArrayList<>();
                tmp.add(data.getBytes(StandardCharsets.UTF_8));
                splitDataTmp.add(tmp);
            }
        }else if(diskSecond.getTextsNumber() == diskOne.getTextsNumber() && diskSecond.getTextsNumber() > diskThird.getTextsNumber()) {
            if (diskOne.getTextsNumber() > diskThird.getTextsNumber()) {
                diskThird.saveText(data);
                List<byte[]> tmp = new ArrayList<>();
                tmp.add(data.getBytes(StandardCharsets.UTF_8));
                splitDataTmp.add(tmp);
                diskFourth.saveChecksum(countSumControl(splitDataTmp));
            } else {
                diskOne.saveText(data);
            }
        }
    }

    @Async
    public void deleteData(String data){
        if(!diskOne.deleteText(data)){
            if(!diskSecond.deleteText(data))
                diskThird.deleteText(data);
        }
    }

    @Async
    public List<String> getData(){
        List<String> returnTexts = new ArrayList<>();
        returnTexts.addAll(diskOne.getTexts());
        returnTexts.addAll(diskSecond.getTexts());
        returnTexts.addAll(diskThird.getTexts());

        return returnTexts;
    }

    private List<List<byte[]>> countSumControl(List<List<byte[]>> splitData) {
        int maxSize = splitData.stream().mapToInt(List::size).max().getAsInt();
        for (int i = 0; i < maxSize; i++) {
            var sumCheck = splitData.get(0).get(i);
            for (int j = 1; j < (splitData.size() - 1); j++) {
                if (splitData.get(j).size() > i) {
                    var data = splitData.get(j).get(i);
                    sumCheck = xor(sumCheck, data);
                } else {
                    sumCheck = xor(sumCheck, new byte[maxSize]);
                }
            }
            splitData.get(splitData.size() - 1).add(i, sumCheck);
        }
        return splitData;
    }

    public byte[] xor(byte[] data1, byte[] data2) {
        if (data1.length > data2.length) {
            byte[] tmp = data2;
            data2 = data1;
            data1 = tmp;
        }
        var result = new byte[data1.length];
        for (int i = 0; i < data1.length; i++) {
            result[i] = (byte) ((int) data2[i] ^ (int) data1[i]);
        }
        return result;
    }

    @Async
    public int disksNumber(){
        return 4;
    }

    @Async
    public String getRaidType(){
        return "RAID3";
    }

    @Async
    public void setSectorMulfunction(int sectorId, int diskId) {
        if(diskId == 1){
            diskOne.setSectorMulfunction(sectorId);
        }else if(diskId == 2){
            diskSecond.setSectorMulfunction(sectorId);
        }else if(diskId == 3){
            diskThird.setSectorMulfunction(sectorId);
        }
    }

    @Async
    public void setVibrationDamage(int sectorId, int diskId){
        if(diskId == 1){
            diskOne.setVibrationDamage(sectorId);
        }else if(diskId == 2){
            diskSecond.setVibrationDamage(sectorId);
        }else if(diskId == 3){
            diskThird.setVibrationDamage(sectorId);
        }
    }

    @Async
    public void setVoltageSurge(int sectorId, int diskId){
        if(diskId == 1){
            diskOne.setVoltageSurge(sectorId);
        }else if(diskId == 2){
            diskSecond.setVoltageSurge(sectorId);
        }else if(diskId == 3){
            diskThird.setVoltageSurge(sectorId);
        }
    }

    @Async
    public List<Integer> freeSectors(int diskId){
        if(diskId == 1){
            return diskOne.freeSectors();
        }else if(diskId == 2){
            return diskSecond.freeSectors();
        }else if(diskId == 3){
            return diskThird.freeSectors();
        }
        return null;
    }

    @Async
    public List<Integer> occupiedSectors(int diskId){
        if(diskId == 1){
            return diskOne.occupiedSectors();
        }else if(diskId == 2){
            return diskSecond.occupiedSectors();
        }else if(diskId == 3){
            return diskThird.occupiedSectors();
        }
        return null;
    }

    @Async
    public List<Integer> damagedSectors(int diskId){
        if(diskId == 1){
            return diskOne.damagedSectors();
        }else if(diskId == 2){
            return diskSecond.damagedSectors();
        }else if(diskId == 3){
            return diskThird.damagedSectors();
        }
        return null;
    }

    @Async
    public List<Integer> damagedSectorsWibrations(int diskId){
        if(diskId == 1){
            return diskOne.damagedSectorsWibrations();
        }else if(diskId == 2){
            return diskSecond.damagedSectorsWibrations();
        }else if(diskId == 3){
            return diskThird.damagedSectorsWibrations();
        }
        return null;
    }

    @Async
    public List<Integer> damagedSectorsVoltageSurge(int diskId){
        if(diskId == 1){
            return diskOne.damagedSectorsVoltageSurge();
        }else if(diskId == 2){
            return diskSecond.damagedSectorsVoltageSurge();
        }else if(diskId == 3){
            return diskThird.damagedSectorsVoltageSurge();
        }
        return null;
    }

    @Async
    public int diskSize(int diskId){
        if(diskId == 1){
            return diskOne.diskSize();
        }else if(diskId == 2){
            return diskSecond.diskSize();
        }else if(diskId == 3){
            return diskThird.diskSize();
        }
        return 0;
    }

    @Async
    public int diskSizeFree(int diskId){
        if(diskId == 1){
            return diskOne.diskSizeFree();
        }else if(diskId == 2){
            return diskSecond.diskSizeFree();
        }else if(diskId == 3){
            return diskThird.diskSizeFree();
        }
        return 0;
    }

    @Async
    public int diskUsage(int diskId){
        if(diskId == 1){
            return diskOne.diskUsage();
        }else if(diskId == 2){
            return diskSecond.diskUsage();
        }else if(diskId == 3){
            return diskThird.diskUsage();
        }
        return 0;
    }

    @Async
    public int diskUsagePercent(int diskId){
        if(diskId == 1){
            return diskOne.diskUsagePercent();
        }else if(diskId == 2){
            return diskSecond.diskUsagePercent();
        }else if(diskId == 3){
            return diskThird.diskUsagePercent();
        }
        return 0;
    }
}
