#Lab Description
 Implemented Apriori Algorithm for Linux environment with scratch
 This Lab is one of my first Lab as a student of the Computer Science and Engineering major. 

# use this input for the small database file
python3 Apriori.py -database_file=database_small.txt -minsupp=0.538 -output_file=output.txt


-database_file: input data
-minsupp: minimum support(in percentage)
 

#Explanation of each function

  # generate_F1()
    I used List and Matrix to make Frequent 1-itemsets
    List is used for make sparseMatrix and compare supports and minimum supports
    Matrix is used for count support in 1 items. I used sparseMatrix for count support

  # generate_candidate()
    I used set and list to find candidate of frequent itemsets
    Set gives union of k-1 itemsets, that is k itmesets
    List is usd for make return value as List

  # prune_candidate()
    I used set and list like generate_candidate( ) function
    Use Set for make subsets and pruning infrequent itemsets
    List is used for return value as List

  # count_support()
    I used set and list to count supports
    Set is used to find the element of candidate itemsets is existed in transactions or not
    List is used for return value: itemsets and its counts

  # eliminate_candidate()  
    I used List to eliminate infrequent itemsets using minimum supports
    also Find minimum supports of each k steps
  
 