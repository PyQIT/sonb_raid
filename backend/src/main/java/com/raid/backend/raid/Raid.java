package com.raid.backend.raid;

import com.raid.backend.disk.Disk;
import com.raid.backend.utility.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface Raid {
    Set<Disk> registeredDisks = new HashSet<>();
    int NUMBER_OF_ATTEMPTS = 2;

    default void setDisks(Set<Disk> backends) {
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

    void removeFile(Integer id) throws Exception;
    Integer writeData(String data) throws Exception;
    String readData(Integer id) throws Exception;
    default Set<Disk> getRegisteredDisks() {
        return registeredDisks;
    }
    Set<Integer> getCurrentFilesIds();
    void resetRaid();
}
