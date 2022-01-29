package com.raid.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.raid.backend.disk.Disk;
import com.raid.backend.raid.RaidTypes;
import com.raid.backend.raid.Raid;
import com.raid.backend.raid.RaidManager;
import com.raid.backend.dataLogic.*;
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
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @GetMapping()
    public String get(Model model) throws JsonProcessingException {
        model.addAttribute("raid", currentRaid);
        model.addAttribute("currentFiles", currentRaid.getCurrentFilesIds());
        model.addAttribute("backends", raidManager.getRegisteredDisks());
        model.addAttribute("content", new WriteDataRequest());
        model.addAttribute("currentRaid", raidManager.getCurrentRaidType());
        model.addAttribute("currentFile", currentContent);
        String json = ow.writeValueAsString(model);
        return json;
    }

    @GetMapping("/disk")
    public String getDisk(Model model) throws JsonProcessingException {
        model.addAttribute("disk", currentDisk);
        String json = ow.writeValueAsString(model);
        return json;
    }

    @PostMapping("/text/writing")
    public void saveText(@RequestParam String content) throws Exception {
        System.out.println(content);
        currentRaid.setDisks(raidManager.getRegisteredDisks());
        currentRaid.writeData(content);
        currentContent = "";
    }

    @GetMapping("/text/reading")
    public String readText(@RequestParam int id) throws Exception {
        currentRaid.setDisks(raidManager.getRegisteredDisks());
        currentContent = currentRaid.readData(id);
        String json = ow.writeValueAsString(currentContent);
        return json;
    }

    @PostMapping("/sector/damage")
    public void damageSector(@RequestParam int sectorId, @RequestParam Integer damageType) throws Exception {
        currentDisk.damageSector(sectorId, damageType);
    }

    @PostMapping("/disk/register")
    public void registerDisk(@RequestBody RegisterDiskRequest request) {
        if (raidManager.addDisk(request))
            currentRaid.setDisks(raidManager.getRegisteredDisks());
    }

    @PostMapping("/disk/unregister")
    public void unregisterDisk(@RequestBody UnregisterDiskRequest request) {
        raidManager.removeDisk(request);
        currentRaid.setDisks(raidManager.getRegisteredDisks());
    }

    @PostMapping(value = "/raid")
    public void changeRaidType(@RequestParam(name = "type") String type) {
        System.out.println(type);
        if(!type.equals(""))
            currentRaid = raidManager.get(RaidTypes.values()[Integer.parseInt(type)]);
    }

    @DeleteMapping("/file/{fileId}")
    public void removeFile(@PathVariable(name = "fileId") int fileId) throws Exception {
        currentRaid = raidManager.getCurrentRaid();
        currentRaid.setDisks(raidManager.getRegisteredDisks());
        currentRaid.removeFile(fileId);
    }

    @PostMapping(value = "/disk/write")
    public void writeOnDisk(@RequestBody WriteRequest writeRequest) throws Exception {
        ReadRequest readRequest = currentDisk.writeData(writeRequest);
    }

    @GetMapping(value = "/disk/read/{id}")
    public String readFromDisk(@PathVariable int id) throws Exception {
        WriteRequest writeRequest = currentDisk.readData(id);
        String json = ow.writeValueAsString(writeRequest);
        return json;
    }

    @GetMapping(value = "/disk/space")
    public String isDiskHaveEnoughSpace() throws JsonProcessingException {
        String json = ow.writeValueAsString(currentDisk.getFreeSpaceSize());
        return json;
    }

    @DeleteMapping(value = "/sector")
    public void clearDisk() {
        currentDisk.clearSectors();
    }

    @DeleteMapping(value = "/sector/{sectorId}")
    public void clearSector(@PathVariable int sectorId) {
        currentDisk.clearSector(sectorId);
    }
}