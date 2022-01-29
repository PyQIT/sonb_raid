package com.raid.backend.raid.raid3;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilePartDetails {
    private Integer partId;
    private Integer sectorId;
    private String ipAddress;
}
