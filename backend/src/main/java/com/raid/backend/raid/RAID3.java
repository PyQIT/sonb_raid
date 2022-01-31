package com.raid.backend.raid;

import com.raid.backend.disk.Disk;

import java.util.ArrayList;
import java.util.List;

public class RAID3 {
    Disk diskOne;
    Disk diskSecond;
    Disk diskThird;
    Disk diskFourth;

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
}
