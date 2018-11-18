package org.example.vector;

public class DataPoint<T> {
	T data;
	double[] vector;
	int hashCode;
	public DataPoint(T data, double[] vector) {
		super();
		this.data = data;
		this.vector = vector;
		this.hashCode = computeHashCode();
	}
	public T getData() {
		return data;
	}
	public double[] getVector() {
		return vector;
	}
	
	public int computeHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}
	@Override
	public int hashCode() {
		return this.hashCode;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataPoint other = (DataPoint) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

}
