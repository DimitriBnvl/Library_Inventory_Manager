# Design Document: Cygnus Warmup

## Overview

Cygnus reads an input file from stdin describing a store inventory and a sequence 
of orders. It validates each order, calculates the best price, updates the inventory, 
and prints the results to stdout.

## Classes

### `CygnusMain`
Entry point. Responsible for parsing all input line by line using a `Scanner`, constructing `Product` and `Order` objects, and printing the final output. 
After parsing, it processes each order sequentially — calling `computeAcceptance` then `computePrice`, and if accepted, updating stock and balance 
immediately so that later orders see the reduced inventory.

### `Order`
Represents a customer order. Stores the order ID, items (product ID → quantity),
the customer's input discount codes, and the computed status, price, and applied 
discount code.

Key methods:
- `computeAcceptance(inventory)` — checks every item against current stock; sets status to `"accepted"` only if all items are available.
- `computePrice(inventory)` — implements the full pricing algorithm (see below).
- `applyPromoDiscount(subset, inventory)` — applies the 3-for-2 offer to any given subset of items.
- `applyIndividualDiscount(subset, rate, inventory)` — applies a percentage discount with ceiling rounding to a given subset of items.

### `Product`
Represents a product in the inventory. Stores product ID, name, price, type, promo 
code, and stock. The `parse()` static method validates and constructs a `Product` 
from a line of input. `reduceStock(quantity)` subtracts from stock after an accepted order.

## Key Data Structures

| Field | Type | Purpose |
|---|---|---|
| `inventory` | `TreeMap<String, Product>` | Products sorted by ID for consistent output |
| `items` | `TreeMap<String, Integer>` | Order items sorted by product ID |
| `discountCode` | `TreeSet<String>` | Customer's input discount codes, sorted |
| `appliedDiscountCode` | `String` | The discount code that achieved the best price (empty if promo-only won) |

`TreeMap` and `TreeSet` are used throughout instead of `HashMap`/`HashSet` to ensure alphabetically sorted output without any extra sorting step.

## Pricing Algorithm

`computePrice` implements a six-step algorithm:

1. Calculate the promo-only price across all items (`applyPromoDiscount` on the full item set).
2. Split items into **restricted** (promo code contains `r`) and **eligible** (no `r`).
3. Calculate the promo price on restricted items only.
4. For each active individual discount code the customer holds (UNI=10%, HEALTH=20%), compute: restricted promo price + individual discount on eligible items.
5. Find the best (lowest) result from step 4, breaking ties by alphabetical order of the discount code.
6. Set `totalPrice` to the lower of step 1 and step 5. If equal, step 1 wins and no discount code is recorded.

`applyPromoDiscount` expands items by quantity into a flat list of prices, and if three or more items have the `c` promo code, subtracts the cheapest one (3-for-2, applied once only).

`applyIndividualDiscount` computes the discount per item as `ceil(price × rate)` and charges `price − discount` per copy.

## Design Decisions

**`applyPromoDiscount` takes a subset parameter** rather than always using 
`this.items`. This allows it to be reused for both the full order (step 1) and 
the restricted subset (step 3) without duplicating code.

**`appliedDiscountCode` is separate from `discountCode`**. The `discountCode` 
field holds the customer's input codes; `appliedDiscountCode` holds which one 
(if any) actually produced the best price. This distinction is necessary because 
the best price may come from the promo-only path with no individual discount applied.

**Orders are processed sequentially, not in batch**. After each accepted order, 
stock is reduced immediately. This means later orders naturally get rejected if 
earlier ones have exhausted stock, without any changes to the validation logic.

**PROMO and EMPLOYEE codes are parsed and validated but treated as inactive**. 
The switch in `computePrice` returns rate `-1` for these, causing them to be 
skipped. Activating them in the future only requires updating the switch.

## Potential Future Changes

- **Activating PROMO/EMPLOYEE discounts**: add their rates to the switch in `computePrice`.
- **Multiple 3-for-2 applications**: currently capped at one free item; would require changing 
`applyPromoDiscount` to subtract one item per group of three.
- **New promo codes (f, h, t)**: reserved in the spec; new behavior would be 
fadded to `applyPromoDiscount` or a new helper.
- **New product types**: just add to the valid set in `Product.parse()`.