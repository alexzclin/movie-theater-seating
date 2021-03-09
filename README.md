# Movie Theater Seating Challenge

## Description
An algorithm written in Java for assigning seats within a movie theater to fulfill reservation requests. To maximize customer satisfaction and customer safety, we will follow these rules.

**Rules**
- Customer satisfaction is defined by the row of the seats and if the group is seated together. Middle seats are given higher priority while the front and back are given lower priority.
- Public safety protocol are in place and a bubble of 3 seats and 1 row will be created for every group.
- The default theater size is 10x20, where rows are labeled starting from 'A' and columns are numbered starting from 1.

The algorithm is executable from the command line by compiling the java files and running:
```
java Solver <input path> <output path>
```
We will assign seats to customers based on the order in which tickets are purchased.
**Priorities:**
1. Seats are grouped together in a single row.
2. Seats are in a row that is towards the middle.
3. If the theater is unable to provide seats that fit the group in one row, the group will be split apart. We will then start filling the rows with most capacity first, which will reduce the size of the group. Once the group can be fit into a single row, we will choose the row that is closest to the middle.



## Assumptions
Inputs will be given in the format of:
```
R001 2
R002 4
R003 4
R004 3
```
where the first item represents the reservation identifier and the second item represents the number of tickets bought. The reservation requests are given in the order they were received.

The outputs will be given in the format of:
```
R001 F1,F2
R002 F6,F7,F8,F9
R003 F13,F14,F15,F16
R004 D1,D2,D3
```
Where the first item represents the reservation identifier and the second item represents the seat numbers of the tickets (with A being the first row and 1 being the first column). The reservation requests will also be served in this order.

If the theater is unable to provide the requested number of seats, an error message will be printed and "FULL" will be written instead of seat numbers for that ticket.

## Variations of the Problem that the Algorithm can Adapt to
- Supporting theaters of different sizes and shapes (each row can have a different number of seats).
- If safety protocol is changed or lifted, the bubble size can easily be changed or removed.

## Improvements to be Considered
- Increasing the satisfaction level of seats towards the middle columns as well. This can be optional as people may prefer sitting towards the sides of rows as well.
- Adding the ability to handle overflow by placing overflowed seats near each other, not based on the quality of the location of seats.
- Considering seat reservation from a business standpoint and find the optimal seating for groups that may not maximize each group's satisfaction, but will allow more customers to be seated.