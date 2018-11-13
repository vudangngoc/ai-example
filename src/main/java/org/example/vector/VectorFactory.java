/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.example.vector;

/**
 */
public abstract class VectorFactory<T>
{
	private final int SIZE_PER_FIELD = 30;
	
	public int getSizePerField()
	{
		return this.SIZE_PER_FIELD;
	}

	public  abstract double[] convertToVector(T data);
}
