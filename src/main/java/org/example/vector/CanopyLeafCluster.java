/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.example.vector;

import java.util.*;

/**
 */
public  class  CanopyLeafCluster <T> extends CanopyCluster<T>
{

	private static int MAX_RECORD = 8;
	private double[][] adjacencyMatrix = new double[MAX_RECORD+1][MAX_RECORD+1];
	private  Set<DataPoint<T>> records= new HashSet<>();
	public CanopyLeafCluster(VectorSpace<?, ?, ?> vs,CanopyCluster<T> parent ,DataPoint<T> centerPk) {
		super(vs,parent,centerPk);
		records.add(centerPk); 
		
	}
	public List<CanopyLeafCluster<T>>  addRecord(DataPoint<T> datapoint) {
		CanopyLeafCluster<T> cluster = this.addRecordPrivate(datapoint);
		return Arrays.asList(new CanopyLeafCluster[] {cluster});
	}
	protected CanopyLeafCluster<T> addRecordPrivate(DataPoint<T> pk)
	{
		if(records.contains(pk))
			return this;
		records.add(pk);
		int adjacencyIndex = 0;
		double minDistance = Double.MAX_VALUE;
		int i = 0;
		for(DataPoint<T> d : records) {
			
			double tempDistance = this.getVectorSpace().distance(d.vector, pk.vector);
			if(minDistance > tempDistance) {
				minDistance = tempDistance;
				adjacencyIndex = i;
			}
			i++;
		}
		adjacencyMatrix[this.records.size() - 1][adjacencyIndex] = minDistance;
		if(this.records.size() > MAX_RECORD)
			return splitCluster();
		return this;
	}

	private CanopyLeafCluster<T> splitCluster() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<DataPoint<T>> getDataPoints()
	{
		return records;
	}
	
	public List<CanopyCluster<T>> getChildrenCluster()
	{
		return Collections.EMPTY_LIST;
	}
	public boolean remove(T data)
	{
		// TODO implement me
		return false;
	}
	public void addChildCluster(CanopyCluster<T> cluster) {
		throw new IllegalAccessError("Shouldn't be called");
	}
	/**
	 * Call after finish clustering
	 */
	public void cleanMemory() {
		this.adjacencyMatrix = null;
	}
}
