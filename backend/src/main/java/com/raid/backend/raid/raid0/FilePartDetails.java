package com.raid.backend.raid.raid0;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilePartDetails {
    private Integer sectorId;
    private String ipAddress;
}
