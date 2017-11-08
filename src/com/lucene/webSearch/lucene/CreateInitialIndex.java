package com.lucene.webSearch.lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.lucene.webSearch.manager.ApplicationManager;
import com.lucene.webSearch.manager.DBManager;
import com.lucene.webSearch.utility.ApplicationConstants;
import com.lucene.webSearch.utility.ApplicationException;
import com.lucene.webSearch.utility.FileHandling;
import com.lucene.webSearch.utility.PropertyLoader;

public class CreateInitialIndex {
	private IndexWriter writer;

	public CreateInitialIndex() {

	}

	public void execute() {

		long start = System.currentTimeMillis();
		System.out.println("Execution started at :: " + start);

		CreateInitialIndex indexer = null;
		int numIndexed = 0;
		String indexDir = null;
		String dataDir = null;

		try {

			PropertyLoader.initialisePropertyLoader();
			System.out.println("Property File initialised.");

			// INDEX DIRECTORY
			indexDir = ApplicationConstants.INDEX_OUTPUT_FILE_DIRECTORY;
			// "C:\\temp\\lucene\\index";

			// INPUT FILES DIRECTORY
			dataDir = ApplicationConstants.INPUT_FILE_DIRECTORY;
			// "C:\\temp\\lucene\\file";

			if (ApplicationManager.isFirstIndexUpdateOnStartUpEnabled()) {
				// Check if Index directory is available else create it
				if (FileHandling.createDirectoryIfNotExists(indexDir)) {
					// directory is created now so create Initial Index
					System.out
							.println("Creating Initial Index as it is not yet created after creating index directory.");
					DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
							"Creating Initial Index as it is not yet created after creating index directory.",
							"Started at : " + System.currentTimeMillis());
					indexer = new CreateInitialIndex(indexDir);
					numIndexed = indexer.index(dataDir, new TextFilesFilter());
					DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
							"Creating Initial Index as it is not yet created after creating index directory.",
							"Ended at : " + System.currentTimeMillis());
				} else {
					DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
							"Re-create Initial Index after deleting Initial index directory.",
							"Started at : " + System.currentTimeMillis());
					System.out.println("Re-create Initial Index after deleting Initial index directory.");
					// directory exists. Since update is enabled clear index
					FileHandling.deleteFilesinFolder(indexDir);
					DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
							"Initial Index purged successfully.", "");
					System.out.println("Initial Index purged successfully. Creating new Index.");
					// re-create Initial Index
					indexer = new CreateInitialIndex(indexDir);
					numIndexed = indexer.index(dataDir, new TextFilesFilter());
					DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
							"Re-create Initial Index after deleting Initial index directory.",
							"Ended at : " + System.currentTimeMillis());
				}
			} else if (ApplicationManager.isFirstIndexEnabled()) {
				if (FileHandling.createDirectoryIfNotExists(indexDir)) {
					// directory is created now so create Initial Index
					System.out.println("Creating index directory.");
					System.out.println("Creating First Index as it is not yet created after creating index directory.");
					DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
							"Creating First Index as it is not yet created after creating index directory.",
							"Started at : " + System.currentTimeMillis());
					indexer = new CreateInitialIndex(indexDir);
					numIndexed = indexer.index(dataDir, new TextFilesFilter());
					DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
							"Creating First Index as it is not yet created after creating index directory.",
							"Ended at : " + System.currentTimeMillis());
				} else {
					System.out.println("Index directory present.");
					if (FileHandling.isEmptyDirectory(indexDir)) {
						DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
								"Creating Initial Index as it is not yet created in index directory.",
								"Started at : " + System.currentTimeMillis());
						System.out.println("Creating Initial Index as it is not yet created in index directory.");
						indexer = new CreateInitialIndex(indexDir);
						numIndexed = indexer.index(dataDir, new TextFilesFilter());
						DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
								"Creating Initial Index as it is not yet created in index directory.",
								"Ended at : " + System.currentTimeMillis());
					} else {
						DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
								"Initial Index already created in index directory.", "");
						System.out.println("Initial Index already created in index directory.");
					}
				}
			} else {
				DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
						"Neither Initial Index nor Updating Index directory enabled.", "");
				System.out.println("Neither Initial Index nor Updating Index directory enabled.");
				return;
			}

		} catch (IOException ex) {
			DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
					"IO-Exception while creating Initial Index", "Error :: " + ex.getMessage());
			System.err.println("IO-Exception while creating Initial Index with error :: " + ex.getMessage());
		} catch (ApplicationException ex) {
			DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
					"Application Exception while creating Initial Index.", "Error :: " + ex.getMessage());
			System.err.println("Application Exception while creating Initial Index with error :: " + ex.getMessage());
		} catch (Exception ex) {
			DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX, "Exception while creating Initial Index.",
					"Error :: " + ex.getMessage());
			System.err.println("Exception while creating Initial Index with error :: " + ex.getMessage());
		} finally {
			try {
				if (indexer != null)
					indexer.close();
			} catch (IOException ex) {
				DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
						"IOException while closing Indexer with error", "Error :: " + ex.getMessage());
				System.err.println("IOException while closing Indexer with error :: " + ex.getMessage());
			} catch (Exception ex) {
				DBManager.ERROR_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
						"Exception while closing Indexer with error", "Error :: " + ex.getMessage());
				System.err.println("Exception while closing Indexer with error :: " + ex.getMessage());
			}
		}

		long end = System.currentTimeMillis();
		System.out.println("Execution ended at :: " + end);
		System.out.println("Indexing " + numIndexed + " files took " + (end - start) + " milliseconds");
		DBManager.AUDIT_INSERT(ApplicationConstants.CREATE_INITIAL_INDEX,
				"Indexing " + numIndexed + " files took " + (end - start) + " milliseconds", "");
	}

	public CreateInitialIndex(String indexDir) throws IOException {
		Directory dir = FSDirectory.open((new File(indexDir)).toPath());
		StandardAnalyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		// LUCENE INDEX WRITER
		writer = new IndexWriter(dir, config);
	}

	public void close() throws IOException {
		// CLOSE INDEX WRITER
		writer.close();
	}

	public int index(String dataDir, FileFilter filter) throws Exception {
		File[] files = new File(dataDir).listFiles();
		for (File f : files) {
			if (!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead()
					&& (filter == null || filter.accept(f))) {
				indexFile(f);
			}
		}
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
		// ADD DOCUMENT TO LUCENE INDEX
		writer.addDocument(doc);
	}

	private static class TextFilesFilter implements FileFilter {
		public boolean accept(File path) {
			// INDEX ONLY .TXT USING FILTER
			return path.getName().toLowerCase().endsWith(".txt");
		}
	}

}
