package com.raid.backend.raid.raid1;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class FileDetails {

    private Map<Integer, List<FilePartDetails>> fileParts = new HashMap<>();

}
