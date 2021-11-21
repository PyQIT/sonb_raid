package com.example.sonb_raid.RAID;

import java.util.Random;

public class RandomStorage {
    Storage storage;

    public RandomStorage(Storage storage)
    {
        if(storage.state) {
            System.out.println("No data,want to write!");
        }

        this.storage = storage;
    }

    public String randomread(long seed)
    {
        String ret = "";
        System.out.println("start random read!");
        int[] result = randomArray(0,storage.size-1,storage.size,seed);

        for(int i=0;i<storage.size;i++){
            if((String) storage.data.get(result[i])=="-1")
                break;
            ret += (String) storage.data.get(result[i]);
        }

        System.out.println("random read stop!");
        return ret;
    }

    public boolean randomwrite(String input,long seed)
    {
        System.out.println("start random wirte!");
        int len = input.length();//real write

        if(input.length()+storage.realsize>storage.size){
            System.out.println("disk not enough!");
            len = storage.size-storage.realsize;
            return false;
        }

        int[] result = randomArray(0,storage.size-1,storage.size,seed);

        for(int i=storage.realsize;i<storage.realsize+len;i++) {
            storage.data.set(result[i],(String)(input.charAt(i-storage.realsize)+""));
        }

        storage.realsize +=len;
        storage.state = false;
        System.out.println("random write stop!");
        return true;
    }

    public static int[] randomArray(int min,int max,int n,long seed){
        int len = max-min+1;

        if(max < min || n > len) {
            return null;
        }
        int[] source = new int[len];

        for (int i = min; i < min+len; i++){
            source[i-min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random(seed);
        int index = 0;

        for (int i = 0; i < result.length; i++) {
            index = Math.abs(rd.nextInt() % len--);
            result[i] = source[index];
            source[index] = source[len];
        }

        return result;
    }

    public String randomByteread(int point,long seed) {
        int[] result = randomArray(0,storage.size-1,storage.size,seed);
        return (String) storage.data.get(result[point]);
    }
}
