/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.example.vector;

import java.util.*;

/**
 */
public class CanopyCluster <T> extends Cluster<T>
{

	/**
	 */
	private  List<CanopyCluster<T>> childrenCluster = new ArrayList<>();
	private CanopyCluster<T> parent;
	
	public CanopyCluster(VectorSpace<?, ?, ?> vs,CanopyCluster<T> parent ,DataPoint<T> centerPk) {
		super(vs,centerPk);
		this.parent = parent;
	}
	public CanopyCluster<T> getParentCluster(){
		return this.parent;
	}
	
	public Set<DataPoint<T>> getDataPoints()
	{
		return Collections.EMPTY_SET;
	}
	public List<CanopyCluster<T>> getChildrenCluster()
	{
		return this.childrenCluster;
	}
	public DataPoint<T> getCenter() {
		return this.center;
	}
	private void splitCluster() {
		// TODO implement me
	}
	public boolean remove(T data)
	{
		throw new IllegalAccessError("Shouldn't be called");
	}
	public void addChildCluster(CanopyCluster<T> cluster) {
		// TODO implement me
	}
	public List<CanopyLeafCluster<T>>  addRecord(DataPoint<T> datapoint) {
		// TODO Auto-generated method stub
		return Collections.EMPTY_LIST;
	}
}
