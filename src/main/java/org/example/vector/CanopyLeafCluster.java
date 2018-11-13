/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.example.vector;

import java.util.*;

/**
 */
public class CanopyLeafCluster <K> extends Cluster<K>
{


	private  Set<K> records1= new HashSet<>();
	private  Set<K> records2 = new HashSet<>();

	public CanopyLeafCluster(K centerPk, double[] vector) {
		super(centerPk, vector);
		records1.add(centerPk); 
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
	public List<Cluster<K>> getChildrenCluster()
	{
		return null;
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
	public void addChildCluster(CanopyLeafCluster<K> cluster) {
		throw new IllegalAccessError("Shouldn't be called");
	}
}
