package com.raid.backend.raid.raid3;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class FileDetails {

    private Map<Integer, FilePartDetails> fileParts = new HashMap<>();

}
