/**
 * 
 */
package com.cisco.dcplite.util;

import redis.clients.jedis.Jedis;

/*
 * @author Prashant Mishra
 * 
 * Interface exposing the methods for the required operations
 * 
 */
public interface InMemoryDSConnection {
	
	public Jedis getWriteResource(String clusterId);
	public void returnWriteResource(String clusterId, Jedis resource);
	public ReadResource getReadResource(String clusterId);
	public void returnReadResource(String clusterId, ReadResource readResource);
	
}
