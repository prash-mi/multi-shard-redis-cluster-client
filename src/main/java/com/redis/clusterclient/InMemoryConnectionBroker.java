package com.redis.clusterclient.util;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

/*
 * @author Prashant Mishra
 * 
 * This class spawns pooled connection to each redis host
 * and implements the required methods for borrowing and returning
 * connections from redis nodes (masters and slaves)
 * 
 */

public class InMemoryConnectionBroker implements InMemoryDSConnection{

	private Map<String, ClusterConnection> multiNodeConnection = new HashMap<String, ClusterConnection>();
	private Map<String, Integer> roundRobinCounter = new HashMap<String, Integer>();

	public InMemoryConnectionBroker(){//Initialize this class while initializing pooled connection with all the hosts

		for(String clusterId: InMemoryUtilConstants.clusterHosts.keySet()){
			if(!multiNodeConnection.containsKey(clusterId)){
				ClusterConnection clusterCon  = new ClusterConnection();
				HostAndPort masterHP = InMemoryUtilConstants.clusterHosts.get(clusterId).getMasterHostAndPort();
				PooledConnection masterPool = new PooledConnection(masterHP.getHost(),
						masterHP.getPort());
				clusterCon.setMasterPool(masterPool);
				multiNodeConnection.put(clusterId, clusterCon);
				roundRobinCounter.put(clusterId, 0);//initializing the round robin counter
				System.out.println("Added MasterPool for clusterId: "+clusterId);
			}

			for(HostAndPort slave: InMemoryUtilConstants.clusterHosts.get(clusterId).getJedisSlaveNodes()){
				PooledConnection slavePool = new PooledConnection(slave.getHost(), slave.getPort());
				multiNodeConnection.get(clusterId).addSlavePool(slavePool);
				System.out.println("Added SlavePool for clusterId: "+clusterId);

			}
		}
	}

	//Gets a redis resource to redis master
	@Override
	public Jedis getWriteResource(String clusterId) {
		// TODO Auto-generated method stub
		return multiNodeConnection.get(clusterId).getMasterPool().getJedis();

	}

	//return redis master resource
	@Override
	public void returnWriteResource(String clusterId, Jedis resource) {
		// TODO Auto-generated method stub
		multiNodeConnection.get(clusterId).getMasterPool().returnJedis(resource);
	}

	//gets redis resource from a slave
	@Override
	public ReadResource getReadResource(String clusterId) {
		int bucket = getRoundRobinBucket(clusterId);
		PooledConnection slavePool = multiNodeConnection.get(clusterId).getSlavePool().get(bucket);
		return new ReadResource(slavePool.getJedis(), bucket);

	}

	//returns redis slave resource to the slave pooler
	@Override
	public void returnReadResource(String clusterId, ReadResource readresource) {
		multiNodeConnection.get(clusterId).getSlavePool().get(readresource.getSlaveBucket()).
		returnJedis(readresource.getSlaveResource());

	}



	//returns a number >= 0 & number <= (number of slaves -1) in a round robin fashion 
	//so that requests can be distributed among the slaves evenly under each cluster

	private	int getRoundRobinBucket(String clusterId){

		int bucket = roundRobinCounter.get(clusterId);
		if(bucket == multiNodeConnection.get(clusterId).getSlavePool().size()-1){
			roundRobinCounter.put(clusterId, 0);//set the counter to zero
		}else{
			roundRobinCounter.put(clusterId, bucket+1);//increment the counter
		}

		return bucket;
	}


}
