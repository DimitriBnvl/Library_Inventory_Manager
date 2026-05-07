# Exercise 5: Updating the inventory


Once an order has been processed, and if it was accepted, we also need to update the inventory to reflect the sale.

Exercises 3 and 4 didn't ask you to do this, but your solutions to them already involve reading the inventory so that
orders can be checked and costs computed, and you may have already noticed that it is going to be necessary to update
the inventory as accepted orders are processed.  

In this exercise, you should now do this: specifically, once an order is accepted and its cost is calculated, you should:

1.  Subtract the quantities of ordered items from the corresponding stock entries in the inventory.
2.  Add the amount paid to the store's balance.


Moreover, you should ensure that the resulting updated inventory is used to process any remaining orders, and once 
the orders in the input are all processed, the final store balance and inventory are printed out.  

Depending on how you implemented exercises 3 and 4, it may be straightforward to modify them so that accepted orders 
are applied ot the inventory, or it may take some thought how to reorganize things do to this.  

As mentioned in exercise 3, once the inventory is actually being updated after each accepted order, the stock check 
from exercise 3 will automatically give the right answer for orders that arrive after earlier ones have claimed some 
of the available stock.  This means that some orders that were previously accepted (because the stock check was run 
against the initial inventory in isolation) will now be rejected instead.  You shouldn't need to change the validation 
logic itself for this to happen.

To test this exercise is implemented correctly, you should come up with some examples of inputs that affect the inventory 
in different ways: for example, purchases of unrelated items, or sequences of purchases of the same item that together will 
eventually exhaust the stock of some item.  Continuing the running example from before, we would expect to see output like this:

```
STORE|WordWorld

ORDER|1001|accepted|28
ITEM|B001|2
ITEM|B002|2
ENDORDER

ORDER|1002|accepted|25
ITEM|B004|1
ENDORDER

ORDER|1003|rejected|20
ITEM|B002|2
ENDORDER

ORDER|1004|accepted|14
ITEM|S001|2
DISCOUNT|HEALTH
ENDORDER

INVENTORY|567
PRODUCT|B001|Pride_and_Prejudice|8|book|c|10
PRODUCT|B002|The_Hobbit|10|book|c|1
PRODUCT|B003|Dune|12|book|c|7
PRODUCT|B004|Clean_Code|25|book|r|4
PRODUCT|S001|Fancy_Pen|9|stationery|c|4
ENDINVENTORY
```