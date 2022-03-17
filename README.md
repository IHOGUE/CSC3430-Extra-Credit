# CSC3430 Extra Credit
 
# Implementation of the Dynamic Programming Weighted Interval Scheduling Algorithm
A work by: Isaiah Hogue
 
## Introduction
 
The project is written in Java and implements the Dynamic Programming Weighted Interval Scheduling Algorithm. Specifically, the bottom-up dynamic programming (unwind recursion) that uses iteration is implemented. I've also created a method of keeping track of the jobs that make up the maximum weight subset of jobs, and the ability to print the grid of jobs and color highlight the solution on the grid. Most aspects of the program are configured in the `settings.txt` file.

![Alt text](https://github.com/IHOGUE/CSC3430-Extra-Credit/blob/main/out/artifacts/WeightedIntervalScheduling_jar/Output.png?raw=true "three Result")
 
## Requirements
 
This program was written and compiled with JDK 17, so Java Runtime Environment 8 and above should work. No additional libraries were used.
 
## Code Guide
The program is broken into four parts: load the variables from `settings.txt`, generate jobs, run the algorithm, and display the results.
 
#### Load Settings
 
Reads the `settings.txt` file inside the same directory as the jar file. The file is parsed and values are loaded into their respective variables. The User Manual will go into detail about how the `settings.txt` file influences the program.
 
#### Generate Jobs
 
If the jobs are being read from a given .csv file, then the file will be parsed and a `Job` object is created from every line. Otherwise, a number of jobs equal to `NUMBER_OF_JOBS` will be generated with a random start time, end time, and weight.
 
- Start time is a random number between 0 and (`MAXIMUM_TIME` - 1)
 
- End time is a random number between start time and the value of `MAXIMUM_TIME`
 
- Weight is a random number between 1 and the value of `MAXIMUM_WEIGHT`
 
These values are stored in the `GridVariables` class.
 
#### Bottom-up Dynamic Programming (Unwind Recursion) Algorithm
 
- After all of the `Job` objects are created they are stored in an array called `Jobs`, and a clone is made of the array named `unsortedJobs`. This is to be able to preserve the original order and display it as a grid at the end. `Jobs` is then sorted by end time using the `Arrays.sort` method using an O(nlogn) algorithm.
 
- After sorting by end time, a loop goes through each job and finds "p", the first compatible job available with a lower end time, where compatible means there is no time conflict. Then a HashMap called `MaxMap` (<`Job`,`Integer`>) is created, and will be used to store the max value for each job. This is how memoization is implemented to make the algorithm efficient. Then a List `solutionPath` <`String`> is created to keep track of the subset of total jobs which make up the solution.
 
*The Heart of the Algorithm*
 
A loop is entered, starting at `j = 1` until `NUMBER_OF_JOBS`, and for every job, it stores the higher value between `max1` or `max2` into `MaxMap`, and sets the current job's `maxWeightJob` to either the job.p or the job at the index of the current job - 1.
- `max1`: The weight of the current job + the max value of the job.p (the first compatible job with a lower end time)
- `max2`: The max value of the job one index below the current job
 
Additionally, if `max1` is higher than `max2`, then a `boolean` value `usesSelf` inside of the current job will be marked true. Therefore, only jobs that are part of the solution will be marked true.
 
A variable `numMax` keeps track of the highest weight recorded during the loop.
 
 
#### Display the Results
 
Once `numMax` has stored the solution (maximum weight given jobs), a HashMap `jobMap` (<`String`,`Job`>) is created, and every job is stored in the HashMap by its name, and if `PRINT_JOBS` is set to true, then every job is printed as well. Then a `Job` `temp` is created and set to the job with the highest max value. Then `while(temp != null)` is entered, and if `temp.usesSelf` is set to true, add its name to `solutionPath`. Then set `temp = temp.maxWeightJob`, and the loop continues until `temp == null`.
 
 After this, a message prints the maximum weight value found, the list of jobs `solutionPath` that make up that max weight, and then prints out each job's value (start time, end time, weight, etc.) using each job's name in `solutionPath` as the key to find the matching job in `jobMap`.
 
Finally, depending on what `DISPLAY_GRID` and `GRID_COLOR` are set to, either the program ends or the program constructs and displays a grid of the jobs to the terminal. If `GRID_COLOR` is set to true, then the jobs that make up the solution will be highlighted in yellow. The grid's dimensions are (x = time from 0 to highest end time value)(y = number of jobs).
 
## User Manual

To run the program, download and unzip `WeightedIntervalScheduling.zip` and execute the jar file from the command line. 

`java -jar WeightedIntervalScheduling.jar`

The following settings in the `settings.txt` file control what the program will do:
 
`DISPLAY_GRID`
 
Write true or false here. Decides whether to print the display of jobs. The height and width are decided by the number of jobs and the maximum end time value respectively.
 
 
`GRID_COLOR`
 
Write true or false here. Decides whether the displayed grid will color highlight the jobs that make up the solution set of maximum weight of mutually compatible jobs. If instead of color you get number values, your terminal doesn't support the ANSI stuff used. Ubuntu should support it though. Command prompt doesn't.
 
`PRINT_JOBS`
 
Write true or false here. Decides whether to print jobs generated, whether from file read or random generation. Impractical with a high number of jobs.
 
`LOAD_FILE_NAME`
 
Enter the file name between the double quotes. If you want to generate a random set of jobs, leave "". If jobs are read from a csv file, then the `NUMBER_OF_JOBS` and `MAXIMUM_TIME` are measured from the file as it's parsed.
 
*For randomly generated jobs*
 
`NUMBER_OF_JOBS`
 
Decides how many random jobs to generate.
 
`MAXIMUM_TIME`
 
The lower bound is always 0, so this setting configures the upper bound for the job's start time.
 
`MAXIMUM_WEIGHT`
 
 
The lower bound is always 0, so this setting configures the upper bound for the job's weight.
 
*Misc. notes:*
 
For a 15-inch screen, 39 is the max value for `MAXIMUM_TIME` before the display extends past the terminal window in full screen mode. Additionally, while the display can handle a large amount of jobs, the usefulness of the display diminishes the more time you spend scrolling. For a large `MAXIMUM_TIME` value and large number of jobs, set `DISPLAY_GRID` and `PRINT_JOBS` to false.
