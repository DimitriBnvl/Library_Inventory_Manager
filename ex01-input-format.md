# Exercise 1: Input format

You work for a small business that has several retail clients for whom you support inventory 
and transactions.  For some reason, they all want to work exclusively with text files processed by Java code 
written by someone they know personally, perhaps because they are concerned by the increasing number of reports of big 
IT companies becoming heavily reliant on AI code generation tools that have a poor track record with security.

In this assignment, you will build a command-line Java program that:

 *   Reads a file containing a store's current inventory data and orders
 *   Processes each order one at a time and prints description of the accepted or rejected order
 *   Updates stock only for accepted orders
 *   When processing of the input file is complete, prints an updated inventory

In this exercise, we will start with the first step: reading the input.  The other steps will be dealt with by 
later exercises; we will add functionality gradually so that you can implement and test one component at a time.


During the warmup, you will focus on one client store, WordWorld Books.  WordWorld is the world's first bookstore to 
offer delivery by drone.  Orders come in via email, and the owner copy pastes the order into a terminal window running 
Java to check whether the books ordered are in stock before updating the inventory, attaching the books to the drone and 
sending them to the customer.  Fortunately, dealing with payment or the logistics of sending the drones to the right 
places, going and getting them if they get lost, etc. is not your problem.


## Command line interface

Your program must be able to run as follows:

```
java CygnusMain < <inputFile>
```
`inputFile` is an input file and we are using redirection to send it to the Java program's standard input.  You can also leave this out and manually copy-paste the input, or use IntelliJ to identify the input file to use here.

## Input file structure

Input to Cygnus is read from the standard input stream, which you can read using the `Scanner` or
`BufferedReader` classes.

Generally, Cygnus input files are sequences of records.  Each non-empty line is one record.  Fields are separated by |.
Lines starting with # are comments and all text on the remainder of that line is to be ignored.
Your program should ignore blank lines (that is, lines with only space ` `or tab `\t` characters before the end of line `\n`.)  

Formally we can describe the general structure using a grammar as follows:
```
records ::= record records
         |  EOF
record ::= identifier values '\n'
values ::= '|' value values
        | ϵ // nothing
value ::= identifier | integer
identifier ::= [a-zA-Z_][a-zA-Z0-9_]* 
integer ::= [0-9]+ 
```

Cygnus uses a number of additional conventions to organize the data in its files, not reflected in the above 
description.   The general structure of a Cygnus input file is as follows:

```
#Store name
STORE|WordWorld

INVENTORY|<balance>
PRODUCT|<productId>|<name>|<price>|<type>|<code>|<stock>
...
ENDINVENTORY

# Order 1
ORDER|<orderId>
ITEM|<productId>|<quantity>
...
DISCOUNT|<discountCode>
...
ENDORDER
# Transaction 2
...
```
It is OK for there to be no orders but the `INVENTORY` block is required.
The file should not end where the `INVENTORY` record has appeared and `ENDINVENTORY` has not, and likewise should not 
end in the middle of an order. 

If syntactic or structural constraints are violated, then the program should terminate and print an error message 
(see below).


### Inventory and Products

The `INVENTORY` record begins the inventory and has a balance field which gives the amount of cash on hand at the beginning 
of the day.

Product records have a product id, name, price, type, code, and stock (the type and code are typically used to annotate 
products that have some temporary discount).  In product records, `price` is 
an integer number of pounds, `name`, `type` and `code` are identifiers, and `stock` is a nonnegative integer representing 
the number of items of this product in stock. 

Identifiers are alphanumeric strings (using a-z, A-Z, 0-9 or underscores), and must begin with a letter or underscore. 

The `ENDINVENTORY` record ends the inventory section and has no data.

### Orders

The `ORDER` record begins an order, and assigns it an order id.
The order id is used to keep track of the order (in this warmup assignment, the main reason for this is to be 
able to match orders in the output easily with the ones in the input).  This is a positive integer and no two orders
in a given input should have the same id.  An order can contain one or more items, each including a product id 
(identifier) and quantity (positive integer).  Orders might contain multiple `ITEM` records with the same 
product ID and this means the same thing as if there were one `ITEM` record with the quantities combined.

`DISCOUNT` records add a possible discount to an order.  More than one `DISCOUNT` record can appear, if so, the eventual 
price of the order will be reduced by applying each discount separately, and taking the minimum resulting price.  This 
will be explained in more detail later, for now, all that is needed is to know that the discount codes are identifiers.  

The `ITEM` and `DISCOUNT` records can be in any order without affecting the meaning of the order.  The order is terminated 
by `ENDORDER`.

## What to do

Implement a class that reads in data of this form, checks that it matches the required format, and then prints it back
out in a standardized form.

### Error handling

As discussed above, inputs that do not match the required format should result in an error.  Specifically, the following 
problems should result in errors.

1. Missing inventory block or `INVENTORY` with no `ENDINVENTORY`
2. `ORDER` line with no matching `ENDORDER` line
3. Missing expected fields in a given line, e.g. `PRODUCT` with only two additional fields.  However, extra fields are
   allowed and should just be ignored.
4. Fields in a line not of required type or form (prices/quantities should be integers, other fields should be 
   identifiers, product identifiers must start with a capital letter)
5. Multiple orders with the same order ID.
6. Product id appearing in an `ITEM` record that do not also appear in an inventory entry.
7. A product id appears in more than one `PRODUCT` record in the inventory.
8. Any use of  record names besides `STORE`, `INVENTORY`, `ENDINVENTORY`, `ORDER`, `ENDORDER`, `ITEM` and `DISCOUNT`.

In the event that the input violates any of the expectations above, the output should consist of the token `ERROR`
which may be followed by any additional content (such as a more useful error message).  There should not be any other
output before the `ERROR` line.

A sensible strategy for accomplishing this is to check the various requirements as you read the input, and raise an
exception if a violation is detected, and eventually handle the exception by printing an `ERROR` line.

### Standardized form

The format accepted for inputs is relatively permissive, and so allows writing the same information in several 
different ways (e.g. due to the presence of comments, blank lines, and the possibility of duplicate product ids or
codes in orders, and the fact that the order of appearance of some of these things does not matter). 

The required standardized form is as follows:

1.  Products in the inventory should be printed out in increasing order of product id.  
2.  The input allows numbers like 007 which have leading zeros, which do not affect the value of the number.  Number 
    values should be printed without any leading zeros.
3.  The `STORE` record should have exactly one blank line separating it from the first `ORDER` and similarly there
    should be exactly one blank line between `ENDORDER` and the next `ORDER` or `INVENTORY` line.
4.  Orders should be printed out in the same order in which they appeared in the input, not sorted by order id. 
5.  In an order, the `ITEM` records should appear first, then the `DISCOUNT` records.
6. `ITEM` records should be sorted by product id and any duplicate product id records should be merged, adding their 
    quantities together. 
8. `DISCOUNT` records should be sorted by discount code and if a discount appears more than once in the input it should 
    only appear once in the output.

HINT: Besides `Scanner`, Java standard library classes such as `HashMap` and `List`, and methods such as `sort`, are
very helpful for this exercise.

## Running example

The following input file will be used as a running example throughout the warmup exercises.  It deliberately includes
a few things that need normalizing (leading zeros, products out of order, repeated items/discounts, irregular blank
lines) so that you can check your implementation against it.

### Example input

```
# WordWorld daily input
STORE|WordWorld


INVENTORY|500
PRODUCT|B003|Dune|12|book|c|7
PRODUCT|B001|Pride_and_Prejudice|008|book|c|12
PRODUCT|B002|The_Hobbit|10|book|c|03
PRODUCT|B004|Clean_Code|25|book|r|5
PRODUCT|S001|Fancy_Pen|9|stationery|c|6
ENDINVENTORY
# First order
ORDER|1001
ITEM|B002|1
DISCOUNT|UNI
ITEM|B001|2
ITEM|B002|1
ENDORDER

ORDER|1002
DISCOUNT|HEALTH
ITEM|B004|01
DISCOUNT|UNI
DISCOUNT|HEALTH
ENDORDER

ORDER|1003
ITEM|B002|2
ENDORDER

ORDER|1004
ITEM|S001|2
DISCOUNT|HEALTH
ENDORDER
```

### Expected output after Exercise 1

```
STORE|WordWorld

INVENTORY|500
PRODUCT|B001|Pride_and_Prejudice|8|book|c|12
PRODUCT|B002|The_Hobbit|10|book|c|3
PRODUCT|B003|Dune|12|book|c|7
PRODUCT|B004|Clean_Code|25|book|r|5
PRODUCT|S001|Fancy_Pen|9|stationery|c|6
ENDINVENTORY

ORDER|1001
ITEM|B001|2
ITEM|B002|2
DISCOUNT|UNI
ENDORDER

ORDER|1002
ITEM|B004|1
DISCOUNT|HEALTH
DISCOUNT|UNI
ENDORDER

ORDER|1003
ITEM|B002|2
ENDORDER

ORDER|1004
ITEM|S001|2
DISCOUNT|HEALTH
ENDORDER
```