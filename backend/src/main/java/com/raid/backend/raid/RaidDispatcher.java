package com.raid.backend.raid;

import com.raid.backend.disk.Disk;
import com.raid.backend.raid.raid0.Raid0;
import com.raid.backend.raid.raid1.Raid1;
import com.raid.backend.raid.raid3.Raid3;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Data
@Component
public class RaidDispatcher {

    private final Raid0 raid0;
    private final Raid1 raid1;
    private final Raid3 raid3;
    private Raid currentRaid;
    private RaidTypes currentRaidType = RaidTypes.RAID0;
    private final Set<Disk> registeredDisks = new HashSet<>();

    public RaidDispatcher(Raid0 raid0, Raid1 raid1, Raid3 raid3) {
        currentRaid = raid0;
        this.raid0 = raid0;
        this.raid1 = raid1;
        this.raid3 = raid3;
    }

    public Raid get(RaidTypes raidType) {
        Raid raid;
        switch (raidType) {
            case RAID0:
                currentRaidType = RaidTypes.RAID0;
                raid = raid0;
                break;
            case RAID1:
                currentRaidType = RaidTypes.RAID1;
                raid = raid1;
                break;
            default:
                currentRaidType = RaidTypes.RAID3;
                raid = raid3;
                break;
        }
        currentRaid = raid;
        currentRaid.resetRaid();
        currentRaid.setDisks(registeredDisks);
        return currentRaid;
    }

    public void removeDisk(int diskId) {
        this.registeredDisks.stream()
                .filter(disk -> disk.getDiskId() == diskId)
                .findFirst()
                .ifPresent(registeredDisks::remove);
        updateDisks();
    }

    public boolean addDisk(int diskId) {
        Disk newDisk = new Disk(diskId);
        var result = this.registeredDisks.add(newDisk);
        updateDisks();
        return result;
    }

    private void updateDisks() {
        this.raid0.setDisks(registeredDisks);
        this.raid1.setDisks(registeredDisks);
        this.raid3.setDisks(registeredDisks);
    }
}
