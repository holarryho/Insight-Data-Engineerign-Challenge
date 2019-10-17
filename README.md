# Border Crossing Analysis

## Table of Contents
1. [Problem](README.md#problem)
1. [Solution](README.md#solution)
1. [How to scale](README.md#Scalability)
1. [Workflow](README.md#WorkFlow)

## Problem
The Bureau of Transportation Statistics regularly makes available data on the number of vehicles, equipment, passengers and pedestrians crossing into the United States by land.

**For this challenge, we want to you to calculate the total number of times vehicles, equipment, passengers and pedestrians cross the U.S.-Canadian and U.S.-Mexican borders each month. We also want to know the running monthly average of total number of crossings for that type of crossing and border.**

## Solution
To solve this problem I'm taking following approach 

Classes: 
* BorderCrossingAnalysis - This is the main class which has 2 routines 
    1. readDataFromFile() -> Read Data from input file and load it into in-memory Cache dataCacheMap.which is <K,V> hashmap.
    
        dataCacheMap - Hashmap<K,V>
         * K - Key combination of < Border+Date+Measure>
         * V - Data Cache consists of all required information to generate report
            
            By generating this hashmap we can compute Average value for each key, which is required for report generation.
    2. generateAndLoadCache() -> Generate Report and Load in in-memory dataCacheMap and generate Average value for final report
* DataCache 
    * DataCache is in-memory cache which will hold data read from file, process it , update it
    and make newly generated data ready to write to report file
    
    * Iterate through dataCacheMap hashmap and get Data Cache for each key.
        for each key get total crossings and compute total crossings for previous months.
        and generaet average for all previous months using total/(crossings for previous months).
        Write that average to dataCacheMap. 
                
* WriteReport
    * DataCacheMap contains all the required information to generate report for each key. 
    * Load data from  dataCacheMap and sort is as per requirements.
    * Read data from dataCacheMap and create csv row to write to report file in following format and write it to report file.
            
        * Border, Date, Measure, Value, Average

## Scalability 

By design this solution can be scaled if required. To acheive this, I have implemented cache based solution
which is loosely coupled to the data on disk and business logic (generating report),hence allows program to scale.
Since cache is loosely coupled with other aspects of solutions. We can replace cache implementation with distributed
cache. And using <key,value> record allows us to scale easily.  
Use of caching can solve basics problem to make scale this system,
1. Hot spotting of servers
2. Parallel requests

In conjunction with cache, We can implement measures such as sharding to minimize server hotspotting.
And also implement replication for continues availability and avoid single point of failure.  


* Data Structure choice for Cache

    Data structure choice to implement in-memory Cache is HashMap over List. 
    HashMap offers constant time complexity for basic operations, get and put if the hash function is properly written.
        

## WorkFlow


1. divide single program into micro-services. and run them on different servers.  
2. Each server will process input files and then generate <K,V> data set and load it in the cache. 
3. Once cache is generated with expected output, this data will be dumped into distributed cache or messaging queue.
4. Report generator program will consume this data from queue to generate final report (csv file).
5. Report can be stored in persistant storage and/or load into UI to display it to user.


