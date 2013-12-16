package com.datastax.ssl;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class ClusterConnect {
	
	
	public ClusterConnect(){
			
		Cluster cluster = Cluster.builder().addContactPoint("cassandra-secure1")
				.withSSL().build();
		
		Session session = cluster.connect();		
		System.out.println("Cluster and Session created with SSL");
		
		session.shutdown();
		cluster.shutdown();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ClusterConnect();
	}

}
