# Exercise 4: Costs

We will now consider the cost of each order.  The inventory keeps track of the price of each product individually, and 
by default the total cost of an order is the sum of the costs of the items in it multiplied by their quantity.

However, the final cost paid can be modified in a number of ways, due to individual discounts and promotions.

## Individual discount codes

WordWorld uses the following discount codes based on customer eligibility:

* `UNI` is a discount for university staff/students of 10%.
* `HEALTH` is a discount of 20% for people who work in healthcare.
* `PROMO` is used for special offers advertised on social media from time to time but is not currently being used.
* `EMPLOYEE` is an employee discount for WordWorld employees and their immediate family members.  At the moment it is
  being redesigned and is not active.

Items marked with the `r` promo code (described in the next section) are not eligible for these individual discounts.
This effectively means that when a customer has one or more of these codes, we split the order into two parts and 
calculate the individual discount on the eligible items and use the promotional discounts (if any apply) on the 
restricted items.


## Promotional discount codes

WordWorld uses the following promotion codes:

* `c`: means eligible for 3 for 2 combination offer.  In the warmup, this code can only be applied once, that is if 
  there are more than three eligible items, only one item is free (we will address the inevitable customer complaints 
  about this later). 
* `f`: reserved for future use; not currently active.
* `h`: reserved for future use; not currently active.
* `r`: means restricted discounts.  This item is not eligible for the individual discount codes mentioned in the 
  previous section (though if it has another promotion discount code it is eligible for that).
* `t`: reserved for future use; not currently active.
* `_`: used when no other code applies; should only ever appear on its own.

The codes are not mutually exclusive (except that `_` cannot appear with other codes), so for example `cr` can be used
to indicate that an item is not eligible for any individual discounts but can be combined with two other items with `c`
to activate a 3 for 2 offer.  


## Calculating percentage discounts

When calculating percentage discounts, we apply the percentage to each applicable item individually, 
round up to the next integer value, and subtract the rounded value from the original price.  For example, if there are two 
items with price £9 and £20 to which a 20% discount applies, then the discount on the £9 item is £2 (since 20% of 9 is 
1.8) and the discount on the £20 item is £4 (since 20% of £20 is exactly £4).  Thus the total price is (£9 - £2) + (£20-£4) = £23.     

## Finding the best discount

WordWorld prides itself on giving customers the best deal they are eligible for.  So when a customer is ready to pay 
for their books they are asked whether any of the discount codes apply and these are all added to their order.

Each item can benefit from only one discount. To find the best deal for a given customer we will do the following:

1.  Calculate the price with no individual discount code applied.  The only possible discount that can result at the 
    moment is if there are three or more items with the `c` promo code, counting quantities (so two copies of the same 
    product count as two items); in that case one copy of the least expensive such item is free.
2.  Separate the order into individual discount eligible items (no `r` code) and  ineligible ones (with `r`).
3.  Calculate the price of the ineligible items using the promotional discounts. 
4.  For each discount code the customer is eligible for, calculate the total price again just using that discount 
    on the eligible items (no promotional discounts apply).  
5.  Identify the most beneficial discount in step 4 and add the resulting price to the price of the restricted items 
    from step 3.  Also, record which discount code leads to this outcome so it can be printed in the order.  If more 
    than one discount code results in the same best combined price, pick the smallest in `String` order.
6.  Pick the lower of the prices from step 1 and step 5.  If they are equal, prefer the step 1 result (no individual 
    discount code applied, so no discount code is recorded for the order).  Otherwise, the discount code identified 
    in step 5 is the one used for the order.

Note that sometimes using the 3 for 2 is better, specifically if the individual discount savings is less than the cost 
of the least expensive 3 for 2 item, but the reverse can also be the case, for example if there are two very expensive 
3 for 2 items (e.g. two £100 rare books) but the third one is much cheaper (£10 which is less than 10-20% of £210).

In this exercise you should implement the above algorithm for finding the best price of an order given the discount 
codes provided.

**HINT:** While your goal is to eventually implement the full algorithm above, you may find it easier to get started by 
first implementing pricing with no discounts at all, then implement the "apply promotional discounts only" (step 1)
and "apply individual discounts only" (step 5) algorithms on their own, and test them, before trying to wire
them all together as outlined above.  

Once the best price and the relevant discount code (if any) achieving this is identified, these should be added to the output.  If the 
promotional codes alone without any individual discount code resulted in the best price, then there should be no 
discount code shown in the output.  In addition, even if the order was not accepted (due to items being out of stock), 
the output should show the correct (minimal) price and the corresponding discount code.  This is so that customers can 
be provided with this information along with encouragement to place their order again when their desired items are in 
stock. 


For example, the final output of the running example provided in exercise 1 should be:


```
STORE|WordWorld

ORDER|1001|accepted|28
ITEM|B001|2
ITEM|B002|2
ENDORDER

ORDER|1002|accepted|25
ITEM|B004|1
ENDORDER

ORDER|1003|accepted|20
ITEM|B002|2
ENDORDER

ORDER|1004|accepted|14
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