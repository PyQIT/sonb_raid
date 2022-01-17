package com.raid.backend.raid.raid3;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CheckSumDetails {

    List<FilePartDetails> partDetailsList = new ArrayList<>();
    FilePartDetails checkSum;

    public CheckSumDetails() {
    }

    public void addPartDetails(FilePartDetails filePartDetails) {
        partDetailsList.add(filePartDetails);
    }
}
