package com.example.sonb_raid.RAID;

public class Raid5 extends Raid {
    public ArrayList <Integer> offset = new ArrayList();

    public Raid5(int disknum, int[] disksize) {
        super(disknum, disksize);
    }

    void RandomRaidwrite(String input) {
        input = Tools.StrToBinstr(input);
        int key = 0;
        int flog = 0;
        String result = "";
        int point = 0;

        while(new RandomStorage(disklist.get(0)).randomByteread(0,point)!="-1") {
            point++;
        }

        offset.add(point);

        while(true) {
            int xor = 1;
            int value = 1;

            for(int i=0;i<disknum-1;i++) {
                value = Integer.parseInt(input.charAt(key+i)+"",10);
                xor = xor^value;
            }

            flog = flog%disknum;
            int distance;

            if(key+disknum>=input.length()) {
                distance = input.length();
            }else {
                distance = key+disknum-1;
            }

            StringBuilder sbtemp=new StringBuilder(input.subSequence(key,distance));
            sbtemp.insert(flog,xor+"");
            result += sbtemp;
            key +=disknum-1;

            if(key>=input.length()-1)
                break;

            flog++;
        }

        int k = 0;

        for(int i=0;i<result.length();i++) {
            k = k%disknum;
            new RandomStorage(disklist.get(k)).randomwrite(result.charAt(i)+"");
            k++;
        }

        while(k<disknum) {
            disklist.get(k).realsize++;
            k++;
        }

    }

    void RandomRaidread(){
        for(int i=0;i<offset.size();i++)
        {
            RandomoffsetRaidread(offset.get(i));
        }
    }

    void RandomoffsetRaidread(int offsetpoint) {
        int k = 0;
        String result ="";
        for(int i=0;;)
        {
            if(k>=disknum)
                i++;
            k = k%disknum;
            String tempstring = new RandomStorage(disklist.get(k)).randomByteread(offsetpoint,i);
            if(tempstring=="-1")
                break;
            result += tempstring;
            k++;
        }

        int flog = 0;
        int key = 0;
        String output = "";
        while(true)
        {
            flog%=disknum;
            for(int i=0;i<disknum;i++)
            {
                if(key+i>=result.length())
                    break;
                if(flog !=i)
                {
                    output +=result.charAt(key+i);
                }
            }
            key+=disknum;
            flog++;
            if(key>=result.length())
                break;
        }
        output = Tools.BinstrToStr(output);
        System.out.println(output);
    }
}
