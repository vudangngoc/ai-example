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
	private  List<CanopyCluster<T>> childrenCluster = new ArrayList<>(MAX_CHILDREN+1);
	private static int MAX_CHILDREN = 8;
	private double[][] adjacencyMatrix = new double[MAX_CHILDREN+1][MAX_CHILDREN+1];
	private CanopyCluster<T> parent;
	private double threshold = 3.9d;
	private double threshold1 = 3.6d;

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
		int i =0;
		int j = 0;
		double temp = adjacencyMatrix[i][j];
		for(int row = 0; row < MAX_CHILDREN+1;row++)
			for( int col = row; col < MAX_CHILDREN +1;col++) {
				if(temp<adjacencyMatrix[row][col]) {
					temp = adjacencyMatrix[row][col];
					i = row;
					j= col;
				}
			}
		adjacencyMatrix[i][j] = 0;// Break edge
		List<CanopyCluster<T>> child1 = new ArrayList<>();
		List<CanopyCluster<T>> child2 = new ArrayList<>();
		double[][] newAdjacencyMatrix1 = new double[MAX_CHILDREN+1][];
		double[][] newAdjacencyMatrix2 = new double[MAX_CHILDREN+1][];
		addVerticesToList(child1,newAdjacencyMatrix1,i);
		addVerticesToList(child2,newAdjacencyMatrix2,j);
		// TODO assign new center
	}

	private void addVerticesToList(List<CanopyCluster<T>> childrenCluster,double[][] newAdjacencyMatrix, int i) {
		childrenCluster.add(this.childrenCluster.get(i));
		int newIndex = childrenCluster.size() - 1;
//		int addedCluster = 0;
		for(int index = 0; index < MAX_CHILDREN+1; index++) {
			if(index < i) {
				if(adjacencyMatrix[i][index] > 0) {
					newAdjacencyMatrix[newIndex][childrenCluster.size()] = adjacencyMatrix[i][index];					
					addVerticesToList(childrenCluster,newAdjacencyMatrix, index);
				}
			}else {
				if(adjacencyMatrix[index][i] > 0) {
					newAdjacencyMatrix[newIndex][childrenCluster.size()] = adjacencyMatrix[index][i];
					addVerticesToList(childrenCluster,newAdjacencyMatrix, index);
				}
			}
		}
	}

	public boolean remove(T data)
	{
		throw new IllegalAccessError("Shouldn't be called");
	}

	public void addChildCluster(CanopyCluster<T> cluster) {
		double[] vector = cluster.getCenter().vector;
		this.childrenCluster.add(cluster);
		int adjacencyIndex = 0;
		double minDistance = Double.MAX_VALUE;
		int i = 0;
		for(CanopyCluster<T> d : childrenCluster) {

			double tempDistance = this.getVectorSpace().euclideanDistance(d.center.vector, vector);
			if(minDistance > tempDistance) {
				minDistance = tempDistance;
				adjacencyIndex = i;
			}
			i++;
		}
		adjacencyMatrix[this.childrenCluster.size() - 1][adjacencyIndex] = minDistance;
		if(this.childrenCluster.size() > MAX_CHILDREN)
			splitCluster();
	}

	public List<CanopyLeafCluster<T>>  addRecord(DataPoint<T> datapoint) {
		List<CanopyLeafCluster<T>> result = new ArrayList<>();
		boolean shouldCreateNewCluster = true;
		for(CanopyCluster<T> c : childrenCluster) {
			double distance = this.getVectorSpace().euclideanDistance(datapoint.vector, c.center.vector);
			if(distance < this.threshold ) {
				result.addAll(c.addRecord(datapoint));
			}
			if(distance < this.threshold1 &&!c.getDataPoints().isEmpty()) {
				shouldCreateNewCluster = false;
			}
		}
		if(shouldCreateNewCluster) {
			this.addChildCluster(new CanopyLeafCluster<>(getVectorSpace(), this, datapoint));
		}
		return result;
	}

	@Override
	public void cleanMemory() {
		this.adjacencyMatrix = null;
		for(CanopyCluster<T> c : this.getChildrenCluster()) {
			c.cleanMemory();
		}
	}
}
