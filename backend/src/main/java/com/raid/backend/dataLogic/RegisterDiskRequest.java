package com.raid.backend.dataLogic;

import lombok.*;
import java.util.Objects;

@Data
@ToString
@AllArgsConstructor
public class RegisterDiskRequest {
    private String ipAddress;
    private String port;
    private int numberOfSectors;
    private int sizeOfSector;
    private boolean isCheckSumDisk;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterDiskRequest that = (RegisterDiskRequest) o;
        return Objects.equals(ipAddress, that.ipAddress) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, port);
    }
}


