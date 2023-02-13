# Huffman-coding

Hufman endoding and decoding, Java implementation of Huffman encoding and decoding using priority queues
Huffman Coding is a technique of compressing data to reduce its size without losing any of the details. It was first developed by David Huffman. Huffman Coding is generally useful to compress the data in which there are frequently occurring characters.

First select browse and select your file, the output will be LIKE THAT:
#Note
( Tree :
for root (just total frequency for children). 
for parent (just total frequency for children, 0 or 1).
for leaf node (character, frequncey, 0 or 1).
0 = left edge.
1 = right edge )


example : 
The original string is:bccabbddaeccbbaeddcc


-----------------------------------Frequency-------------------------------
Char:a  Frequency:3
Char:b  Frequency:5
Char:c  Frequency:6
Char:d  Frequency:4
Char:e  Frequency:2


-----------------------------------Code-----------------------------------
Char:a  Code:011
Char:b  Code:10
Char:c  Code:11
Char:d  Code:00
Char:e  Code:010

The encoded string is:101111011101000000110101111101001101000001111

The decoded string is:bccabbddaeccbbaeddcc


-----------------------------------Tree-----------------------------------
                                                                20                                                                                                                              
                                9                                                              11                                                              
                d4                              5                              b5                              c6                              
        --              --              e2              a3              --              --              --              --              
--------------------------------------------------------------------------

Average number of bits used per character (BEFORE)= 8
Average number of bits used per character (AFTER)= 2.25
