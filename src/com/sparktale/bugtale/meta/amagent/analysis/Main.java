package com.sparktale.bugtale.meta.amagent.analysis;

import java.io.IOException;

import com.sparktale.bugtale.meta.amagent.analysis.analyzer.Analyzer;
import com.sparktale.bugtale.meta.amagent.analysis.analyzer.Histogram;
import com.sparktale.bugtale.meta.amagent.analysis.analyzer.TimespanAggregator;
import com.sparktale.bugtale.meta.amagent.analysis.analyzer.TimestampFilter;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		if (args.length == 0)
		{
			System.out.println("Please provide a file name to parse.");
			return;
		}
		
		String inputFileName = args[0];
		
		System.out.println("Parsing " + inputFileName + " ...");
		
		Parser parser = new Parser(inputFileName);
		
		Analyzer histogram = new Histogram();
		Analyzer aggregator = new TimespanAggregator(1000, histogram);
		Analyzer filter = new TimestampFilter(1409367660000l, 1409367840000l, aggregator);
		
		parser.parse(filter);
		
		System.out.println("Aggregation:");
		System.out.println(aggregator);
		
		System.out.println("Histogram:");
		System.out.println(histogram);
	}
}
