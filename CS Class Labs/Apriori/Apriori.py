###### Jinhee Lee
###### Input for Linux Env.
###### python3 Apriori.py -database_file=database_small.txt -minsupp=0.538 -output_file=output.txt
###### python3 Apriori.py -database_file=database_large.txt -minsupp=0.82 -output_file=output.txt

import argparse
from itertools import combinations

##### No.1 
def Parser():                  # make Parser for execute
    parser=argparse.ArgumentParser()
    parser.add_argument('-database_file')
    parser.add_argument('-minsupp')
    parser.add_argument('-output_file')

    return parser


##### No.2
## read database
def read_database(fileName):    

    transactions=[]
    item=[]

    count=0

    for i in open(fileName, 'r'):
        i=i.strip()
        data=i.split(' ')
        data=[int(a) for a in data]   # Make strings to Integers

        if count != 0:                  
            transactions.append(data)  # transactions show all transaction data in database
        else:
            item.append(data)       # item is not transaction, it shows the number of Transactions and items   
        count=count+1

    database=[transactions, item]

    return database    

##### No.4

### Find frequent 1-itemsets

def generate_F1(database, minsupp):

    
    totTran=database[1][0][0]   # Total number of transactions in database               
    totItem=database[1][0][1]   # Total kinds of items in database

    database=database[0]       # all transactions in database    

    #print(totTran)
    #print(totItem)    

    # Make items
    itemName=[]

    for i in range(0, totItem, 1):
        itemName.append(i)

    # make sparseMatrix of Transactions
    sparseMatrix=[]
    for i in range(0, totTran, 1):
        Num=[]
        for j in range(0, totItem, 1):
            #Num.append(database[i].count(j))    # Checking whether item is existed,
            if j in database[i]:
                Num.append(1)
            else:
                Num.append(0)
                
        sparseMatrix.append(Num)            # Make SparseMatrix of database
                                        # if Item is exist = marked 1 or not exist = marked 0
    

    trSparseMatrix=list(map(list, zip(*sparseMatrix)))  # to count the number of each items ->transpose sparse matrix

    counts=[]
    for i in range(0, totItem, 1):
        counts.append(sum(trSparseMatrix[i]))       # count the number of each items(items number 0 to 29)

    itemset1=[itemName, counts]                # itemset1 is not classified as frequent itemsets
                                               # It shows each items and the number of each items
                                                
    if counts:                         # minimum support 
        minsupp=min(counts)+(max(counts)-min(counts))*minsupp  

                                                   
    Freqitems=[]
    frequent=[]
    for i in range(0, totItem, 1):
        if itemset1[1][i] >= minsupp:             # Frequent 1-itemset is greater than minsupp
            Freqitems.append(itemset1[0][i])     # make frequent 1-itemsets
            frequent.append(itemset1[1][i])

    F1_itemsets=[Freqitems, frequent]           # Frequent 1-itemsets
    
    key=F1_itemsets[0]                        # make dictionary using key, value to check item and its count
    value=F1_itemsets[1]                     
    diction=dict(zip(key, value))
    #print(diction) 

    
    F1=[]

    for key, value in diction.items():  # key is item number and value is count(support)
        if value >= minsupp:            # if support of a item in 1-itemsets is greater than minimum support
            item=set([key])
            F1.append(item)             # make it member of Frequent 1-itemsets

    return F1

                

### Find candidate of Fk+1 itemsets 
def generate_candidate(Fk, Fk_, k):       # Find all possible k+1 itemsets using frequent k itemsets
                                          # using Fk-1 x Fk-1 method
    #print(Fk)
    
    Ck1=[]                                # Ck1 means Ck+1

    for firstFk in Fk:
        for secondFk in Fk_:
            itemset=set(firstFk)       # make new itemset : { k-1 }
            for item in set(secondFk):  # choose only one item in second Fk-1 itemsets : { 1 }
                itemset.add(item)            # add two Fk-1 itemsets as one itemset : {k-1 +1} -> { k } 

            if len(itemset) == k and itemset not in Ck1:            # this new itemset is k-itemsets, so its length should be equal to k
                Ck1.append(itemset)   # make new set using this k-itemsets
                                             # these new k-itemsets are candidate of k-itemsets
    return Ck1            


### Pruning candidates itemsets
def prune_candidate(Ck_1, Fk, k):        # Remove infrequent itemsets in candidate of Frequent k itemsets
                                         # It can help to remove operations about infrequent itemsets
    #print(Ck_1)
    
    Lk1=[]                               # Lk1 means Lk+1    

    for Ck1Member in Ck_1:                 # get element sets in the candidate itemset        
        subsets=set(combinations(Ck1Member,k-1))  # make all subsets of candidate itemsets
                                               # subsets are only composed of k-1 element subsets
        for element in subsets:                    
            element=set(element)           
            if element in Fk and Ck1Member not in Lk1:  # This element must be frequent-itemsets to be a candidate              
                Lk1.append(Ck1Member)                   # infrequent itemsets are not included in Lk+1
    
    return Lk1
            

### Counting supports of each k-itemsets
def count_support(database, Lk_1):  # At eliminating step, supports will be compared to the minimum support

    database=database[0]

    #print(Lk_1)    

    key=[]                      # this key shows itemsets
    value=[]                    # this value shows support count of key's itemsets
   

    for Lk1Member in Lk_1:      # using pruned candidate itemsets to count each itemsets support
       
        count=0
        transNum=[]
        for transaction in database:      # itemsets in each transactions
            transItemset=set(transaction)           # make each transactions as set
            
            if Lk1Member.issubset(transItemset):  # find transactions than contains k-itemsets as its subset
                count+=1                # count its number
                    
        key.append(Lk1Member)   # it shows k-itemsets
        value.append(count)     # it shows count(support) of each k-itemsets
               

    k1itemsets=[]
    for i in range(0, len(key), 1):
        k1itemsets.append((key[i], value[i])) # make (itemsets, count) format to use at eliminate_candidate steps   

    return k1itemsets


### Eliminating itemsets compare to the minimum support
def eliminate_candidate(support_itemsets, minsupp):  # this step is eliminating step using support and minimum support
                                            # if support is less than minimum support, it will be removed
    #print(support_itemsets)

    count=[]                                       
    s_i=support_itemsets
    for i in range(0, len(s_i), 1):        # to apply minimum support, find itemsets count range
        count.append(s_i[i][1])        

    if count:
        diff=max(count)-min(count)
        minsupp=min(count)+diff*minsupp    # find new minimum support for this itemsets
        
    #print(minsupp)
    Fk1=[]                                 # Fk1 is Fk+1
    for i in range(0, len(s_i), 1):
        if s_i[i][1] >= minsupp:           # if support(the number of itemsets in transactions) is greater than minsupp  
            Fk1.append(s_i[i][0])          # attach to the Fk+1. This step makes eliminating infrequent itemsets

    return(Fk1)
 
def output_freq_itemsets(output_file, transactionNum, itemNum):         # make output file
    with open(output_file, 'w+') as ofile:                 
        ofile.write(str(len(transactionNum)) + " " + str(itemNum))      # use for write and show number of transactions and items
        print(str(len(transactionNum)) + ' '+ str(itemNum))
        for i in transactionNum:                                #  use for write and show all k-itemsets
            string = " ".join(str(j) for j in i)          # make set to string
            print(string)
            ofile.write(str(string))
            ofile.write('\n')      

###### No.3
### Using Apriori algorithm to find frequent itemsets from a transaction database 
def apriori(database, minsupp, output_file,itemNum):

    k=1               # first, itemsets have one item in each itemsets 
                      # Generate F1 = {frequent 1-itemsets}
    F1=generate_F1(database, minsupp)    # return each number of items in 1-itemsets

    Fk=F1             # Firstly, Frequent 1-itemsets are Frequent k-itemsets
    Fk_=Fk            # it is used for Fk-1 x Fk-1 method

    #print(Fk_)

    # It use to make ouput file as input file,
    transactionNum=[]
    for i in Fk_:
        transactionNum.append(i)    # this calculate itemsets in output data. Here for F1       


    k=2    # After make Frequent 1-itemsets, make 2-itemsets using Frequent 1-itemsets

    ## Repeat until Fk is empty
    while len(Fk) > 0:      # Do operations until Fk will be zero : itemsets at k is not zero -> do this iteration
        
        ## Generate Lk+1 from Fk             
        generate_candidate(Fk, Fk_, k)                   # It shows candidates of Frequent (k+1)-itemsets
        Ck_1=generate_candidate(Fk, Fk_, k)              # This is result of the generate_candidate()
                                               
        ## Prune candidate itemsets in Lk+1 containing subsets of length k that are infrequent
        prune_candidate(Ck_1, Fk, k)                     # It shows pruned candidates of Frequent (k+1)-itemsets
        Lk_1=prune_candidate(Ck_1, Fk, k)                # This is result of the prune_candidate()

        ## Count the support of each candidate in Lk+1 by scanning the DB
        count_support(database, Lk_1)                    # It count the number of itemsets in each transactions
        support_itemsets=count_support(database, Lk_1)   # This is result of the count_support()

        ## Eliminate candidates in Lk+1 that are infrequent, leaving only those that are frequent => Fk+1
        eliminate_candidate(support_itemsets, minsupp)   # Eliminate itemsets which are smaller than minimum support
        Fk=eliminate_candidate(support_itemsets, minsupp)   # new Fk for iteration

        if(len(Fk)>0):
            Fk_=Fk
            for i in Fk_:
                transactionNum.append(i)    # this calculate itemsets in output data. Here for Fk  

        k += 1
    
    ## Output F1, F2, ..., Fk to output_file

    output_freq_itemsets(output_file, transactionNum, itemNum)    # output file function


###  Implement main() function        
if __name__ == '__main__':         # make main()

    # Argument parser to read
    parser = Parser()
    args = parser.parse_args()
    databaseName = str(args.database_file)     # database file name
    minsup = float(args.minsupp)              # Minimum support value
    outputFile = str(args.output_file)        # output_file name

    database=read_database(databaseName)      # read database
    itemNum=database[1][0][1]    

    apriori(database, minsup, outputFile, itemNum)      # apply Apriori function to the database
