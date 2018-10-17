/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.example;

import java.io.*;
import java.util.*;

import org.apache.spark.ml.clustering.*;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;

/**
 */
public class FeatureHashing_Kmeans
{
	public static void main(String...strings) {
		SparkSession spark = SparkSession
			.builder()
			.appName("FeatureHasher")
			.config("spark.master", "local") // TODO for POC only
			.config("spark.network.timeout", 1000000)
			.config("spark.executor.heartbeatInterval", 100000)
			.getOrCreate();
		StructType	schema = new StructType( new StructField[]{
				new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
				new StructField("siren", DataTypes.IntegerType, false, Metadata.empty()),
				new StructField("name", DataTypes.StringType, false, Metadata.empty()),
				new StructField("unknow", DataTypes.StringType, false, Metadata.empty()),
				new StructField("address", DataTypes.StringType, false, Metadata.empty())
		});
		List<Row> data = importData();
		Dataset<Row> predictions = kmean(spark, schema, data);

		predictions.show(false);
		spark.stop();
	}
	private static List<Row> importData()
	{
		List<Row> result = new ArrayList<>(10000);
		File file = new File("./data.csv");
		try
		{
			final BufferedReader reader = new BufferedReader(
			    new InputStreamReader(new FileInputStream(file),"ISO-8859-1"));
			String line;
			long count = 0;
			while ((line = reader.readLine()) != null) {
				final String[] record = line.split(",");
				result.add(RowFactory.create(Integer.parseInt(record[0]),
					Integer.parseInt(record[1].replaceAll("\"", "")),
					record[2].replaceAll("\"", ""),
					record[3].replaceAll("\"", ""),
					record[4].replaceAll("\"", "")));
				count ++;
				if(count % 100000 == 0) {
					System.out.println(count);
				}
			}
			reader.close(); //TODO not good
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return result;
	}
	private static Dataset<Row> kmean(SparkSession spark,StructType schema,List<Row> data)
	{
		Dataset<Row> featurized = vectorize(spark, schema, data);
		//	featurized.show(100, false);

		KMeans kmeans = new KMeans().setK(data.size()/20).setSeed(1L);
		KMeansModel model = kmeans.fit(featurized);

		Dataset<Row> predictions = model.transform(featurized);
		return predictions;
	}
	private static Dataset<Row> vectorize(SparkSession spark,StructType schema,List<Row> data)
	{
		Dataset<Row> dataset = spark.createDataFrame(data, schema);
		FeatureHasher hasher = new FeatureHasher()
			.setInputCols(new String[]{"name", "unknow", "address"})
			.setCategoricalCols(new String[]{"siren"})
			.setNumFeatures(500)
			.setOutputCol("features");

		Dataset<Row> featurized = hasher.transform(dataset);
		return featurized;
	}
}
