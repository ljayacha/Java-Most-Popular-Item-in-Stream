# Java-Most-Popular-Item-in-Stream
Most Popular Item in Stream

Your program should take input from stdin. The input contains a decaying constant c
(a double floating-point number, e.g., 0.000000001), and a <host>:<port> pair. Then
you create a thread which opens a socket and connects to the <host>:<port>, where
you can receive an input stream of pairs of timestamp (starting from 0) and itemIDs,
both are 32-bit unsigned integers. You need always waiting query (i.e., “What is the
most current popular itemID?”) from stdin and once the query comes, your program
should output the current timestamp and the most “current” popular itemID.
  
It is more easy to use an exponentially decaying window than to a fixed window with
count. When an itemID comes, you check if you see the itemID before or not. If not,
create a new sum for it and initialize it to 1. Otherwise, you multiply all sums (for all
the other itemIDs), and only add 1 to the sum of the itemID. If any sum is below ½,
drop the sum (in this case, 2/c is the limit on the number of sums you keep).
