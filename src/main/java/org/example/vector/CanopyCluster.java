/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.example.vector;

import java.util.*;

/**
 */
public class CanopyCluster <K> extends Cluster<K>
{

	/**
	 */
	private  List<Cluster<K>> childrenCluster = new ArrayList<Cluster<K>>();
	
	public CanopyCluster(K centerPk, double[] vector) {
		super(centerPk, vector);
	}
	
	public void addRecord(K pk, boolean isTightThreshold)
	{
		throw new IllegalAccessError("Shouldn't be called");
	}
	/**
	 * expensive method
	 */
	
	public Set<K> getPKs()
	{
		return null;
	}
	public List<Cluster<K>> getChildrenCluster()
	{
		return this.childrenCluster;
	}
	public K getCenter() {
		return this.center;
	}
	
	public boolean remove(String pk)
	{
		throw new IllegalAccessError("Shouldn't be called");
	}
	public void addChildCluster(CanopyCluster<K> cluster) {
		// TODO implement me
	}
}
