package com.example.sonb_raid.RAID;

import java.util.ArrayList;

public  class Storage {
    public int size;
    public int realsize;
    public ArrayList data = new ArrayList();
    public boolean state;

    private void init() {
        for(int i = 0;i<size;i++) {
            data.add("-1");
        }
        realsize = 0;
        state = true;
    }

    public boolean clear() {
        for(int i = 0;i<size;i++) {
            data.add("-1");
        }
        state = true;
        realsize = 0;
        return true;
    }

    public Storage(int size) {
        this.size = size;
        init();
    }
}
