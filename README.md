## What is single-shard-redis-cluster-client?
single-shard-redis-cluster-client is a Redis wrapper Java API on top of Jedis 2.6  This API connects to many clusters in parallel, where each cluster 
has a single master and multiple slaves running in ACTIVE mode. This API routes write requests to the masters and read requests to the slaves, thus 
distributing the load between master and slaves

##What is the need?
In traditional redis the keys are distributed between multiple masters, but due to this distribution aggregation operations like union and intersection can't work. 
So for the requirements where we need to perform set operations, we can break data in many clusters (shards). Each cluster will have a single master and multiple 
slaves, all running in active mode. This util will route read request to slaves and writes to master, thus maximizing resource utilization and enabling set operations. 
Connection to many such clusters can be maintained by this API.

## How to Use It?
1> Start Redis nodes in cluster mode.

2> Make slaves active.

3> Enter the Redis Hosts and ports details in srcc_en_US.properties which is found at src/main/resources, group different masters under different cluster names.

4> Simply consume SRCC using a Java code like:

```java
	InMemoryDSConnection inMemCon = InMemoryConnectionUtil.getInstance();

		//Sample write operation
		Jedis writeRes = inMemCon.getWriteResource("content-listing-cluster");//borrow a connection from a clusterId
		try {
			Transaction writeTransaction  = 	writeRes.multi();

			writeTransaction.set("TestKey1", "TestValue");
			writeTransaction.set("TestKey2", "TestValue");
			//put all the operations for the row in a single transactions

			writeTransaction.exec();
			System.out.println("Set TestKey1");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			inMemCon.returnWriteResource("content-listing-cluster", writeRes);//return the connection back to the pool
		}

		//Sample read operation
		ReadResource readRes = inMemCon.getReadResource("content-listing-cluster");//borrow a connection from a clusterId
		try {
			String value = readRes.getSlaveResource().get("TestKey1");
			System.out.println("Read value :"+value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			inMemCon.returnReadResource("content-listing-cluster", readRes);//return the connection back to the pool
		}
```

## License
Open source under Apache License Version 2.0
