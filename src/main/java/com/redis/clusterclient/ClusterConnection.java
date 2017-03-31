package com.redis.clusterclient.util;
/*
 * 
 *  @author Prashant Mishra
 * 
 * 
 */
import java.util.ArrayList;

public class ClusterConnection {

	private PooledConnection masterPool;
	private ArrayList<PooledConnection> slavePool = new ArrayList<PooledConnection>();
	/**
	 * @return the masterPool
	 */
	public PooledConnection getMasterPool() {
		return masterPool;
	}
	/**
	 * @param masterPool the masterPool to set
	 */
	public void setMasterPool(PooledConnection masterPool) {
		this.masterPool = masterPool;
	}
	/**
	 * @return the slavePool
	 */
	public ArrayList<PooledConnection> getSlavePool() {
		return slavePool;
	}
	/**
	 * @param slavePool the slavePool to set
	 */
	public void addSlavePool(PooledConnection slavePool) {
		this.slavePool.add(slavePool);
	}
	
	
	
}
