package com.lucene.webSearch.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.lucene.webSearch.manager.DBManager;
import com.lucene.webSearch.utility.ApplicationConstants;
import com.lucene.webSearch.utility.ApplicationException;
import com.lucene.webSearch.utility.PropertyLoader;


public class Searcher {
	public static void main(String[] args)
			throws IllegalArgumentException, IOException, ParseException, ApplicationException {

		// String indexDir = "C:\\temp\\lucene\\index"; //PARSE INDEX DIRECTORY
		String q = "president"; // PARSE SEARCH QUERY
		search(q);
	}

	public static List<String> search(String q) throws ApplicationException {
		PropertyLoader.initialisePropertyLoader();
		System.out.println("Property File initialised.");
		List<String> outputResults = new ArrayList<String>();

		// INDEX DIRECTORY
		String indexDir = ApplicationConstants.INDEX_OUTPUT_FILE_DIRECTORY;
		Directory dir;
		IndexReader reader = null;
		Query query;
		IndexSearcher is = null;
		QueryParser parser = null;
		TopDocs hits = null;
		long start = System.currentTimeMillis();
		try {
			dir = FSDirectory.open((new File(indexDir)).toPath());
			reader = DirectoryReader.open(dir);
			is = new IndexSearcher(reader); // OPEN INDEX
			parser = new QueryParser("contents", new StandardAnalyzer());
			query = parser.parse(q);
			hits = is.search(query, 10);// SEARCH INDEX

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // PARSE QUERY
		long end = System.currentTimeMillis();

		System.err.println("Found " + hits.totalHits + " document(s) (in " + (end - start)
				+ " milliseconds) that matched query '" + q + "':");
		// WRITE SEARCH STATS
		Document doc;

		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			try {
				doc = is.doc(scoreDoc.doc);
				// RETRIEVE MATCHING DOCS
				System.out.println(doc.get("fullpath")); // DISPLAY FILENAME
				outputResults.add(doc.get("fullpath"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			reader.close();
			if(hits.totalHits > 0)
				DBManager.TAG_INSERT_UPDATE(q);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // CLOSE READER
		return outputResults;
	}
}
