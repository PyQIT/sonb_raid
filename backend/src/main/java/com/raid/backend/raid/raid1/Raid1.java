package com.raid.backend.raid.raid1;

import com.raid.backend.raid.Raid;
import com.raid.backend.dataLogic.ReadRequest;
import com.raid.backend.dataLogic.RegisterDiskRequest;
import com.raid.backend.dataLogic.WriteRequest;
import com.raid.backend.utility.ByteUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class Raid1 implements Raid {

    public Raid1() {
        this.client = new RestTemplate();
    }

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

    private int saveData(String data) throws Exception {
        List<RegisterDiskRequest> disks = new ArrayList<>(getRegisteredDisks());
        if (disks.size() % 2 != 0) {
            throw new Exception("Raid 1 require even number of disks");
        }
        int partId = 0;
        files.put(fileId, new FileDetails());
        List<byte[]> dataToSave = splitData(data.getBytes());
        if (!checkIsEnoughSpace(dataToSave)) {
            throw new Exception("File is too large");
        }
        int i = 0;
        for (byte[] dataPart : dataToSave) {
            WriteRequest writeRequest = new WriteRequest(dataPart);

            RegisterDiskRequest disk = disks.get(i);
            String url = "http://" + disk.getIpAddress() + ":" + disk.getPort();

            RegisterDiskRequest backupDisk = disks.get(i + 1);
            String backupUrl = "http://" + backupDisk.getIpAddress() + ":" + backupDisk.getPort();
            var isSaved = false;
            for (int j = 0; j < NUMBER_OF_ATTEMPTS; j++) {
                var response = client.postForEntity(url + "/disk", writeRequest, ReadRequest.class);
                var backupResponse = client.postForEntity(backupUrl + "/disk", writeRequest, ReadRequest.class);

                if (response.getStatusCode() == HttpStatus.OK && backupResponse.getStatusCode() == HttpStatus.OK) {
                    var sectorId = Objects.requireNonNull(response.getBody()).getId();
                    FilePartDetails partDetails = new FilePartDetails(sectorId, url);

                    var backupSectorId = Objects.requireNonNull(backupResponse.getBody()).getId();
                    FilePartDetails backupPartDetails = new FilePartDetails(backupSectorId, backupUrl);

                    var parts = files.get(fileId).getFileParts();
                    if (parts.containsKey(partId)) {
                        parts.get(partId).add(partDetails);
                        parts.get(partId).add(backupPartDetails);
                    } else {
                        parts.put(partId, Arrays.asList(partDetails, backupPartDetails));
                    }
                    isSaved = true;
                    break;
                }
            }
            if (!isSaved) {
                removeFile(fileId);
                return -1;
            }
            i = (i + 2) % disks.size();
            partId++;
        }
        return fileId++;
    }

    private List<byte[]> splitData(byte[] data) {
        int size = getRegisteredDisks().stream().findFirst().get().getSizeOfSector();
        int start = 0;
        List<byte[]> result = new LinkedList<>();
        while (data.length > start) {
            byte[] dataPart = new byte[size];
            int end = Math.min(data.length, start + size);

            System.arraycopy(data, start, dataPart, 0, end - start);
            result.add(dataPart);
            start = end;
        }
        return result;
    }

    @Override
    public String readData(Integer id) throws Exception {
        if (!files.containsKey(id)) {
            throw new Exception("File not found");
        }
        var content = new ConcurrentHashMap<Integer, byte[]>();
        var fileDetails = files.get(id);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (Integer partId : fileDetails.getFileParts().keySet()) {
            executor.execute(() -> {
                int currentDiskIndex = 0;
                var part = fileDetails.getFileParts().get(partId);
                do {
                    FilePartDetails disk = part.get(currentDiskIndex);
                    String url = disk.getIpAddress() + "/disk/" + disk.getSectorId();
                    try {
                        var response = client.getForEntity(url, WriteRequest.class);
                        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                            content.put(partId, Objects.requireNonNull(response.getBody()).getData());
                            break;
                        } else {
                            currentDiskIndex++;
                        }
                    } catch (Exception e) {
                        currentDiskIndex++;
                    }
                } while (currentDiskIndex < part.size());
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (files.get(id).getFileParts().keySet().size() != content.size())
            throw new Exception("Cannot retrieve data");
        try {
            return mergeParts(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    private boolean checkIsEnoughSpace(List<byte[]> splitData) {
        boolean isEnoughSpace = true;
        var disks = new ArrayList<>(getRegisteredDisks());
        for (RegisterDiskRequest disk : disks) {
            String uri = "http://" + disk.getIpAddress() + ":" + disk.getPort() + "/disk/enough-space?fileSize=" + ByteUtils.countSize(splitData);
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

    @Override
    public void removeFile(Integer id) throws Exception {
        if (files.containsKey(id)) {
            for (Integer partId : files.get(id).getFileParts().keySet()) {
                var partDetails = files.get(id).getFileParts().get(partId);
                for (FilePartDetails part : partDetails) {
                    var url = "http://" + part.getIpAddress() + "/disk/" + part.getSectorId();
                    try {
                        client.delete(url);
                    } catch (Exception e) {
                        throw new Exception("Cannot removed part: " + partId + " - Message " + e.getMessage());
                    }
                }
            }
        }
        files.remove(id);
    }
}
