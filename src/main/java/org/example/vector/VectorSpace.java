/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.example.vector;

import java.util.*;



/**
 */
public class VectorSpace<K,R,V>
{
	ClusterImpl<K> root;
	private final Map<K,double[]> data = new HashMap<>();
	private final Map<K,V> indexed =  new HashMap<>();
	private final Map<K,List<ClusterImpl<K>>> pkToCluster =  new HashMap<>();
	private final List<ClusterImpl<K>> clusters = new ArrayList<>();
	
	private double[] weights;
	private  double threshold1 = 3.6d;
	private  double threshold2 = 3.9d;
	private VectorFactory<R> vectorFactory;
	private IndexFactory<R, V> indexFactory;
	public VectorSpace(double[] weights,VectorFactory<R> vectorFactory,IndexFactory<R,V> indexFactory)
	{
		this.weights = weights;
		this.vectorFactory = vectorFactory;
		this.indexFactory = indexFactory;
	}
	public void add(K pk,R record) {
		double[] vector = this.vectorFactory.convertToVector(record);
		this.data.put(pk, vector);
		List<ClusterImpl<K>> clustersOfRecord = addToClusters(this.clusters,vector,pk);
		this.pkToCluster.put(pk, clustersOfRecord);
		this.indexed.put(pk, this.indexFactory.index(record));
	}

	private List<ClusterImpl<K>> getClosestCluster(List<ClusterImpl> clusters, double[] vector,K id){
		// TODO implement me
		List<ClusterImpl<K>> result = new LinkedList<>();
		Stack<ClusterImpl<K>> stack = new Stack<>();
		clusters.stream().forEach(c -> stack.push(c)); // lazy code
		boolean shouldCreateNewCluster = true;
		while(!stack.isEmpty()) {
			ClusterImpl<K> temp = stack.pop();
			if(temp.getChildrenCluster().isEmpty() ) {
				shouldCreateNewCluster = checkOneCluster(vector, id, result, temp)?true:shouldCreateNewCluster; 
			} else {
				for(ClusterImpl<K> c : temp.getChildrenCluster()) {
					
				}
			}
		}
		return result;
	}
	private boolean checkOneCluster(
		double[] vector,
		K id,
		List<ClusterImpl<K>> result,
		ClusterImpl<K> temp)
	{
		boolean shouldCreateNewCluster = true;
		double distance = distance(vector,temp.vector);
		if(distance < this.threshold1) {
			temp.addRecord(id,true);
			result.add(temp);
			shouldCreateNewCluster = false;
			//				break;
		} else if(distance < this.threshold2) {
			temp.addRecord(id,false);
			result.add(temp);
		}
		return shouldCreateNewCluster;
	}
	private List<ClusterImpl<K>> addToClusters(List<ClusterImpl<K>> clusters, double[] vector, K id)
	{
		List<ClusterImpl<K>> result = new ArrayList<>();
		// TODO implement me
		return result;
	}

	private double distance(double[] vector, double[] ds)
	{
		if(ds == null)
			return Double.MAX_VALUE;
		double result = 0;
		double d;
		for(int i = 0; i < vector.length; i++) {
			d = vector[i] - ds[i];
			result += weights[i]*d*d;
		}
		return Math.pow(result,0.5);
	}
	public double[] getVector(K pk) {return this.data.get(pk);}
	public V getIndexed(K pk) {return this.indexed.get(pk);}

	public Set<K> getSimilarRecords(K pk){
		Set<K> suspectPks = new HashSet<>();
		this.pkToCluster.get(pk).stream().map(c -> c.getPKs()).forEach(s -> suspectPks.addAll(s));		
		return suspectPks;
	}


	public int getClusterSize()
	{
		return this.clusters.size();
	}
	public void remove(String primaryKey)
	{
		List<ClusterImpl<K>> clusters = this.pkToCluster.remove(primaryKey);
		for(ClusterImpl<K> c : clusters) {
			c.remove(primaryKey);
		}

	}

}
