package com.example.sonb_raid.RAID;

public class Raid0 extends Raid{
    public Raid0(int disknum,int[] disksize) {
        super(disknum, disksize);
        System.out.println("this is raid0 create!");
    }

    void RandomRaidread(long seed) {
        int k = 0;
        String result = "";

        for(int i=0;;) {
            if(k>=disknum)
                i++;
            k = k%disknum;
            String tempstring = new RandomStorage(disklist.get(k)).randomByteread(i,seed);
            if(tempstring=="-1")
                break;
            result+=tempstring;
            k++;
        }

        System.out.println(Tools.BinstrToStr(result));
    }

    void RandomRaidwrite(String input, long seed) {
        input = Tools.StrToBinstr(input);
        String[] inputs;
        inputs = new String[disknum];
        for(int i=0;i<disknum;i++) {
            inputs[i] ="";
        }
        int k = lastdisk;
        for(int i=0;i<input.length();i++) {
            k = i%disknum;
            inputs[k]=inputs[k]+input.charAt(i)+"";
            k++;
        }
        for(int i=0;i<disknum;i++) {
            new RandomStorage(disklist.get(i)).randomwrite(inputs[i],seed);
            lastdisk = k+1;
        }
    }
}
