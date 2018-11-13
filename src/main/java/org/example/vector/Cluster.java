/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.example.vector;

/**
 */
public class Cluster<K>
{
	double[] centerVector;
	K center;
	public Cluster(K center, double[] centerVector)
	{
		this.center = center;
		this.centerVector = centerVector;
	}

	public double[] getCenterVector()
	{
		return this.centerVector;
	}

	public K getCenter()
	{
		return this.center;
	}
	public void updateCenter(K center, double[] centerVector) {
		this.center = center;
		this.centerVector = centerVector;
	}
}