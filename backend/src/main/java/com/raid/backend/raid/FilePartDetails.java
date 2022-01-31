package com.raid.backend.raid;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilePartDetails {
    private Integer partId;
    private Integer sectorId;
    private Integer diskId;
}
