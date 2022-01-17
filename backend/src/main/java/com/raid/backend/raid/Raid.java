package com.raid.backend.raid;

import com.raid.backend.dataLogic.RegisterDiskRequest;
import com.raid.backend.utility.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface Raid {

    Set<RegisterDiskRequest> registeredDisks = new HashSet<>();

    int NUMBER_OF_ATTEMPTS = 2;

    default void setDisks(Set<RegisterDiskRequest> backends) {
        if (!registeredDisks.isEmpty()) {
            registeredDisks.clear();
        }
        registeredDisks.addAll(backends);
    }

    default String mergeParts(Map<Integer, byte[]> content) throws IOException {
        var output = new ByteArrayOutputStream();
        for (Integer integer : content.keySet().stream().sorted().collect(Collectors.toList())) {
            output.write(ByteUtils.trimEnd(content.get(integer)));
        }
        return output.toString();
    }

    void removeFile(Integer id);

    Integer writeData(String data) throws Exception;

    String readData(Integer id) throws Exception;

    default Set<RegisterDiskRequest> getRegisteredDisks() {
        return registeredDisks;
    }

    Set<Integer> getCurrentFilesIds();

    void resetRaid();
}
