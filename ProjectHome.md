# Correlated sequences generator #
## General notes ##
The goal of the project is to create a tool for sequences with prescribed correlation properties generation.
### Sequences ###
Current version support two types of sequnce elements:
  * binary (each value is either 0 or 1);
  * ternary (elements from set {0, 1, 2}).
## Generation method ##
On first cycle initial population of random sequences is generated.
Each sequence is treated as a chromosome. On each evolution step population mutates and then selection is done.
It is a rough model with mutation only, i.e. crossover is not implemented yet.
### Mutation ###
Mutation starts with some initial rate and then changes depending on what strategy selected in options and result of previous mutations.
### Selection procedure ###
Selection means that sequence that is the closest to the desired properties survives: the strongest one gets all. :)
Measure of being close to desired properties is defined as average square of the correlator difference.