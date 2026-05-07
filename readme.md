# Assignment 3 Warmup: Read Me First


This project consists of the warmup exercises for Assignment 3 ("Assignment 3 Warmup").

The warmup is not assessed formally in any way.  

It consists of six exercises that ask you to apply different pieces of knowledge that you have gained throughout the 
course so far.  The goal of the warmup is to ensure that whatever your background, you have an opportunity to work through
developing a basic program solving a simple form of the assessment problem, where you can ask for help if you need it,
prior to the assessed stage where you are required to work individually.

If you are already a skilled Java programmer, you need not complete the warmup exercises at all.  The assessed part of 
assignment 3 (Assignment 3 Assessment) will provide self-contained instructions and requirements and if you want you can just look 
at this when the assessed version of Assignment 3 becomes available.

However, if you are not already an expert Java programmer, this warmup is intended to help ensure you are prepared for 
the assessment, ideally with a partial solution that can be modified to meet new requirements you will be given.

**Since the warmup is not assessed, you are free to work together on it, ask your favorite LLM, etc.  
However, this is not the case for the assessed stage of assignment 3, so it is essential that whatever you do during the 
warmup, you understand well enough to adapt in the assessment WITHOUT assistance.**

The simplified task in the warmup is to implement a simplified version of Cygnus: an inventory and sales system for a 
small bookstore, WordWorld.  Cygnus version 1.0 reads in an input file containing the store's current inventory
and a sequence of specifications of orders (e.g. book purchases), checks that they are valid (e.g. a book purchase 
doesn't decrease the inventory below zero) and calculates the cost of each valid order, including any applicable 
discounts or promotional deals.  
The output consists 
of a sequence of order records which include the data from the input orders plus some additional information 
such as validity and (if the order was valid) the cost/amount paid, as well as applicable discounts, followed by 
the inventory at the end of the day in the same format as the input uses.

The exercises that outline the main requirements and suggested steps for meeting them are provided as markdown files 
like this:

* `ex01-input-format.md`: defines the input format and normalization rules
* `ex02-output-format.md`: describes how to augment the input data with the required output fields, initially with dummy values.
* `ex03-validation.md`: describes the validation rules and how validity information is to be added to the output.
* `ex04-cost.md`: describes the cost rules and how cost information is to be added to the output.
* `ex05-update.md`: describes how successful orders should affect the inventory and store balance.
* `ex06-reflect-design.md`: suggests that once the coding exercises 1-5 are complete, you spend some time reflecting on the 
  code, and any design decisions, and documenting the design and/or code. 

There is also a `src/` directory containing a dummy `CygnusMain.java` file.  It is suggested that you keep this file and use its 
main method as the entry point to your solution to the warmup: you do not have to do this (we aren't going to check) 
but if you do, then adapting your warmup solution for use in the Assignment 3 assessment should be easier.

You may add any Java files you want to the `src/` directory.  For assignment 3 assessment submissions, we are going to 
ask that all Java files be immediately in the `src` subdirectory and not organized further into packages/subdirectories.  
So you may want to avoid that now, to avoid having to reorganize things later.  It isn't particularly good software 
engineering practice, but makes the automatic grading of the assessment easier. 


Some general observations/principles you might wish to follow are:

* Go from working to working.  Each exercise outlines a small piece of functionality that you can implement and test, 
  before considering other perhaps more complex features.  Make sure that your system works as a solution to exercise 1..n 
  before moving on to n+1.
* Test as you go.  The exercises include a running example that tests some of the functionality, but writing other 
  tests that consider corner cases that the provided tests don't exercise is a good idea.  
* Document / design as you go.  The sixth exercise encourages you to reflect on the design and document the code and 
  design at the end of the coding exercises.  But you can also do this incrementally, as you solve each exercise.  If 
  you do so then updating your notes to reflect the final design should be easy.
* The sequence of exercises is designed to present and break down the initial programming task into smaller natural 
  steps, so in principle you should be able to read each exercise, work on it, and go on to the next one.  However, 
  there are some points where you may benefit from having a more holistic view of the task as well, so it may be
  worthwhile to skim over all of the exercises before starting to work on the first one.