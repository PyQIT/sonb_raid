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
    public void setSectorMulfunction(@RequestBody int sectorId, int diskId) {
        raid1.setSectorMulfunction(sectorId,diskId);
    }

    @PostMapping("/vibrationdamage")
    @ResponseStatus(HttpStatus.OK)
    public void setVibrationDamage(@RequestBody int sectorId, int diskId) {
        raid1.setVibrationDamage(sectorId,diskId);
    }

    @PostMapping("/voltagesurge")
    @ResponseStatus(HttpStatus.OK)
    public void setVoltageSurge(@RequestBody int sectorId, int diskId) {
        raid1.setVoltageSurge(sectorId,diskId);
    }

    @GetMapping("/freesectors/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getFreeSectors(@PathVariable int diskId) {
        return raid1.freeSectors(diskId);
    }

    @GetMapping("/occupiedsectors/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getOccupiedSectors(@PathVariable int diskId) {
        return raid1.occupiedSectors(diskId);
    }

    @GetMapping("/damagedsectors/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectors(@PathVariable int diskId) {
        return raid1.damagedSectors(diskId);
    }

    @GetMapping("/damagedsectorswibrations/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectorsWibrations(@PathVariable int diskId) {
        return raid1.damagedSectorsWibrations(diskId);
    }

    @GetMapping("/damagedsectorsvoltagesurge/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectorsVoltageSurge(@PathVariable int diskId) {
        return raid1.damagedSectorsVoltageSurge(diskId);
    }

    @GetMapping("/disksize/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskSize(@PathVariable int diskId) {
        return raid1.diskSize(diskId);
    }

    @GetMapping("/disksizefree/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskSizeFree(@PathVariable int diskId) {
        return raid1.diskSizeFree(diskId);
    }

    @GetMapping("/diskusage/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskUsage(@PathVariable int diskId) {
        return raid1.diskUsage(diskId);
    }

    @GetMapping("/diskusagepercent/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskUsagePercent(@PathVariable int diskId) {
        return raid1.diskUsagePercent(diskId);
    }

}
