package com.raid.backend.controller;

import com.raid.backend.disk.Disk;
import com.raid.backend.raid.RaidTypes;
import com.raid.backend.raid.Raid;
import com.raid.backend.raid.RaidManager;
import com.raid.backend.dataLogic.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class RaidController {

    public RaidController(Disk disk, RaidManager raidManager) {
        this.currentDisk = disk;
        this.raidManager = raidManager;
        this.currentRaid = raidManager.getCurrentRaid();
    }

    private final Disk currentDisk;
    private Raid currentRaid;
    private final RaidManager raidManager;
    private String currentContent = "";

    @GetMapping()
    public String get(Model model) {
        model.addAttribute("raid", currentRaid);
        model.addAttribute("currentFiles", currentRaid.getCurrentFilesIds());
        model.addAttribute("backends", raidManager.getRegisteredDisks());
        model.addAttribute("content", new WriteDataRequest());
        model.addAttribute("currentRaid", getRaidTypeLabel(raidManager.getCurrentRaidType()));
        model.addAttribute("currentFile", currentContent);
        return "index";
    }

    private String getRaidTypeLabel(RaidTypes currentRaidType) {
        switch (currentRaidType) {
            case RAID0:
                return "RAID 0";
            case RAID1:
                return "RAID 1";
            default:
                return "RAID 3";
        }
    }

    @GetMapping("/index")
    public String get1(Model model) {
        model.addAttribute("disk", currentDisk);
        return "index";
    }

    @PostMapping("/text/writing")
    public String saveText(@ModelAttribute WriteDataRequest content, Model model) throws Exception {
        currentRaid.setDisks(raidManager.getRegisteredDisks());
        currentRaid.writeData(content.getData());
        currentContent = "";
        return "redirect:/index";
    }

    @GetMapping("/text/reading")
    public String readText(@RequestParam int id, Model model) throws Exception {
        currentRaid.setDisks(raidManager.getRegisteredDisks());
        currentContent = currentRaid.readData(id);
        return "redirect:/index";
    }

    @PostMapping("/raid/type")
    public String changeRaidType(@RequestParam int type, Model model) {
        currentContent = "";
        currentRaid = raidManager.get(RaidTypes.values()[type]);
        for (RegisterDiskRequest registeredbackend : raidManager.getRegisteredDisks()) {
            String backendIpAddress = "http://" + registeredbackend.getIpAddress() + ":" + registeredbackend.getPort();
            String url = backendIpAddress + "/backend";
        }
        return "redirect:/index";
    }

    @PostMapping("/sector/damage")
    public String damageSector(@RequestParam int sectorId, @RequestParam Integer damageType, Model model) throws Exception {
        currentDisk.damageSector(sectorId, damageType);
        return "redirect:/index";
    }

    @PostMapping("/disk/register")
    public ResponseEntity<Void> registerDisk(@RequestBody RegisterDiskRequest request) {
        if (raidManager.addDisk(request)) {
            currentRaid.setDisks(raidManager.getRegisteredDisks());
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/disk/unregister")
    public ResponseEntity<Void> unregisterDisk(@RequestBody UnregisterDiskRequest request) {
        raidManager.removeDisk(request);
        currentRaid.setDisks(raidManager.getRegisteredDisks());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/data/writing")
    public ResponseEntity<Integer> writeData(@RequestBody WriteDataRequest request) throws Exception {
        currentRaid.setDisks(raidManager.getRegisteredDisks());
        var fileId = currentRaid.writeData(request.getData());
        return ResponseEntity.ok(fileId);
    }

    @GetMapping("/data/reading/{id}")
    public ResponseEntity<String> writeData(@PathVariable Integer id) throws Exception {
        currentRaid.setDisks(raidManager.getRegisteredDisks());
        var content = currentRaid.readData(id);
        return ResponseEntity.ok(content);
    }

    @PatchMapping(value = "/raid")
    public ResponseEntity<String> changeRaidType(@RequestParam(name = "type") int type) {
        currentRaid = raidManager.get(RaidTypes.values()[type]);
        return ResponseEntity.ok("Change to raid" + type);
    }

    @DeleteMapping("/file/{fileId}")
    public ResponseEntity<Void> removeFile(@PathVariable(name = "fileId") int fileId) {
        currentRaid = raidManager.getCurrentRaid();
        currentRaid.setDisks(raidManager.getRegisteredDisks());
        currentRaid.removeFile(fileId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/disk/write")
    public ResponseEntity<ReadRequest> writeOnDisk(@RequestBody WriteRequest writeRequest) throws Exception {
        ReadRequest readRequest = currentDisk.writeData(writeRequest);
        return ResponseEntity.ok(readRequest);
    }

    @GetMapping(value = "/disk/read/{id}")
    public ResponseEntity<WriteRequest> readFromDisk(@PathVariable int id) throws Exception {
        WriteRequest writeRequest = currentDisk.readData(id);
        return ResponseEntity.ok(writeRequest);
    }

    @GetMapping(value = "/disk/space")
    public ResponseEntity<Boolean> isDiskHaveEnoughSpace(@RequestParam int fileSize) {
        return ResponseEntity.ok(fileSize <= currentDisk.getFreeSpaceSize());
    }

    @DeleteMapping(value = "/sector")
    public ResponseEntity<Void> clearbackend() {
        currentDisk.clearSectors();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/sector/{sectorId}")
    public ResponseEntity<Void> clearSector(@PathVariable int sectorId) {
        currentDisk.clearSector(sectorId);
        return ResponseEntity.noContent().build();
    }
}
