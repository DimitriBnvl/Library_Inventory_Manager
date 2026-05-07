# Exercise 2: Output format


The output format of Cygnus for WordWorld is similar to the normalized input format discussed in the previous
exercise, in particular, it uses the format for records as lines separated by `|`.
The main differences are due to the fact that the output reflects what happened retrospectively after the
orders were processed on the initial inventory.  Since you will be processing the transactions as they come in,
you will not know the ending inventory when you need to start producing output, so we will put the ending inventory at 
the end of the output, and output the processed versions of the order transactions as they arrive.  

So, the output will generally look like this:

```
# Store name
STORE|WordWorld

# Order 1
ORDER|<orderId>|<status>|<price>
ITEM|<productId>|<quantity>
...
DISCOUNT|<discountCode>  
...
ENDORDER
# Order 2
...


INVENTORY|<balance>
PRODUCT|<productId>|<name>|<price>|<type>|<code>|<stock>
...
ENDINVENTORY
```

Aside from the fact that the inventory is now at the end, the differences are the following:

* `ORDER` records have two extra fields, `status` and `price`.  The `status` is an identifier (`accepted`/`rejected`). 
  Since we have not yet implemented validation, we will just put `rejected` here.  The `price` is 
  likewise the calculated price of the order with the best discount, but since we are not yet calculating this either 
  we will put 0 here as a placeholder.
* `ORDER` records in the output should have just one `DISCOUNT` showing the discount code that was actually applied.  
  Since we don't know this yet, we will just set this to be the smallest `DISCOUNT` code in the default `String`-derived 
  order on identifiers.  If no discount codes were provided, the `DISCOUNT` record should be omitted entirely.
* The `balance` in `INVENTORY` and the `stock` values in `PRODUCT` are intended to reflect the changes.  Since we are 
  rejecting all orders at the moment because the order processing functionality hasn't been implemented yet, we can just 
  use the values from the original input here.   

This exercise is very simple: it just involves adding code to the normalizer implemented in exercise 1 to add the above 
dummy fields, and remove all but the first (in alphabetical order) discount code.  The orders and inventory should also 
be normalized as per the rules in the previous exercise, and there should be exactly one blank line between `STORE` and the 
first `ORDER`...`ENDORDER` transaction block (if any), and between the end of each order block `ENDORDER` and the beginning of the next `ORDER` or the final `INVENTORY` block.  Note that this is essentially the same format as used for the output in exercise 1, but since the inventory is at the end, we explain it slightly differently.

Continuing the running example, the output after making this change should be:

```
STORE|WordWorld

ORDER|1001|rejected|0
ITEM|B001|2
ITEM|B002|2
DISCOUNT|UNI
ENDORDER

ORDER|1002|rejected|0
ITEM|B004|1
DISCOUNT|HEALTH
ENDORDER

ORDER|1003|rejected|0
ITEM|B002|2
ENDORDER

ORDER|1004|rejected|0
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

If you have already implemented an internal data structure for representing the entries and fields, this should be very
easy.  If you have not done that yet, now is a good time.