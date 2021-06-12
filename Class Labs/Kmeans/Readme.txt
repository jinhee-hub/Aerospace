# Lab Description
Implemented Kmeans Algorithm for Linux Environment with scratch
This Lab is one of my first lab as a student of the Computer Science and Engineering major 

# use this input1 (You can change K=30 n=20 e=0.1 values)
python3 kmeans.py -database_file=data1.txt -k=30 -max_iters=20 -eps=0.1 -output_file=output.txt
-database_file: input data
-k the number of clusters
-max_iters: maximum iteration
-esp: minimum distance for the old and new centroids

Get data1output.txt: It takes 9 iterations
Number of datapoints in each clusters 
[[56, 71, 45, 47, 13, 100, 27, 34, 42, 50, 23, 25, 8, 44, 24, 53, 31, 32, 24, 30, 23, 18, 37, 55, 25, 52, 33, 29, 23, 26]]

What I used...
# initCentroids( )
 I randomly choose k number of datapoints in database for k initial centroids
 I used list to make centroids array
 return type is list : There are k centroids

# EuclideanDistance( )
 I used list to extrach each datapoints and its dimensional data(attributes) to calculate Euclidean distance
 return type is list : There are k centroids

# AssignCluster( )
 I used list to find minimum distance along the Euclidean distances
 Datapoint which has minimum distance is the closest datapoint
 return type is list : There are k clusters

# MakeNewCentroids( )
 I used list to update new centroids
 Classify each datapoints in k clusters, and using clustering information -> assign new centroids
 return type is list : There are k centroids

# NewOldDifference( )
 I used list to compare find distance between old and new centroids
 Comparing difference of old and new centroids to epsilon to find end-iteration point
 return type is list : to show all k clusters


