package com.raid.backend.raid;

import com.raid.backend.disk.Disk;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    public void deleteData(String data){
        if(!diskOne.deleteText(data)){
            if(!diskSecond.deleteText(data))
                diskThird.deleteText(data);
        }
    }

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

    public int disksNumber(){
        return 4;
    }

    public String getRaidType(){
        return "RAID3";
    }
}
