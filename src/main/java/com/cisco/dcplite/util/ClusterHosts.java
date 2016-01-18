package com.cisco.dcplite.util;
/*
 *  @author Prashant Mishra
 */
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
//Its a DTO class for holding redis master and slave host addresses
public class ClusterHosts {

	private Set<HostAndPort> jedisSlaveNodes = new HashSet<HostAndPort>();
	private HostAndPort masterHostAndPort;
	/**
	 * @return the jedisSlaveNodes
	 */
	public Set<HostAndPort> getJedisSlaveNodes() {
		return jedisSlaveNodes;
	}
	/**
	 * @param jedisSlaveNodes the jedisSlaveNodes to set
	 */
	public void setJedisSlaveNodes(Set<HostAndPort> jedisSlaveNodes) {
		this.jedisSlaveNodes = jedisSlaveNodes;
	}
	/**
	 * @return the masterHostAndPort
	 */
	public HostAndPort getMasterHostAndPort() {
		return masterHostAndPort;
	}
	/**
	 * @param masterHostAndPort the masterHostAndPort to set
	 */
	public void setMasterHostAndPort(HostAndPort masterHostAndPort) {
		this.masterHostAndPort = masterHostAndPort;
	}


	public void addSlaveHostAndPort(HostAndPort slaveHostAndPort){
		this.jedisSlaveNodes.add(slaveHostAndPort);
	}

}
