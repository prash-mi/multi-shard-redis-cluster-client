package com.cisco.dcplite.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/*
 * @author Prashant Mishra
 * 
 * This class simply tests connection by setting and getting a key
 * 
 * 
 */

public class Sample {


	public static void main(String[] a){



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

		for(int i=0; i<10;i++){
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

		}
	}

}
