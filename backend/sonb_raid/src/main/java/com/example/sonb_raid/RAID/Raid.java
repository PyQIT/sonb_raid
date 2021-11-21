package com.example.sonb_raid.RAID;

import java.util.ArrayList;

public abstract class Raid {
    protected ArrayList <Disk> disklist = new ArrayList<Disk>();
    private String description;
    protected int disknum=0;
    protected int lastdisk = 0;

    public boolean addDisk(Disk disk) {
        disklist.add(disk);
        disknum++;
        return true;
    }

    public boolean init(int disknum,int[] disksize) {
        int i;

        for(i =0;i<disknum;i++) {
            Disk temp = new Disk(disksize[i]);
            temp.setDescription("I am NO "+i+" disk.");
            if(!addDisk(temp))
                return false;
        }

        for(i=0;i<disklist.size();i++) {
            if(!disklist.get(i).clear())
                return false;
        }

        return true;
    }

    public Raid(int disknum,int[] disksize) {
        if(disknum != disksize.length) {
            System.out.println("raid disk num is error");
        }
        init(disknum,disksize);
    }

    public void removeDisk(int targetdisknum) {
        if(targetdisknum>disknum) {
            System.out.println("remove disk does not exist");
            return ;
        }

        disklist.remove(targetdisknum);
        System.out.println("you remove 1 disk!");
        disknum--;
    }

    public void listDisk() {
        System.out.println("We have "+disknum+" disks");
        for(int i=0;i<disknum;i++) {
            System.out.println(disklist.get(i).getDescription());
        }
    }

    public void readAll() {
        for(int i=0;i<disknum;i++) {
            String result ="";
            System.out.println("NO "+i);

            for(int j=0;j<disklist.get(i).size;j++) {
                result +=disklist.get(i).data.get(j)+"";
            }

            System.out.println(result);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    abstract void RandomRaidread(long seed);
    abstract void RandomRaidwrite(String input,long seed);
}
