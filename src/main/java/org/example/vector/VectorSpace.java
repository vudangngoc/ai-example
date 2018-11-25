/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.example.vector;

import java.util.*;
import java.util.stream.Collectors;



/**
 */
public class VectorSpace<K,R,V>
{
	CanopyCluster<V> root;
	private final Map<K,List<CanopyLeafCluster<V>>> pkToCluster =  new HashMap<>();
	private final List<CanopyCluster<V>> clusters = new ArrayList<>();

	private double[] weights;
	private VectorFactory<R> vectorFactory;
	private IndexFactory<R, V> indexFactory;
	public VectorSpace(double[] weights,VectorFactory<R> vectorFactory,IndexFactory<R,V> indexFactory)
	{
		this.weights = weights;
		this.vectorFactory = vectorFactory;
		this.indexFactory = indexFactory;

	}
	public void add(K pk,R record) {
		V index = indexFactory.index(record);
		double[] vector = vectorFactory.convertToVector(record);
		DataPoint<V> datapoint = new DataPoint<V>(index, vector);
		if(root == null) {			
			root = new CanopyCluster<>(this, null, datapoint);
		}		
		pkToCluster.put(pk, root.addRecord(datapoint));		
	}


	public double euclideanDistance(double[] vector, double[] ds)
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
	public double cheapDistance(double[] vector, double[] ds) {
		double result = 0d;
		for(int i = 0; i < vector.length; i++)
			result += weights[i]*(vector[i] > ds[i]?vector[i] - ds[i]:ds[i]-vector[i]);
		return result;
	}
	public Set<V> getSimilarRecords(K pk){
		// TODO optimize me
		Set<DataPoint<V>> suspectPks = new HashSet<>();
		this.pkToCluster.get(pk).stream().map(c -> c.getDataPoints()).forEach(s -> suspectPks.addAll(s));		
		return suspectPks.stream().map(p -> p.data).collect(Collectors.toSet());
	}


	public int getClusterSize()
	{
		return this.clusters.size();
	}
	public void remove(K key)
	{
		// TODO implement me

	}
	public void afterIndexing() {
		this.root.cleanMemory();
	}
}
