package com.raid.backend.raid.raid0;

import com.raid.backend.raid.Raid;
import com.raid.backend.dataLogic.ReadRequest;
import com.raid.backend.dataLogic.RegisterDiskRequest;
import com.raid.backend.dataLogic.WriteRequest;
import com.raid.backend.utility.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class Raid0 implements Raid {

    public Raid0() {
        this.client = new RestTemplateBuilder()
                .build();
    }

    private final Logger logger = LoggerFactory.getLogger(Raid0.class.getName());
    private final RestTemplate client;
    private final Map<Integer, FileDetails> files = new HashMap<>();
    private Integer fileId = 0;

    @Override
    public Set<Integer> getCurrentFilesIds() {
        return files.keySet();
    }

    @Override
    public void resetRaid() {
        files.clear();
        fileId = 0;
    }


    @Override
    public Integer writeData(String data) throws Exception {
        for (int i = 0; i < 2; i++) {
            int fileId = saveData(data);
            if (fileId != -1) {
                return fileId;
            }
        }
        throw new Exception("Couldn't save data");
    }

    private Integer saveData(String data) throws Exception {
        List<RegisterDiskRequest> disks = new ArrayList<>(registeredDisks);
        int partId = 0;
        var splitData = splitData(data);
        boolean isEnoughSpace = checkIsEnoughSpace(splitData);
        if (!isEnoughSpace) {
            throw new Exception("File is too large");
        }
        files.put(fileId, new FileDetails());
        var max = splitData.stream().mapToInt(List::size).max().getAsInt();
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < splitData.size(); j++) {
                if (splitData.get(j).size() > i) {
                    var dataToSave = splitData.get(j).get(i);
                    RegisterDiskRequest currentDisk = disks.get(j);
                    String diskIpAddress = currentDisk.getIpAddress() + ":" + currentDisk.getPort();
                    String url = "http://" + diskIpAddress + "/disk";
                    WriteRequest writeRequest = new WriteRequest(dataToSave);
                    var isSave = false;
                    for (int k = 0; k < NUMBER_OF_ATTEMPTS; k++) {
                        var response = client.postForEntity(url, writeRequest, ReadRequest.class);
                        if (response.getStatusCode() == HttpStatus.OK) {
                            var sectorId = Objects.requireNonNull(response.getBody()).getId();
                            files.get(fileId).getFileParts().put(partId, new FilePartDetails(sectorId, diskIpAddress));
                            isSave = true;
                            break;
                        } else {
                            logger.error("Write attempt error for part " + partId + " with message: " + Objects.requireNonNull(response.getBody()));
                        }
                    }
                    if (!isSave) {
                        removeFile(fileId);
                        return -1;
                    }
                    partId++;
                }
            }
        }
        return fileId++;
    }

    private boolean checkIsEnoughSpace(List<List<byte[]>> splitData) {
        boolean isEnoughSpace = true;
        var disks = new ArrayList<>(getRegisteredDisks());
        for (int i = 0; i < disks.size(); i++) {
            var disk = disks.get(i);
            String uri = "http://" + disk.getIpAddress() + ":" + disk.getPort() + "/disk/enough-space?fileSize=" + ByteUtils.countSize(splitData.get(i));
            try {
                var response = client.getForEntity(uri, boolean.class);
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    isEnoughSpace = isEnoughSpace && response.getBody();
                } else {
                    isEnoughSpace = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isEnoughSpace = false;
            }
        }
        return isEnoughSpace;
    }

    private List<List<byte[]>> splitData(String data) {
        List<RegisterDiskRequest> disks = new ArrayList<>(registeredDisks);
        List<List<byte[]>> splitedData = new ArrayList<>();
        for (int i = 0; i < disks.size(); i++) {
            splitedData.add(new ArrayList<>());
        }
        byte[] dataToSave = data.getBytes(StandardCharsets.UTF_8);
        int start = 0;
        int currentDiskIndex = 0;
        while (dataToSave.length > start) {
            RegisterDiskRequest currentDisk = disks.get(currentDiskIndex);
            byte[] dataPart = new byte[currentDisk.getSizeOfSector()];
            int end = Math.min(dataToSave.length, start + currentDisk.getSizeOfSector());
            System.arraycopy(dataToSave, start, dataPart, 0, end - start);
            splitedData.get(currentDiskIndex).add(dataPart);
            start = end;
            currentDiskIndex = (currentDiskIndex + 1) % registeredDisks.size();
        }
        return splitedData;
    }

    @Override
    public String readData(Integer id) throws Exception {
        if (!files.containsKey(id)) {
            throw new Exception("File not found");
        }
        Map<Integer, byte[]> content = new ConcurrentHashMap<>();
        var fileDetails = files.get(id);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (Integer partId : fileDetails.getFileParts().keySet()) {
            executor.execute(() -> {
                var part = fileDetails.getFileParts().get(partId);
                String url = "http://" + part.getIpAddress() + "/disk/" + part.getSectorId();
                var isRead = false;
                for (int i = 0; i < NUMBER_OF_ATTEMPTS; i++) {
                    byte[] result = readSinglePart(partId, url);
                    if (result != null) {
                        content.put(partId, result);
                        isRead = true;
                    }
                }
                if (!isRead) try {
                    throw new Exception("Cannot retrieve data");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            if (content.keySet().size() != files.get(id).getFileParts().keySet().size()) {
                throw new Exception("Can't retrieve all parts");
            }
            return mergeParts(content);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private byte[] readSinglePart(Integer partId, String url) {
        try {
            var response = client.getForEntity(url, WriteRequest.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getData();
            } else {
                logger.error("Read attempt error for part " + partId + " with message: " + Objects.requireNonNull(response.getBody()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeFile(Integer id) {
        if (files.containsKey(id)) {
            for (Integer partId : files.get(id).getFileParts().keySet()) {
                var partDetails = files.get(id).getFileParts().get(partId);
                String url = "http://" + partDetails.getIpAddress() + "/disk/" + partDetails.getSectorId();
                client.delete(url);
            }
        }
        files.remove(id);
    }
}
