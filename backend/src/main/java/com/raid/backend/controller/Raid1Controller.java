package com.raid.backend.controller;

import com.raid.backend.raid.RAID1;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/raid1")
public class Raid1Controller {

    private final RAID1 raid1;

    public Raid1Controller(RAID1 raid1) {
        this.raid1 = raid1;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getData() {
        return raid1.getData();
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public void saveData(@RequestBody String data) {
        raid1.saveData(data);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteData(@RequestBody String data) {
        raid1.deleteData(data);
    }

    @GetMapping("/disks")
    @ResponseStatus(HttpStatus.OK)
    public int getDisksNumber() {
        return raid1.disksNumber();
    }

    @GetMapping("/type")
    @ResponseStatus(HttpStatus.OK)
    public String getRaidType() {
        return raid1.getRaidType();
    }

    @PostMapping("/sectormulfunction")
    @ResponseStatus(HttpStatus.OK)
    public void setSectorMulfunction(@RequestBody String data) {
        int sectorId = Integer.parseInt(data);
        raid1.setSectorMulfunction(sectorId);
    }

    @PostMapping("/vibrationdamage")
    @ResponseStatus(HttpStatus.OK)
    public void setVibrationDamage(@RequestBody String data) {
        int sectorId = Integer.parseInt(data);
        raid1.setVibrationDamage(sectorId);
    }

    @PostMapping("/voltagesurge")
    @ResponseStatus(HttpStatus.OK)
    public void setVoltageSurge(@RequestBody String data) {
        int sectorId = Integer.parseInt(data);
        raid1.setVoltageSurge(sectorId);
    }

    @GetMapping("/freesectors")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getFreeSectors() {
        return raid1.freeSectors();
    }

    @GetMapping("/occupiedsectors")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getOccupiedSectors() {
        return raid1.occupiedSectors();
    }

    @GetMapping("/damagedsectors")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectors() {
        return raid1.damagedSectors();
    }

    @GetMapping("/damagedsectorswibrations")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectorsWibrations() {
        return raid1.damagedSectorsWibrations();
    }

    @GetMapping("/damagedsectorsvoltagesurge")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectorsVoltageSurge() {
        return raid1.damagedSectorsVoltageSurge();
    }

    @GetMapping("/disksize")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskSize() {
        return raid1.diskSize();
    }

    @GetMapping("/disksizefree")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskSizeFree() {
        return raid1.diskSizeFree();
    }

    @GetMapping("/diskusage")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskUsage() {
        return raid1.diskUsage();
    }
}
