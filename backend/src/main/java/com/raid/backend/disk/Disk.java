package com.raid.backend.disk;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.raid.backend.disk.BinaryConverter.convertStringToBinary;
import static com.raid.backend.disk.DamageType.*;

@Data
@Component
public class Disk {
    private List<Sector> sectors = new ArrayList<>();
    private int diskId;
    private int numberOfSectors = 128;

    public void createSectors(){
        for (int i = 0; i < numberOfSectors; i++) {
            Sector sector = new Sector(i, 32, 0);
            sectors.add(sector);
        }
    }

    public boolean deleteText(String text){
        for(Sector sector: sectors){
            List<String> tmp = sector.getData();
            if(tmp.remove(text)){
                sector.setData(tmp);
                return true;
            }
        }
        return false;
    }

    public List<String> getTexts(){
        List<String> returnTexts = new ArrayList<>();
        for(Sector sector: sectors){
            //System.out.println(sector.getId() + " " + sector.getDamageType());
            if(sector.getDamageType().equals(WITHOUT_DAMAGE)) {
                for (String s : sector.getData()) {
                    returnTexts.add(s);
                }
            }
            if(sector.getDamageType().equals(VOLTAGE_SURGE)) {
                sector.setDamageType(WITHOUT_DAMAGE);
            }else if(sector.getDamageType().equals(VIBRATION_DAMAGE)) {
                sector.setDamageType(WITHOUT_DAMAGE);
            }
        }
        return returnTexts;
    }

    public int getTextsNumber(){
        int returnTextsNumber = 0;
        for(Sector sector: sectors){
            if(!sector.getData().isEmpty()) {
                for (String s : sector.getData()) {
                    returnTextsNumber += 1;
                }
            }
        }
        return returnTextsNumber;
    }

    public void saveText(String text){
        for(Sector sector: sectors){
            byte[] tmpInput = text.getBytes(StandardCharsets.UTF_8);
            System.out.println(sector.getSectorSize() + " " + sector.getSectorOccupiedSize() + " " + tmpInput.length);
            if(sector.getSectorSize() > (sector.getSectorOccupiedSize() + tmpInput.length)){
                if(!sector.getData().isEmpty()) {
                    List<String> tmp = sector.getData();
                    tmp.add(text);
                    sector.setData(tmp);
                    sector.setSectorOccupiedSize(sector.getSectorOccupiedSize() + tmpInput.length);
                    System.out.println("Sector: " + sector.getId() + " " + text + " = " + convertStringToBinary(text));
                    break;
                }else{
                    List<String> tmp = new ArrayList<>();
                    tmp.add(text);
                    sector.setData(tmp);
                    sector.setSectorOccupiedSize(sector.getSectorOccupiedSize() + tmpInput.length);
                    System.out.println("Sector: " + sector.getId() + " " + text + " = " + convertStringToBinary(text));
                    break;
                }
            }
        }
    }

    public void saveChecksum(List<List<byte[]>> checkSum){
        for(Sector sector: sectors){
            if(sector.getCheckSum().isEmpty())
                sector.setCheckSum(checkSum);
        }
    }

    public List<Integer> freeSectors(){
        List<Integer> returnSectorId = new ArrayList<Integer>();
        for(Sector sector: sectors){
            if(sector.getData().isEmpty()){
               returnSectorId.add(sector.getId());
            }
        }

        return returnSectorId;
    }

    public List<Integer> occupiedSectors(){
        List<Integer> returnSectorId = new ArrayList<Integer>();
        for(Sector sector: sectors){
            if(!sector.getData().isEmpty()){
                returnSectorId.add(sector.getId());
            }
        }
        return returnSectorId;
    }

    public List<Integer> damagedSectors(){
        List<Integer> returnSectorId = new ArrayList<Integer>();
        for(Sector sector: sectors){
            if(sector.getDamageType().equals(SECTOR_MULFUNCTION)){
                returnSectorId.add(sector.getId());
            }
        }
        return returnSectorId;
    }

    public List<Integer> damagedSectorsWibrations(){
        List<Integer> returnSectorId = new ArrayList<Integer>();
        for(Sector sector: sectors){
            if(sector.getDamageType().equals(VIBRATION_DAMAGE)){
                returnSectorId.add(sector.getId());
            }
        }
        return returnSectorId;
    }

    public List<Integer> damagedSectorsVoltageSurge(){
        List<Integer> returnSectorId = new ArrayList<Integer>();
        for(Sector sector: sectors){
            if(sector.getDamageType().equals(VOLTAGE_SURGE)){
                returnSectorId.add(sector.getId());
            }
        }
        return returnSectorId;
    }

    public int diskSize(){
        int diskSizeTotal = 0;
        for(Sector sector: sectors){
            diskSizeTotal = diskSizeTotal + sector.getSectorSize();
        }
        return diskSizeTotal;
    }

    public int diskSizeFree(){
        int diskSizeFree = 0;
        for(Sector sector: sectors){
            int tmpSectorSize = 0;
            if(sector.getData().isEmpty()){
                diskSizeFree = diskSizeFree + sector.getSectorSize();
            }else{
                for(String s: sector.getData()){
                    byte[] tmp = s.getBytes(StandardCharsets.UTF_8);
                    tmpSectorSize = tmpSectorSize + tmp.length;
                }
                diskSizeFree = diskSizeFree + (sector.getSectorSize() - tmpSectorSize);
            }
        }
        return diskSizeFree;
    }

    public int diskUsage(){
        int diskUsageSize = 0;
        for(Sector sector: sectors){
            if(!sector.getData().isEmpty()){
                for(String s: sector.getData()){
                    byte[] tmp = s.getBytes(StandardCharsets.UTF_8);
                    diskUsageSize = diskUsageSize + tmp.length;
                }
            }
        }
        return diskUsageSize;
    }

    public double diskUsagePercent(){
        return (diskUsage()/diskSize())/100;
    }

    public void setSectorMulfunction(int id){
        for(Sector sector: sectors){
            if(sector.getId() == id)
                sector.setDamageType(SECTOR_MULFUNCTION);
        }
    }

    public void setVibrationDamage(int id){
        for(Sector sector: sectors){
            if(sector.getId() == id) {
                sector.setDamageType(VIBRATION_DAMAGE);
                List<String> tmp = sector.getData();
                tmp.clear();
                sector.setData(tmp);
            }
        }
    }

    public void setVoltageSurge(int id){
        for(Sector sector: sectors){
            if(sector.getId() == id)
                sector.setDamageType(VOLTAGE_SURGE);
        }
    }
}
