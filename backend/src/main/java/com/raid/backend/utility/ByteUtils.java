package com.raid.backend.utility;

import java.util.Arrays;
import java.util.List;

public class ByteUtils {
    public static byte[] trimEnd(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }
        return Arrays.copyOf(bytes, i + 1);
    }

    public static int countSize(List<byte[]> bytes) {
        int size = 0;
        for (byte[] aByte : bytes) {
            size += aByte.length;
        }
        return size;
    }
}
