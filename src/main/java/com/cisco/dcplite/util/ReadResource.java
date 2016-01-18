package com.cisco.dcplite.util;
/*
 * 
 *  @author Prashant Mishra
 * 
 */
import redis.clients.jedis.Jedis;
//DTO Class for holding slave bucket# and resource
public class ReadResource {
	private Jedis slaveResource;
	private int slaveBucket;

	public ReadResource(Jedis slaveResource, int slaveBucket){
		this.slaveResource = slaveResource;
		this.slaveBucket = slaveBucket;
	}


	/**
	 * @return the slaveResource
	 */
	public Jedis getSlaveResource() {
		return slaveResource;
	}

	/**
	 * @return the slaveBucket
	 */
	protected int getSlaveBucket() {
		return slaveBucket;
	}

}
