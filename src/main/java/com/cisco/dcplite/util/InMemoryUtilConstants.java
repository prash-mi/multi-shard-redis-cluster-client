package com.cisco.dcplite.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import redis.clients.jedis.HostAndPort;

/*
 * @author Prashant Mishra
 * 
 * Constants file
 * 
 */

public class InMemoryUtilConstants {




		private InMemoryUtilConstants() {
			//Utility classes should not have a default constructor
		}

	/*Redis Keys and constants start*/
	public static Map<String, ClusterHosts> clusterHosts = new HashMap<String, ClusterHosts>();

	public static final String REDIS_PASSOWORD ;
	public static final int REDIS_CONNECTION_TIMEOUT;
	public static final int POOL_SET_MAX_TOTAL;
	public static final int POOL_SET_MAX_IDLE;
	public static final int POOL_SET_MIN_IDLE;
	public static final int POOL_SET_NUM_TESTS_PER_EVICTION_RUN;
	public static final int POOL_SET_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
	public static final boolean POOL_SET_BLOCK_WHEN_EXHAUSTED;
	public static final boolean POOL_SET_TEST_ON_BORROW;
	public static final boolean POOL_SET_TEST_ON_RETURN;
	public static final boolean POOL_SET_TEST_WHILE_IDLE;
	private static final String PROPERTIES_FILE_LOCAL = "src/main/resources/srcc_en_US.properties";
														//change is to relative or absolute path as required


	/*
	 *Static initialization block
	 * 
	 */


	static{//loads properties from srcc properties file

		ArrayList<String> propertyKeys = new ArrayList<String>();

		ResourceBundle bundle = null;
		try {
			FileInputStream fis = new FileInputStream(PROPERTIES_FILE_LOCAL);
			bundle = new PropertyResourceBundle(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_LOCAL);//, Locale.ENGLISH);

		/*Property Loading for Inmemdeb start*/	
		Enumeration<String> keys =  bundle.getKeys();
		while(keys.hasMoreElements()){//load hosts and ports from property file
			String key = keys.nextElement();
			propertyKeys.add(key);
			if(key.startsWith("cluster-id")){
				clusterHosts.put(bundle.getString(key), new ClusterHosts());
			}

		}

		//MASTER_HOST_AND_PORT = bundle.getString("master-host-and-port");
		REDIS_PASSOWORD = bundle.getString("redis-password");
		REDIS_CONNECTION_TIMEOUT = new Integer(bundle.getString("redis-connection-timeout")).intValue();//int
		POOL_SET_BLOCK_WHEN_EXHAUSTED = new Boolean(bundle.getString("pool-set-block-when-exhausted"));//bool
		POOL_SET_MAX_TOTAL = new Integer(bundle.getString("pool-set-max-total")).intValue();//int
		POOL_SET_TEST_ON_BORROW = new Boolean(bundle.getString("pool-set-test-on-borrow"));//bool
		POOL_SET_TEST_ON_RETURN = new Boolean(bundle.getString("pool-set-test-on-return"));//bool
		POOL_SET_MAX_IDLE = new Integer(bundle.getString("pool-set-max-idle")).intValue();//int
		POOL_SET_MIN_IDLE = new Integer(bundle.getString("pool-set-min-idle")).intValue();//int
		POOL_SET_TEST_WHILE_IDLE = new Boolean(bundle.getString("pool-set-test-while-idle"));//bool
		POOL_SET_NUM_TESTS_PER_EVICTION_RUN = new Integer(bundle.getString("pool-set-num-tests-per-eviction-run")).intValue();//int
		POOL_SET_TIME_BETWEEN_EVICTION_RUNS_MILLIS = new Integer(bundle.getString("pool-set-time-between-eviction-runs-millis")).intValue();//int

		for(String clusterPrefix: clusterHosts.keySet()){//logic for loading master and slaves

			for(String pKey: propertyKeys){
				if(pKey!= null && pKey.toLowerCase().startsWith(clusterPrefix+"-slave-host-and-port")){
					String value = bundle.getString(pKey);
					String[] hostPort = value.split(":");
					if (hostPort.length == 2){
						HostAndPort slaveHost = new HostAndPort(hostPort[0],new Integer(hostPort[1]));
						clusterHosts.get(clusterPrefix).addSlaveHostAndPort(slaveHost);
						System.out.println("Read Slave Nodes: "+value);
					}
				}else if(pKey!= null && pKey.toLowerCase().equals(clusterPrefix+"-master-host-and-port")){
					String value = bundle.getString(pKey);
					String[] hostPort = value.split(":");
					if (hostPort.length == 2){
						HostAndPort masterHost = new HostAndPort(hostPort[0],new Integer(hostPort[1]));
						clusterHosts.get(clusterPrefix).setMasterHostAndPort(masterHost);
						System.out.println("Read Master Nodes: "+value);
					}
				}
			}	

		}

		/*Property Loading for Inmemdeb end*/	

	}
}
