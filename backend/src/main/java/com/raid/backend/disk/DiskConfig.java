package com.raid.backend.disk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiskConfig {

    @Value(value = "128")
    private int numberOfSectors;

    @Value(value = "32")
    private int sizeOfSector;

    @Bean
    public int getSizeOfSector() {
        return sizeOfSector;
    }

    @Bean
    public int getNumberOfSectors() {
        return numberOfSectors;
    }

}
