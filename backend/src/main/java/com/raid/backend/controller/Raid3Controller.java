package com.raid.backend.controller;

import com.raid.backend.raid.RAID3;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/raid3")
public class Raid3Controller {

    private final RAID3 raid3;

    public Raid3Controller(RAID3 raid3) {
        this.raid3 = raid3;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getData() {
        return raid3.getData();
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public void saveData(@RequestBody String data) {
        raid3.saveData(data);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteData(@RequestBody String data) {
        raid3.deleteData(data);
    }

    @GetMapping("/disks")
    @ResponseStatus(HttpStatus.OK)
    public int getDisksNumber() {
        return raid3.disksNumber();
    }

    @GetMapping("/type")
    @ResponseStatus(HttpStatus.OK)
    public String getRaidType() {
        return raid3.getRaidType();
    }

    @PostMapping("/sectormulfunction")
    @ResponseStatus(HttpStatus.OK)
    public void setSectorMulfunction(@RequestBody int sectorId, int diskId) {
        raid3.setSectorMulfunction(sectorId,diskId);
    }

    @PostMapping("/vibrationdamage")
    @ResponseStatus(HttpStatus.OK)
    public void setVibrationDamage(@RequestBody int sectorId, int diskId) {
        raid3.setVibrationDamage(sectorId,diskId);
    }

    @PostMapping("/voltagesurge")
    @ResponseStatus(HttpStatus.OK)
    public void setVoltageSurge(@RequestBody int sectorId, int diskId) {
        raid3.setVoltageSurge(sectorId,diskId);
    }

    @GetMapping("/freesectors/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getFreeSectors(@PathVariable int diskId) {
        return raid3.freeSectors(diskId);
    }

    @GetMapping("/occupiedsectors/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getOccupiedSectors(@PathVariable int diskId) {
        return raid3.occupiedSectors(diskId);
    }

    @GetMapping("/damagedsectors/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectors(@PathVariable int diskId) {
        return raid3.damagedSectors(diskId);
    }

    @GetMapping("/damagedsectorswibrations/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectorsWibrations(@PathVariable int diskId) {
        return raid3.damagedSectorsWibrations(diskId);
    }

    @GetMapping("/damagedsectorsvoltagesurge/{diskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> getDamagedSectorsVoltageSurge(@PathVariable int diskId) {
        return raid3.damagedSectorsVoltageSurge(diskId);
    }

    @GetMapping("/disksize")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskSize() {
        return raid3.diskSize();
    }

    @GetMapping("/disksizefree")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskSizeFree() {
        return raid3.diskSizeFree();
    }

    @GetMapping("/diskusage")
    @ResponseStatus(HttpStatus.OK)
    public int getDiskUsage() {
        return raid3.diskUsage();
    }

}
