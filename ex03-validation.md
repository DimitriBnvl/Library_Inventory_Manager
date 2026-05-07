# Exercise 3: Input validation and order acceptance


We will now move on to validation, one of the two main tasks of this assignment.  Validation simply means checking that
the data being read in follows certain rules, which we will outline below.  This subdivides into two problems: first, 
*input validation*, or checking that the store data as a whole meets certain conditions, and second, *order acceptance*, 
determining whether each order can be fulfilled given the current inventory.  

For input validation, we already did some of this as high-level syntactic checks in exercise 1, for example \
ensuring that all orders reference only products that exist, that the inventory doesn't contain duplicate product ids, 
etc.  We will now introduce some additional checks that require looking at the current inventory state more carefully.  
As in exercise 1, violations of the order acceptance rules results in an error message being printed as the only
output.

For the second form of checking, order acceptance, we will check each order can be successfully filled.  **Just for this
exercise**, we will consider each order separately against the original inventory and not the impact of previous orders.
We call orders where this is the case *accepted* and others *rejected*.  Instead of the whole run failing with an error 
when an order is rejected, we instead print the order with `rejected` or `accepted`.

You are strongly encouraged at this point to sketch out a class hierarchy for the inventories, orders, and store, and
identify appropriate fields and methods for meeting the requirements in this exercise.  It may even be helpful to skim 
over the next exercise as well to identify any additional requirements that might affect your design choices here.  
You will then want to define classes and/or methods for converting entry data structures into instances of appropriate 
classes, and perhaps identifying general operations that multiple classes use and collecting these into abstract methods 
in abstract superclasses.

You don't have to do this, it is perfectly possible to program this just by maintaining the data in an intermediate
data structure and crawling over it to check each entry, or indeed by maintaining the input as a string and modifying
it using string operations.  But as the number and level of complexity of requirements grows (and spoiler alert, they will), 
maintaining and adding to such an approach may become painful.



## Rules

### Input validation

For completeness, we rephrase the validation rules already mentioned in exercise 1, which should still be checked and lead 
to an error if violated.

1. The first record must be a `STORE` record and this should be the only such record.
2. There must be exactly one inventory block  `INVENTORY` with an ending `ENDINVENTORY` containing only `PRODUCT` lines 
3. Each `ORDER` should have a matching `ENDORDER` line containing only `ITEM` and `DISCOUNT` lines.
4. Each line should have the expected fields described in exercise 1.  However, extra fields are
   allowed and should just be ignored.
5. Fields in a line must have the required types or forms as described in exercise 1 (prices/order IDs should be 
   positive integers, quantities should be nonnegative integers, other fields should be identifiers, product 
   identifiers must start with a capital letter)
6. Orders with the same order ID are not allowed.
7. Product ids appearing in an `ITEM` record must appear in an inventory entry.
8. Product ids must not appear in more than one `PRODUCT` record in the inventory.
9. Only records  `STORE`, `INVENTORY`, `ENDINVENTORY`, `ORDER`, `ENDORDER`, `ITEM` and `DISCOUNT` can be used.

In addition, we consider the following WordWorld-specific rules, which if violated should also lead to an error:

1. There are four valid product types, `book`, `children`, `stationery`, and `game`.
2. The promotion code is either `_` (meaning no promotion applies) or an identifier made up of one or more of these
   letters, in alphabetical order without duplication: `c`, `f`, `h`, `r`, `t`.
3. The inventory balance, number of items in stock, product price, and order item quantity must all be nonnegative
   integers.  Prices and order item quantities must not be zero (but a stock level of zero is allowed, for example
   when a product has sold out).
4. There are four discount codes: `UNI`, `HEALTH`, `PROMO`, `EMPLOYEE`.

### Order acceptance

An order is acceptable if for each product mentioned in it, the number requested in the order is less than or equal 
to the number of items currently in stock.  Since orders are allowed to have multiple occurrences of the same product 
id, this check should take that into account, for example if an order looks like this:
```
ORDER|123
ITEM|B007|1
ITEM|B007|3
ENDORDER
```

but there are only 3 copies of product `B007` in stock, the order should be rejected, as it is the same as an order that 
asks for four copies all on one line.

For this exercise, implement input validation and order acceptance checking for the data and modify the output printer to 
mark acceptable orders as `accepted`.  

**IMPORTANT** The eventual goal of these warmup exercises is to check each order and then (if accepted) process it 
by updating the inventory before considering the next order, but, *for the time being*, we will only 
check all orders against the *initial* stock levels.  

This is another temporary simplification, sometimes called _scaffolding_, like the placeholder `rejected` and `0` values 
in exercise 2: it means two orders can both be accepted even though together they ask for more of a product than 
actually exists.  That is obviously not what we ultimately want, but it lets us get acceptance checking working in isolation.  
Exercise 5 will add the missing piece, updating stock as each accepted order is processed, at which point this 
behaviour will correct itself without you needing to change the acceptance logic here.


After having implemented validation/acceptance and wiring it up to the output, the output of the running example from exercise 1
should look like this:
```
STORE|WordWorld

ORDER|1001|accepted|0
ITEM|B001|2
ITEM|B002|2
DISCOUNT|UNI
ENDORDER

ORDER|1002|accepted|0
ITEM|B004|1
DISCOUNT|HEALTH
ENDORDER

ORDER|1003|accepted|0
ITEM|B002|2
ENDORDER

ORDER|1004|accepted|0
ITEM|S001|2
DISCOUNT|HEALTH
ENDORDER

INVENTORY|500
PRODUCT|B001|Pride_and_Prejudice|8|book|c|12
PRODUCT|B002|The_Hobbit|10|book|c|3
PRODUCT|B003|Dune|12|book|c|7
PRODUCT|B004|Clean_Code|25|book|r|5
PRODUCT|S001|Fancy_Pen|9|stationery|c|6
ENDINVENTORY
```

All four orders are accepted here, because each is being checked against the initial inventory in isolation.  
Order 1003 asks for 2 copies of `B002` and the initial stock is 3, so it passes, even though order 1001 has 
already claimed 2 of those copies.  Once exercise 5 is done and stock is actually deducted as orders are accepted, 
order 1003 will be rejected instead.  The price is still the placeholder `0` and the printed inventory is still 
unchanged; those are also dealt with in later exercises.