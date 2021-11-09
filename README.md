# sonb_raid

Projekt 3 - RAID

Grupa projektowa:
Przemysław Pyk
Krzysztof Siczek

RAID to technologia wirtualizacji przechowywania danych, która łączy wiele komponentów dysków fizycznych w jedną lub więcej jednostek logicznych w celu redundancji danych,poprawy wydajności lub obu. Było to sprzeczne z poprzednią koncepcją wysoce niezawodnych dysków mainframe określanych jako "pojedynczy duży drogi dysk" (SLED).

Dane są dystrybuowane na dyskach na jeden z kilku sposobów, określanych jako poziomy RAID, w zależności od wymaganego poziomu nadmiarowości i wydajności. Różne schematy lub układy dystrybucji danych są nazywane słowem "RAID", po którym następuje liczba, na przykład RAID 0 lub RAID 1. Każdy schemat lub poziom RAID zapewnia inną równowagę między kluczowymi celami: niezawodnością, dostępnością, wydajnościąi pojemnością. Poziomy RAID większe niż RAID 0 zapewniają ochronę przed nieodwracalnymi błędami odczytu sektora, a także przed awariami całych dysków fizycznych.

RAID-1 is shorthand for RAID Level 1: Mirroring and Duplexing.

RAID Level 1 is a type of RAID configuration which uses 2 disks to provide 100% redundancy in case of a single drive failure.

Features:
- Availability: Can tolerate the loss of 1 drive
- Capacity: 50% (2 drives = 1 drive available space)
- Degradation: Slight degradation of read performance during rebuild, write speed actually increases. Rebuilding is fast.
- Performance: Reads are up to twice as fast as a single disk, writes are the same as the speed of a write to the slowest of the 2 disks.
- Summary: Highest fault tolerance / availability, lowest storage capacity
- Typical Uses: Accounting, payroll, financial services, databases servers, highly available services
- 
How it works
- READ: read data from both disks at the same time
- WRITE: write data to both disks, write is complete when both disk writes complete (when writing in synchronous mode)
- 
Comparison with other RAID types:
- RAID-0 has faster writes and equal reads (for a 2-drive RAID-0 set)
- RAID-5 has slower writes and faster reads (as it requires minimum 3 disks, but can read from all 3)

  
![image](https://user-images.githubusercontent.com/13750868/140877402-8ca2294a-b49b-4459-9e80-d6f160245bb8.png)



![image](https://user-images.githubusercontent.com/13750868/140877347-974d4f6c-a8de-426e-b888-2d5a10ef659d.png)
