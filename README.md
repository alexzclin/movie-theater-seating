# Movie Theater Seating Challenge

## Description
An algorithm written in Java for assigning seats within a movie theater to fulfill reservation requests.

The algorithm is executable from the command line by compiling the java files and running:
```
java Solver <input path> <output path>
```

## Assumptions
Inputs will be given in the format of:
```
R001 2
R002 4
R003 4
R004 3
```
where the first item represents the ticket number and the second item represents the number of tickets bought.

The outputs will be given in the format of:
```
R001 F1,F2
R002 F6,F7,F8,F9
R003 F13,F14,F15,F16
R004 D1,D2,D3
```
Where the first item represents the ticket number and the second item represents the seat numbers of the tickets (with A being the first row and 1 being the first column).

## Variations of the Problem that the Algorithm can Adapt to
- Supporting theaters of different sizes and shapes (each row can have a different number of seats).

## Improvements to be Implemented
- Considering the increase of satisfaction level of seats towards the middle columns as well.