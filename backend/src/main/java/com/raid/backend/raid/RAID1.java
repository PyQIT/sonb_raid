package com.raid.backend.raid;

import com.raid.backend.disk.Disk;

import java.util.ArrayList;
import java.util.List;

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

    public void saveData(String data){
        diskSecond.saveText(data);
        diskOne.saveText(data);
    }

    public void deleteData(String data){
        diskOne.deleteText(data);
        diskSecond.deleteText(data);
    }

    public List<String> getData(){
        List<String> returnTexts = new ArrayList<>();
        returnTexts.addAll(diskOne.getTexts());

        return returnTexts;
    }
}
