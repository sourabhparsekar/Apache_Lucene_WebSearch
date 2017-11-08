package com.lucene.webSearch.lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.lucene.webSearch.manager.ApplicationManager;
import com.lucene.webSearch.manager.DBManager;
import com.lucene.webSearch.utility.ApplicationConstants;
import com.lucene.webSearch.utility.ApplicationException;
import com.lucene.webSearch.utility.FileHandling;
import com.lucene.webSearch.utility.PropertyLoader;



public class UpdateInitialIndex {
	private IndexWriter writer;
	private IndexReader reader;
    private IndexSearcher searcher;
	private TopDocs results;
	
	public UpdateInitialIndex() {

	}

	public void execute(File file) {

		long start = System.currentTimeMillis();
		System.out.println("Execution started at :: " + start);

		UpdateInitialIndex indexer = null;
		int numIndexed = 0;
		String indexDir = null;
		
//		String dataDir = null;

		try {

			PropertyLoader.initialisePropertyLoader();
			System.out.println("Property File initialised.");

			// INDEX DIRECTORY
			indexDir = ApplicationConstants.INDEX_OUTPUT_FILE_DIRECTORY;
			// "C:\\temp\\lucene\\index";

			// INPUT FILES DIRECTORY
			//dataDir = ApplicationConstants.INPUT_FILE_DIRECTORY;
			// "C:\\temp\\lucene\\file";

			if (ApplicationManager.isFirstIndexUpdateOnStartUpEnabled() || ApplicationManager.isFirstIndexEnabled()
					|| !FileHandling.isEmptyDirectory(indexDir)) {
				System.out.println("Initial Index is created after creating index directory.");

				if (ApplicationManager.isUpdateIndexEnabled()) {
					DBManager.AUDIT_INSERT(ApplicationConstants.UPDATE_INITIAL_INDEX,
							"Updating existing index directory for new file.",
							"Started at : " + System.currentTimeMillis());
					System.out.println("Updating existing index directory for new file.");

					indexer = new UpdateInitialIndex(indexDir);
					//File[] files = { new File(filePath) };
					numIndexed = indexer.updateIndex(file, new TextFilesFilter());
					DBManager.AUDIT_INSERT(ApplicationConstants.UPDATE_INITIAL_INDEX,
							"Updating existing index directory for new file.",
							"Ended at : " + System.currentTimeMillis());

				} else {
					System.out.println("Index directory update not enabled. Existing Index directory will be used. ");
					DBManager.AUDIT_INSERT(ApplicationConstants.UPDATE_INITIAL_INDEX,
							"Index directory update not enabled. Existing Index directory will be used.", "");
					return;
				}
			} else {
				System.out.println(
						"Neither Initial Index nor Updating Index directory enabled nor Index directory has Initial Index.");
				DBManager.AUDIT_INSERT(ApplicationConstants.UPDATE_INITIAL_INDEX,
						"Neither Initial Index nor Updating Index directory enabled nor Index directory has Initial Index.",
						"");
				return;
			}

		} catch (IOException ex) {
			DBManager.AUDIT_INSERT(ApplicationConstants.UPDATE_INITIAL_INDEX,
					"IO-Exception while updating Initial Index", "Error :: " + ex.getMessage());
			System.err.println("IO-Exception while updating Initial Index with error :: " + ex.getMessage());
		} catch (ApplicationException ex) {
			DBManager.AUDIT_INSERT(ApplicationConstants.UPDATE_INITIAL_INDEX,
					"Application Exception while updating Initial Index.", "Error :: " + ex.getMessage());
			System.err.println("Application Exception while updating Initial Index with error :: " + ex.getMessage());
		} catch (Exception ex) {
			DBManager.AUDIT_INSERT(ApplicationConstants.UPDATE_INITIAL_INDEX, "Exception while updating Initial Index.",
					"Error :: " + ex.getMessage());
			System.err.println("Exception while updating Initial Index with error :: " + ex.getMessage());
		} finally {
			try {
				if (indexer != null)
					indexer.close();
			} catch (IOException ex) {
				DBManager.AUDIT_INSERT(ApplicationConstants.UPDATE_INITIAL_INDEX,
						"IOException while closing Indexer with error", "Error :: " + ex.getMessage());
				System.err.println("IOException while closing Indexer with error :: " + ex.getMessage());
			} catch (Exception ex) {
				DBManager.ERROR_INSERT(ApplicationConstants.UPDATE_INITIAL_INDEX,
						"Exception while closing Indexer with error", "Error :: " + ex.getMessage());
				System.err.println("Exception while closing Indexer with error :: " + ex.getMessage());
			}
		}

		long end = System.currentTimeMillis();
		System.out.println("Execution ended at :: " + end);
		System.out.println("Indexing " + numIndexed + " files took " + (end - start) + " milliseconds");
		DBManager.AUDIT_INSERT(ApplicationConstants.UPDATE_INITIAL_INDEX,
				"Indexing " + numIndexed + " files took " + (end - start) + " milliseconds", "");
	}

	public UpdateInitialIndex(String indexDir) throws IOException {
		Directory dir = FSDirectory.open((new File(indexDir)).toPath());
		StandardAnalyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
		// LUCENE INDEX WRITER
		writer = new IndexWriter(dir, config);
		reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);
	}

	public void close() throws IOException {
		// CLOSE INDEX WRITER
		writer.close();
	}

	public int updateIndex(File file, FileFilter filter) throws Exception {
//		for (File f : files) {
			if (!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead()
					&& (filter == null || filter.accept(file))) {
				indexFile(file);
			}
//		}
		// RETURN NO OF DOCS INDEXED
		return writer.numDocs();
	}

	private Document getDocument(File f) throws Exception {
		Document doc = new Document();
		// INDEX FILE CONTENT
		doc.add(new TextField("contents", new FileReader(f)));
		// INDEX FILENAME
		doc.add(new StoredField("filename", f.getName()));
		// INDEX FILE FULL PATH
		doc.add(new StoredField("fullpath", f.getCanonicalPath()));
		return doc;
	}

	private void indexFile(File f) throws Exception {
		System.out.println("Indexing " + f.getCanonicalPath());
		Document doc = getDocument(f);
		// ADD/UPDATE DOCUMENT TO LUCENE INDEX
		results = searcher.search(new TermQuery(new Term("fullpath", f.getCanonicalPath())), 1);
		if (results.totalHits == 0){
		    writer.addDocument(doc);
		}
		else
		{
			writer.updateDocument(new Term("fullpath", f.getCanonicalPath()), doc);
			
		}
	}

	private static class TextFilesFilter implements FileFilter {
		public boolean accept(File path) {
			// INDEX ONLY .TXT USING FILTER
			return path.getName().toLowerCase().endsWith(".txt");
		}
	}

}
