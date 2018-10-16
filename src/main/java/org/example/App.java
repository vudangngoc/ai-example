package org.example;

import java.util.*;

import org.apache.spark.ml.feature.*;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args )
	{
		SparkSession	spark = SparkSession
			.builder()
			.appName("FeatureHasher")
			.config("spark.master", "local") // TODO for POC only
			.getOrCreate();
		List<Row> data = Arrays.asList(
			RowFactory.create(Arrays.asList("Hi I heard about Spark".split(" "))),
			RowFactory.create(Arrays.asList("I wish Java could use case classes".split(" "))),
			RowFactory.create(Arrays.asList("Logistic regression models are neat".split(" ")))
			);
		StructType schema = new StructType(new StructField[]{
				new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
		});
		Dataset<Row> documentDF = spark.createDataFrame(data, schema);

		// Learn a mapping from words to Vectors.
		Word2Vec word2Vec = new Word2Vec()
			.setInputCol("text")
			.setOutputCol("result")
			.setVectorSize(3)
			.setMinCount(0);

		Word2VecModel model = word2Vec.fit(documentDF);
		Dataset<Row> result = model.transform(documentDF);

		for (Row row : result.collectAsList()) {
			List<String> text = row.getList(0);
			Vector vector = (Vector) row.get(1);
			System.out.println("Text: " + text + " => \nVector: " + vector + "\n");
		}
		spark.stop();
	}
}
