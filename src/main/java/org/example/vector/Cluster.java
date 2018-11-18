/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.example.vector;

/**
 */
public abstract class Cluster<T>
{
	DataPoint<T> center;
	private VectorSpace<?, ?, ?> vs;

	public Cluster(VectorSpace<?, ?, ?> vs ,DataPoint<T> center)
	{
		this.vs = vs;
		this.center = center;
	}
	public DataPoint<T> getCenter()
	{
		return this.center;
	}
	public void updateCenter(DataPoint<T> center) {
		this.center = center;
	}
	public VectorSpace<?, ?, ?> getVectorSpace() {
		return this.vs;
	}
	public abstract void cleanMemory();
}