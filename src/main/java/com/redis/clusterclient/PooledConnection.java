package com.redis.clusterclient.util;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/*
 * @author Prashant Mishra
 * This class maintains pooled connection with respective hosts
 * 
 */
public class PooledConnection{

	//	private  JedisConnectionPool conFactInstance;
	private  JedisPool jPool;
	private String host;

	protected PooledConnection(String host, int port){//this isnt supposed to be initialized outside of the package

		try {
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			/* Some extra configuration */


			//this will raise JedisConnectionException if the pool is exhausted 
			poolConfig.setBlockWhenExhausted(InMemoryUtilConstants.POOL_SET_BLOCK_WHEN_EXHAUSTED);

			//Max connections per redis node
			poolConfig.setMaxTotal(InMemoryUtilConstants.POOL_SET_MAX_TOTAL);

			// Tests whether connection is dead when connection
			// retrieval method is called
			poolConfig.setTestOnBorrow(InMemoryUtilConstants.POOL_SET_TEST_ON_BORROW);


			// Tests whether connection is dead when returning a
			// connection to the pool
			poolConfig.setTestOnReturn(InMemoryUtilConstants.POOL_SET_TEST_ON_RETURN);

			// Number of connections to Redis that just sit there
			// and do nothing
			poolConfig.setMaxIdle(InMemoryUtilConstants.POOL_SET_MAX_IDLE);
			// Minimum number of idle connections to Redis
			// These can be seen as always open and ready to serve
			poolConfig.setMinIdle(InMemoryUtilConstants.POOL_SET_MIN_IDLE);

			// Tests whether connections are dead during idle periods
			poolConfig.setTestWhileIdle(InMemoryUtilConstants.POOL_SET_TEST_WHILE_IDLE);

			// Maximum number of connections to test in each idle check
			poolConfig.setNumTestsPerEvictionRun(InMemoryUtilConstants.POOL_SET_NUM_TESTS_PER_EVICTION_RUN);

			// Idle connection checking period
			poolConfig.setTimeBetweenEvictionRunsMillis(InMemoryUtilConstants.POOL_SET_TIME_BETWEEN_EVICTION_RUNS_MILLIS);

			// Create the jedisPool
			if(InMemoryUtilConstants.REDIS_PASSOWORD != null && !InMemoryUtilConstants.REDIS_PASSOWORD.trim().equals("")){
				jPool = new JedisPool(poolConfig, host, port, InMemoryUtilConstants.REDIS_CONNECTION_TIMEOUT, InMemoryUtilConstants.REDIS_PASSOWORD);
			}else{
				jPool = new JedisPool(poolConfig, host, port, InMemoryUtilConstants.REDIS_CONNECTION_TIMEOUT);
			}

			this.host = host;

			System.out.println("PooledConnection Initialized for host: "+this.host);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	protected Jedis getJedis(){//returns a resource
		return this.jPool.getResource();
	}

	protected void returnJedis(Jedis resource){
		this.jPool.returnResource(resource);
	}



}
