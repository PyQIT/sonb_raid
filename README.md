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

RAID = Redundant Array of Independent Disks To tablice dyskowe wykorzystujące redundancję do zwiększenia szybkości lub niezawodności działania dysków.
Istnieje kilka sposobów na połączenie ze sobą grupy dysków, oznaczanych jako RAIDx.

RAID0:

Model RAID0 zbudowany jest z N dysków bez redundancji – nie zwiększa niezawodności działania dysków!

![raid0](https://user-images.githubusercontent.com/75424132/140889399-7444cb6c-d287-4bec-bfb5-508b832ed215.JPG)

Struktura taka ukierunkowana jest na zwiększenie szybkości operacji dyskowych.
Kolejne jednostki danych (bajty lub sektory) zapisywane są sekwencyjnie na kolejnych
dyskach (modulo N).
Przy odpowiednich buforach pamięciowych można wykonywać operację jednocześnie
na N dyskach i przyspieszyć operację N-razy.

RAID1:

W modelu RAID1 każdy dysk posiada swoją kompletną kopię (mirror). Wszystkie
zapisy realizowane są jednocześnie na dysku podstawowym i zapasowym. Odczyt
realizowany jest z jednego dysku.
W przypadku wykrycia błędu (CRC) informacja odczytywana jest z dysku
zapasowego.

Do N dysków pierwotnych dodaje się k dysków, które przechowują dane korekcyjne
(zazwyczaj kod Hamminga) wyznaczane np. dla sektorów o tych samych adresach
z dysków podstawowych.

![raid2](https://user-images.githubusercontent.com/75424132/140889494-b1f9313b-0016-4653-88a3-184b12b72927.JPG)

W przypadku uszkodzenia jednego z dysków jego dane można odtworzyć. Operacja
zapisu wymaga uaktualnienia danych korekcyjnych!

WAŻNE:
Model RAID2 w praktyce nie jest stosowany.

RAID 3:

RAID3 to pewne uproszczenie struktury RAID2. W tym modelu jest tylko jeden dysk
redundancyjny (Dp) zawierający parzystość poprzeczną (bity parzystości)
odpowiednich danych z dysków podstawowych.

Operacje odczytu realizowane są z pełną szybkością. W przypadku błędu należy
wykonać odczyt ze wszystkich dysków (poza uszkodzonym) i odtworzyć dane.
Model RAID3 pozwala na wymianę uszkodzonego dysku na nowy w trakcie pracy
systemu (tzw. hot swap)!

![raid3](https://user-images.githubusercontent.com/75424132/140889799-3cb4007c-76af-430c-9676-e2a13d4d9ad7.JPG)

RAID 4:

Organizacja dysków w modelu RAID4 jest taka sama jak dla RAID3.
- RAID3 ma strukturę drobnoziarnistą, jest zorientowany
na dużą szybkość transmisji danych.
- RAID4 posiada strukturę gruboziarnistą, która pozwala
na równoczesny dostęp do różnych zbiorów (plików).

RAID 5:

RAID5 to udoskonalenie struktury RAID3 – sektory redundancyjne z bitami
parzystości są rozłożone równomiernie na N+1 dyskach.

![raid5](https://user-images.githubusercontent.com/75424132/140890074-45b00bbc-287c-4dd0-8c6d-b954c0642d78.JPG)

Organizacja taka pozwala uniknąć „wąskiego gardła”, jakim jest dysk parzystości
w strukturze RAID3 podczas operacji zapisu.

RAID 6:

RAID6 (nazywana też RAID5+1) to macierz RAID5 uzupełniona o dodatkowy dysk
i dodatkowy rekord parzystości dla każdego bloku danych (Q). Blok Q kodowany jest
inaczej niż blok P (np. z pomocą kodu Reeda-Solomona).

![raid6](https://user-images.githubusercontent.com/75424132/140890159-b24bcf1c-0222-40f5-9034-6513d33f8529.JPG)

Struktura pozwala na tolerowanie uszkodzeń dwóch dysków jednocześnie. Pozwala
uniknąć sytuacji, gdy w trakcie wymiany uszkodzonego dysku awarii ulegnie kolejny.

Podsumowanie:
![podsumowanie](https://user-images.githubusercontent.com/75424132/140890367-a1616436-c246-4549-a5bb-17d5578b258d.JPG)

RAID0+1:

RAID0+1 to schemat RAID1 którego elementami są macierze RAID0. Do jej
budowy potrzebne są co najmniej 4 dyski.

![raid0+1](https://user-images.githubusercontent.com/75424132/140890560-634b6a4d-fefc-4be1-ad67-8fa2b3abf651.JPG)


Macierz tego typu posiada zarówno zalety RAID0 (szybkość) jak i RAID1
(bezpieczeństwo), jest też łatwiejsza w implementacji niż RAID3, 5 i 6.

RAID1+0:

W przypadku RAID1+0 (inna nazwa to RAID10) elementami macierzy RAID0
są macierze RAID1.

![raid1+0](https://user-images.githubusercontent.com/75424132/140890745-49442487-d576-45a4-8f8a-50e85d62c87b.JPG)


Model posiada wady i zalety analogicznie jak dla RAID0+1.
