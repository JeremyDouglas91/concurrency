CSC2002: Assignment 4 
DPLJER001
September 2018

NB! Before attempting to run any compiled java code in the bin folder via the make file, you make need to re-compile all the java source files as they have been compiled using the most recent JRE (10). If so, clean the bin folder and recompile the source code with:

	$ make clean
	$ make 

a. This folder contains all submission content for A3 in the following :

src: 		source code for solution program

bin: 		compiled java code

data: 		sample data files

Git Files	the git log record and git repository

Report.pdf: 	written report with test results 



b. To run the simulation do the following:

	1. Place the input/data file in the /data folder

	2. From the root directory enter the following command and fill in the name of the input file 
	   (if you want to run the program using the sample input provided, type "sample_input.txt"):


		$ make run_main input="<input file name>"


	3. Read the output on the console for confirmation of successful data load and other details about the program 


Thanks,
Jeremy 
