package com.raid.backend.dataLogic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnregisterDiskRequest {
    private String ipAddress;
    private String port;

}
