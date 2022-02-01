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
        raid0.saveData(data);
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
    public void setSectorMulfunction(@RequestBody int sectorId, int diskId) {
        raid0.setSectorMulfunction(sectorId,diskId);
    }

    @PostMapping("/vibrationdamage")
    @ResponseStatus(HttpStatus.OK)
    public void setVibrationDamage(@RequestBody int sectorId, int diskId) {
        raid0.setVibrationDamage(sectorId,diskId);
    }

    @PostMapping("/voltagesurge")
    @ResponseStatus(HttpStatus.OK)
    public void setVoltageSurge(@RequestBody int sectorId, int diskId) {
        raid0.setVoltageSurge(sectorId,diskId);
    }

    @GetMapping("/freesectors/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getFreeSectors(@PathVariable int diskId) {
        return raid0.freeSectors(diskId);
    }

    @GetMapping("/occupiedsectors/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getOccupiedSectors(@PathVariable int diskId) {
        return raid0.occupiedSectors(diskId);
    }

    @GetMapping("/damagedsectors/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectors(@PathVariable int diskId) {
        return raid0.damagedSectors(diskId);
    }

    @GetMapping("/damagedsectorswibrations/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectorsWibrations(@PathVariable int diskId) {
        return raid0.damagedSectorsWibrations(diskId);
    }

    @GetMapping("/damagedsectorsvoltagesurge/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectorsVoltageSurge(@PathVariable int diskId) {
        return raid0.damagedSectorsVoltageSurge(diskId);
    }

    @GetMapping("/disksize/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskSize(@PathVariable int diskId) {
        return raid0.diskSize(diskId);
    }

    @GetMapping("/disksizefree/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskSizeFree(@PathVariable int diskId) {
        return raid0.diskSizeFree(diskId);
    }

    @GetMapping("/diskusage/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskUsage(@PathVariable int diskId) {
        return raid0.diskUsage(diskId);
    }

    @GetMapping("/diskusagepercent/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskUsagePercent(@PathVariable int diskId) {
        return raid0.diskUsagePercent(diskId);
    }

}