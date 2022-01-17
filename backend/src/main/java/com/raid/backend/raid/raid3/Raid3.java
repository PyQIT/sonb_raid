package com.raid.backend.raid.raid3;

import com.raid.backend.raid.Raid;
import com.raid.backend.dataLogic.ReadRequest;
import com.raid.backend.dataLogic.RegisterDiskRequest;
import com.raid.backend.utility.ByteUtils;
import com.raid.backend.dataLogic.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class Raid3 implements Raid {


    public Raid3() {
        this.client = new RestTemplate();
    }

    private final RestTemplate client;
    private final Map<Integer, FileDetails> files = new HashMap<>();
    private final Map<Integer, List<CheckSumDetails>> fileCheckSumDetailsList = new HashMap<>();
    private Integer fileId = 0;
    private final Logger logger = LoggerFactory.getLogger(Raid3.class.getName());

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
        validDisks();
        for (int i = 0; i < 2; i++) {
            int fileId = savaData(data);
            if (fileId != -1) {
                return fileId;
            }
        }
        throw new Exception("Couldn't save data");
    }

    private int savaData(String data) throws Exception {
        List<List<byte[]>> splitData = splitData(data);
        List<List<byte[]>> splitDataWithCrc = countSumControl(splitData);

        List<RegisterDiskRequest> disks = getDisksWithDiskForCheckSum();


        if (!checkIsEnoughSpace(splitData)) {
            throw new Exception("File is too large");
        }

        int partId = 0;
        files.put(fileId, new FileDetails());
        int max = splitDataWithCrc.stream().mapToInt(List::size).max().getAsInt();
        fileCheckSumDetailsList.put(fileId, new ArrayList<>());
        for (int i = 0; i < max; i++) {

            var checkSumDetails = new CheckSumDetails();

            for (int j = 0; j < splitDataWithCrc.size() - 1; j++) {
                if (splitDataWithCrc.get(j).size() > i) {
                    var dataToSave = splitData.get(j).get(i);
                    RegisterDiskRequest currentDisk = disks.get(j);

                    String diskIpAddress = getIpAddressFromDisk(currentDisk);
                    String url = "http://" + diskIpAddress + "/disk";

                    WriteRequest writeRequest = new WriteRequest(dataToSave);
                    var isSaved = false;
                    for (int k = 0; k < NUMBER_OF_ATTEMPTS; k++) {
                        var response = client.postForEntity(url, writeRequest, ReadRequest.class);
                        if (response.getStatusCode() == HttpStatus.OK) {
                            var sectorId = Objects.requireNonNull(response.getBody()).getId();
                            FilePartDetails partDetails = new FilePartDetails(partId, sectorId, diskIpAddress);
                            checkSumDetails.addPartDetails(partDetails);
                            files.get(fileId).getFileParts().put(partId, partDetails);
                            isSaved = true;
                            break;
                        } else {
                            logger.error("Write attempt error for part " + partId + " with message: " + Objects.requireNonNull(response.getBody()));
                        }
                    }
                    if (!isSaved) {
                        removeFile(fileId);
                        return -1;
                    }
                    partId++;
                }
            }

            var isSaved = false;
            for (int k = 0; k < NUMBER_OF_ATTEMPTS; k++) {
                var sumControl = splitDataWithCrc.get(splitDataWithCrc.size() - 1).get(i);

                RegisterDiskRequest currentDisk = disks.get(disks.size() - 1);
                String diskIpAddress = getIpAddressFromDisk(currentDisk);
                String url = "http://" + diskIpAddress + "/disk";
                WriteRequest writeRequest = new WriteRequest(sumControl);
                var response = client.postForEntity(url, writeRequest, ReadRequest.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    var sectorId = Objects.requireNonNull(response.getBody()).getId();
                    FilePartDetails partDetails = new FilePartDetails(-1, sectorId, diskIpAddress);
                    checkSumDetails.setCheckSum(partDetails);
                    isSaved = true;
                } else {
                    System.out.println(response.getBody());
                }
                if (!isSaved) {
                    removeFile(fileId);
                    return -1;
                }
                fileCheckSumDetailsList.get(fileId).add(checkSumDetails);
            }
        }
        return fileId++;
    }

    private void validDisks() throws Exception {
        if (registeredDisks.size() <= 1) throw new Exception("Raid 3 required 2 or more disks");
    }

    private List<RegisterDiskRequest> getDisksWithDiskForCheckSum() {
        var checkSumDisk = registeredDisks.stream().filter(RegisterDiskRequest::isCheckSumDisk).findFirst();
        if (checkSumDisk.isPresent()) {
            var disk = checkSumDisk.get();
            registeredDisks.remove(disk);
            var disks = new ArrayList<>(registeredDisks);
            disks.add(disk);
            logger.info("Sum check disk:" + getIpAddressFromDisk(disks.get(disks.size() - 1)));
            return disks;
        }
        var disks = new ArrayList<>(registeredDisks);
        disks.get(disks.size() - 1).setCheckSumDisk(true);
        logger.info("Sum check disk:" + getIpAddressFromDisk(disks.get(disks.size() - 1)));
        return disks;
    }

    private String getIpAddressFromDisk(RegisterDiskRequest currentDisk) {
        return currentDisk.getIpAddress() + ":" + currentDisk.getPort();
    }

    private List<List<byte[]>> countSumControl(List<List<byte[]>> splitData) {
        int maxSize = splitData.stream().mapToInt(List::size).max().getAsInt();
        for (int i = 0; i < maxSize; i++) {
            var sumCheck = splitData.get(0).get(i);
            for (int j = 1; j < (splitData.size() - 1); j++) {
                if (splitData.get(j).size() > i) {
                    var data = splitData.get(j).get(i);
                    sumCheck = xor(sumCheck, data);
                } else {
                    sumCheck = xor(sumCheck, new byte[maxSize]);
                }
            }
            splitData.get(splitData.size() - 1).add(i, sumCheck);
        }
        return splitData;
    }

    public byte[] xor(byte[] data1, byte[] data2) {
        if (data1.length > data2.length) {
            byte[] tmp = data2;
            data2 = data1;
            data1 = tmp;
        }
        var result = new byte[data1.length];
        for (int i = 0; i < data1.length; i++) {
            result[i] = (byte) ((int) data2[i] ^ (int) data1[i]);
        }
        return result;
    }

    @Override
    public String readData(Integer id) throws Exception {
        if (!files.containsKey(id)) {
            throw new Exception("File not found");
        }
        var fileDetails = files.get(id);
        Map<Integer, byte[]> content = new ConcurrentHashMap<>();
        for (Integer partId : fileDetails.getFileParts().keySet()) {
            var part = fileDetails.getFileParts().get(partId);
            String url = "http://" + part.getIpAddress() + "/disk/" + part.getSectorId();
            try {

                var response = client.getForEntity(url, WriteRequest.class);
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    content.put(partId, response.getBody().getData());
                } else {

                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        for (Integer partId : files.get(id).getFileParts().keySet().stream().sorted().collect(Collectors.toList())) {
            if (!content.containsKey(partId)) {
                byte[] retrieveDataParts = retrieveDataPart(id, partId, content);
                content.put(partId, ByteUtils.trimEnd(retrieveDataParts));
            }
        }
        try {
            return mergeParts(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private byte[] retrieveDataPart(Integer fileId, Integer partId, Map<Integer, byte[]> content) throws Exception {
        var file = fileCheckSumDetailsList.get(fileId);

        var checkSumDetails = file.stream().filter(sumDetails ->
                        sumDetails.getPartDetailsList().stream()
                                .anyMatch(partDetails -> partDetails.getPartId().equals(partId)))
                .findFirst();
        if (checkSumDetails.isPresent()) {
            var needPartsIds = checkSumDetails.get().partDetailsList.stream().map(FilePartDetails::getPartId).collect(Collectors.toList());
            needPartsIds.removeAll(List.of(partId));
            for (Integer id : needPartsIds) {
                if (!content.containsKey(id)) throw new Exception("Cannot find part with id = " + id);
            }
            var needCheckSum = checkSumDetails.get().getCheckSum();
            String url = "http://" + needCheckSum.getIpAddress() + "/disk/" + needCheckSum.getSectorId();
            try {
                var response = client.getForEntity(url, WriteRequest.class);
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    byte[] checkSum = response.getBody().getData();
                    var needParts = needPartsIds.stream().map(content::get).collect(Collectors.toList());
                    return recreateData(needParts, checkSum);
                } else {
                    throw new Exception("Cannot retrieve data");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    public byte[] recreateData(List<byte[]> currentData, byte[] checksum) {
        byte[] result = checksum;
        for (byte[] currentDatum : currentData) {
            result = xor(result, currentDatum);
        }
        return result;
    }

    private List<List<byte[]>> splitData(String data) {
        List<RegisterDiskRequest> disks = new ArrayList<>(registeredDisks);
        List<List<byte[]>> splitData = new ArrayList<>();
        for (int i = 0; i < disks.size(); i++) {
            splitData.add(new ArrayList<>());
        }
        byte[] dataToSave = data.getBytes(StandardCharsets.UTF_8);
        int start = 0;
        int currentDiskIndex = 0;
        while (dataToSave.length > start) {
            RegisterDiskRequest currentDisk = disks.get(currentDiskIndex);
            byte[] dataPart = new byte[currentDisk.getSizeOfSector()];
            int end = Math.min(dataToSave.length, start + currentDisk.getSizeOfSector());
            System.arraycopy(dataToSave, start, dataPart, 0, end - start);
            splitData.get(currentDiskIndex).add(dataPart);
            start = end;
            currentDiskIndex = (currentDiskIndex + 1) % (registeredDisks.size() - 1);
        }
        return splitData;
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

    @Override
    public void removeFile(Integer id) {
        if (fileCheckSumDetailsList.containsKey(id)) {
            for (CheckSumDetails checkSumDetails : fileCheckSumDetailsList.get(id)) {
                for (FilePartDetails filePartDetails : checkSumDetails.getPartDetailsList()) {
                    var url = "http://" + filePartDetails.getIpAddress() + "/disk/" + filePartDetails.getSectorId();
                    try {
                        client.delete(url);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
                var url = "http://" + checkSumDetails.getCheckSum().getIpAddress() + "/disk/" + checkSumDetails.getCheckSum().getSectorId();
                try {
                    client.delete(url);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
        files.remove(id);
        fileCheckSumDetailsList.remove(id);
    }
}
