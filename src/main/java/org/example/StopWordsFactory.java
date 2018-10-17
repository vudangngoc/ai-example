/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.example;

import java.io.*;
import java.util.*;

/**
 */
public class StopWordsFactory
{
	static Set<String> values;
	public static Set<String> getStopwords() {
		if(values == null) {
			final InputStream stream = StopWordsFactory.class.getClassLoader().getResourceAsStream("./stopwords.txt");
			values = new HashSet<>();
			try
			{
				final BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"ISO-8859-1"));
				String line;
				while ((line = reader.readLine()) != null) {
					values.add(line);
					values.add(line.toUpperCase());
				}
				reader.close(); 
			}
			catch (IOException ex)
			{
				throw new RuntimeException(ex);
			}
			values.add("RUE"); // special stop words for address
			values.add("rue");
			values.add("ROUTE");
			values.add("route");
		}
		
		return values;
	}
}
