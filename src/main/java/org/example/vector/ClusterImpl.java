/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.example.vector;

import java.util.*;

/**
 */
public class ClusterImpl <K>
{
	public K center;
	/**
	 */
	private final List<ClusterImpl<K>> childrenCluster = new ArrayList<>();
	

	private final Set<K> records1 = new HashSet<>();
	private final Set<K> records2 = new HashSet<>();
	final double[] vector;

	public double[] getVector()
	{
		return this.vector;
	}
	public ClusterImpl(K centerPk, double[] vector) {
		this.center = centerPk;
		records1.add(centerPk); //TODO maybe duplicate work
		this.vector = vector;
	}
	
	public void addRecord(K pk, boolean isTightThreshold)
	{
		if(isTightThreshold)
			records1.add(pk);
		else
			records2.add(pk);

	}
	/**
	 * expensive method
	 */
	
	public Set<K> getPKs()
	{
		Set<K> result = new HashSet<>(); // TODO not good performance
		result.addAll(records1);
		result.addAll(records2);
		return result;
	}
	public List<ClusterImpl<K>> getChildrenCluster()
	{
		return this.childrenCluster;
	}
	public K getCenter() {
		return this.center;
	}
	
	public boolean remove(String pk)
	{
		if(center.equals(pk)) {
			records1.remove(pk);
			if(records1.size() > 1)
				center = records1.iterator().next();
			else 
				center = null;
		} else {
			records1.remove(pk);
			records2.remove(pk);
		}
		return false;
	}
	public void addChildCluster(ClusterImpl<K> cluster) {}
}
