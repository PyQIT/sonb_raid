package com.raid.backend.raid.raid3;

import com.raid.backend.raid.FilePartDetails;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class CheckSumDetails {
    List<FilePartDetails> partDetailsList = new ArrayList<>();
    FilePartDetails checkSum;

    public CheckSumDetails() {
    }

    public void addPartDetails(FilePartDetails filePartDetails) {
        partDetailsList.add(filePartDetails);
    }
}
