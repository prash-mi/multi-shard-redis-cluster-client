package com.cisco.dcplite.util;

/*
 * Author Prashant Mishra
 * 
 * This class is singleton thread safe implementation for initializing pooled connection
 * 
 */

public class InMemoryConnectionUtil{
	private static volatile InMemoryDSConnection inMemoryDSConnection;
	private static volatile Object lock = new Object();

	private InMemoryConnectionUtil(){}

	public static InMemoryDSConnection getInstance(){//Thread safe singleton initialization
		if(inMemoryDSConnection == null){
			synchronized(lock){//thread safe initialization
				if(inMemoryDSConnection!= null){//checking again to make certain that only one instance would be spawned, as more than one threads might enter the previous null check 
					return inMemoryDSConnection;
				}
				inMemoryDSConnection = new InMemoryConnectionBroker();
			}
		}
		return inMemoryDSConnection;
	}


}
