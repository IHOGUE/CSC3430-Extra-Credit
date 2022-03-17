# CSC3430 Extra Credit

# Implementation of the Dynamic Programming Weighted Interval Scheduling Algorithm
A work by: Isaiah Hogue

## Introduction
*Describe what your project is about and what language you used*

The project is written in Java, and implements the Dynamic Programming Weighted Interval Scheduling Algorithm. Specifcally, the bottom-up dymamic programming method (unwind recursion) that uses iteration is implemented. I've also created a method of keeping track of the jobs that make up the maximum weight subset of jobs, and the ability to print the grid of jobs and color highlight those solution jobs in the grid. Most aspects of the program are configured in the `settings.txt` file.

## Requirements

This program was written and compiled with JDK 17, so Java Runtime Environment 8 and above should work. No additoinal libraries were used.

## Code Guide
The program is broken into four parts: Load the variables from `settings.txt`, generate jobs, run the algorithm, then display the results.

#### Load Settings

The User manual will go into detail about how the `settings.txt` file influences the program.

#### Generate Jobs

If the jobs are being read from a given .csv file, then the file will be parsed and a Job is created for every line. Otherwise, a number of jobs equal to `NUMBER_OF_JOBS` will be generated with a random start time, end time, and weight. 

- Start time is a random number between 0 and (`MAXIMUM_TIME` value - 1) 

- End time is a random number between start time and the value of `MAXIMUM_TIME`

- Weight is a random number between 1 and the value of `MAXIMUM_WEIGHT`

#### Bottom-up Dymamic Programming (Unwind Recursion) Algorithm

ds

#### Display the Results

dds

## User Manual
*Once a person clones this into their computer how the person is supposed to run the program, add screenshots showing how your program works, also add here the link to the Youtube video showing the program running*

The following settings in the `settings.txt` file control what the program will do:

`DISPLAY_GRID`

Write true or false here. Decideds whether to print the display of jobs. The height and width are decided by the number of jobs and the maximum end time value respectively.


`GRID_COLOR`

Write true or false here. Decides whether the displayed grid will color highlight the jobs that make up the solution set of maximum weight of mutually compatible jobs. If instead of color you get number values, your terminal doesn't support the ANSI stuff used. Ubuntu should support it though. Command prompt doesn't.

`PRINT_JOBS`

Write true or false here. Decides whether to print jobs generated, whether from file read or random generation. Impractical with high number of jobs.

`LOAD_FILE_NAME`

Enter the file name between the double quotes. If you want to generate a random set of jobs, leave "". If jobs are read from a csv file, then the `NUMBER_OF_JOBS` and `MAXIMUM_TIME` are measured from the file as it's parsed.

*For randomly generated jobs*

`NUMBER_OF_JOBS`

Decides how many random Jobs to generate.

`MAXIMUM_TIME`

The lower bound is always 0, so this setting configures the upper bound for the job's start time.

`MAXIMUM_WEIGHT`


The lower bound is always 0, so this setting configures the upper bound for the job's weight.

*Misc notes:*

For my 15 inch screen, 39 is the max value for `MAXIMUM_TIME` before the display extends past the terminal window in full screen mode. Additoinally, while the display can handle a large amount of jobs, the usefulness of the display dimisnhes the more time you spend scrolling. For a large `MAXIMUM_TIME` value and large number of jobs, set `LOAD_FILE_NAME` to `""`.