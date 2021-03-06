package com.raid.backend.controller;

import com.raid.backend.raid.RAID0;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/raid0")
public class Raid0Controller {

    private final RAID0 raid0;

    public Raid0Controller(RAID0 raid0) {
        this.raid0 = raid0;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getData() {
        return raid0.getData();
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public void saveData(@RequestBody String data) {
        raid0.saveData(data.substring(0,data.length() - 1));
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteData(@RequestBody String data) {
        raid0.deleteData(data);
    }

    @GetMapping("/disks")
    @ResponseStatus(HttpStatus.OK)
    public int getDisksNumber() {
        return raid0.disksNumber();
    }

    @GetMapping("/type")
    @ResponseStatus(HttpStatus.OK)
    public String getRaidType() {
        return raid0.getRaidType();
    }

    @PostMapping("/sectormulfunction")
    @ResponseStatus(HttpStatus.OK)
    public void setSectorMulfunction(@RequestBody String data) {
        String[] result = data.split(",");
        int sectorId = Integer.parseInt(result[0]);
        int diskId = Integer.parseInt(result[1].substring(0,data.length() - 1));
        raid0.setSectorMulfunction(sectorId,diskId);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public void setVibrationDamage(@RequestBody String data) {
        String[] result = data.split(",");
        int sectorId = Integer.parseInt(result[0]);
        int diskId = Integer.parseInt(result[1].substring(0,data.length() - 1));
        raid0.setVibrationDamage(sectorId,diskId);
    }

    @PostMapping("/voltagesurge")
    @ResponseStatus(HttpStatus.OK)
    public void setVoltageSurge(@RequestBody String data) {
        String[] result = data.split(",");
        int sectorId = Integer.parseInt(result[0]);
        int diskId = Integer.parseInt(result[1].substring(0,data.length() - 1));
        raid0.setVoltageSurge(sectorId,diskId);
    }

    @GetMapping("/freesectors")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getFreeSectors(@RequestBody String data) {
        int diskId = Integer.parseInt(data.substring(0,data.length() - 1));
        return raid0.freeSectors(diskId);
    }

    @GetMapping("/occupiedsectors")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getOccupiedSectors(@RequestBody String data) {
        int diskId = Integer.parseInt(data.substring(0,data.length() - 1));
        return raid0.occupiedSectors(diskId);
    }

    @GetMapping("/damagedsectors")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectors(@RequestBody String data) {
        int diskId = Integer.parseInt(data.substring(0,data.length() - 1));
        return raid0.damagedSectors(diskId);
    }

    @GetMapping("/damagedsectorswibrations")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectorsWibrations(@RequestBody String data) {
        int diskId = Integer.parseInt(data.substring(0,data.length() - 1));
        return raid0.damagedSectorsWibrations(diskId);
    }

    @GetMapping("/damagedsectorsvoltagesurge")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectorsVoltageSurge(@RequestBody String data) {
        int diskId = Integer.parseInt(data.substring(0,data.length() - 1));
        return raid0.damagedSectorsVoltageSurge(diskId);
    }

    @GetMapping("/disksize")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskSize() {
        return raid0.diskSize();
    }

    @GetMapping("/disksizefree")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskSizeFree() {
        return raid0.diskSizeFree();
    }

    @GetMapping("/diskusage")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskUsage() {
        return raid0.diskUsage();
    }

}
