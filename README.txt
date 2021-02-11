Course: CSCI_576
Assignment 1
Name: Wuyang Liu
USC_ID: 9248262923

This program first reads an RGB format image and displays it, then converts it to YUV space and processes subsampling,
then adjust up-sampling to "fill the gap" of discard pixel information. Afterwards, it converts YUV data back to
RGB space and perform the quantization, eventually, it displays the processed image.

# Note: The original image will be overlapped by the processed one, please drag them apart manually.

############## Executing Instruction ##############
To run the code from command line, first compile with:

>> javac *.java

and then, you can run it to take in 5 parameters:

>> java Main <path to RGB Image> <Y> <U> <V> <Q>