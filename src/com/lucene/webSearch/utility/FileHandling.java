package com.lucene.webSearch.utility;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.tcs.bits.lucene.UpdateInitialIndex;

public class FileHandling {

	/**
	 * Method to check if given path is a Directory
	 * 
	 * @param path
	 * @return true/false
	 */
	public static Boolean isDirectory(final String path) {
		if (Paths.get(path) == null || !Files.exists(Paths.get(path))) {
			System.out.println("The Path is not a directory.");
			return false;
		} else {
			return Files.isDirectory(Paths.get(path));
		}
	}

	/**
	 * Method to check if Directory is empty for a given path
	 * 
	 * @param path
	 * @return true/false
	 */
	public static Boolean isEmptyDirectory(final String path) {
		if (isDirectory(path)) {
			System.out.println("The Path is a directory.");
			File parentDir = new File(path);
			if (parentDir.isDirectory() && parentDir.list().length < 4) {
				System.out.println("The Directory is empty or all initial files are not available.");
				return true;
			} else {
				System.out.println("The Directory is not empty.");
				return false;
			}
		} else {
			System.out.println("The Path is not a directory.");
			return false;
		}
	}

	/**
	 * Method to delete all files in the Directory
	 * 
	 * @param path
	 */
	public static void deleteFilesinFolder(final String path) {

		if (!isEmptyDirectory(path)) {
			System.out.println("The Directory is not empty.");
			File[] files = (new File(path)).listFiles();
			if (files != null) { // some JVMs return null for empty dirs
				for (File f : files) {
					if (f.isDirectory()) {
						System.out.println("There is a sub directory.");
						deleteFilesinFolder(f.getAbsolutePath());
					} else {
						System.out.println("Deleting File :: " + f.getName());
						f.delete();
					}
				}
			}
		}
	}
	
	/**
	 * Method to create Directory if it is not available.
	 *
	 * @param path
	 * @return
	 */
	public static Boolean createDirectoryIfNotExists(final String path){
	    File directory = new File(path);
	    if (! directory.exists()){
	    	System.out.println("Directory not available so creating the Folder Structure.");
	        directory.mkdirs();
	        return true;
	    }
	    System.out.println("Directory already exists.");
	    return false;
	}
	
		public static void watchDirectoryPath(Path path) {
			// Sanity check - Check if path is a folder
			try {
				Boolean isFolder = (Boolean) Files.getAttribute(path,
						"basic:isDirectory", NOFOLLOW_LINKS);
				if (!isFolder) {
					throw new IllegalArgumentException("Path: " + path + " is not a folder");
				}
			} catch (IOException ioe) {
				// Folder does not exists
				ioe.printStackTrace();
			}
			
			System.out.println("Watching path: " + path);
			
			// We obtain the file system of the Path
			FileSystem fs = path.getFileSystem ();
			
			// We create the new WatchService using the new try() block
			try(WatchService service = fs.newWatchService()) {
				
				// We register the path to the service
				// We watch for creation events
				path.register(service, ENTRY_CREATE);
				
				// Start the infinite polling loop
				WatchKey key = null;
				while(true) {
					key = service.take();
					
					// Dequeueing events
					Kind<?> kind = null;
					for(WatchEvent<?> watchEvent : key.pollEvents()) {
						// Get the type of the event
						kind = watchEvent.kind();
						if (OVERFLOW == kind) {
							continue; //loop
						} else if (ENTRY_CREATE == kind || ENTRY_MODIFY == kind) {
							// A new Path was created 
							Path filePath = ((WatchEvent<Path>) watchEvent).context();
							// Output
							System.out.println("New path created: " + filePath);
							new UpdateInitialIndex().execute(filePath.toFile());
						}
					}
					
					if(!key.reset()) {
						break; //loop
					}
				}
				
			} catch(IOException ioe) {
				ioe.printStackTrace();
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
			
		}
	 
		
		/**
		 * Method to process all the modified/created files for last n minutes
		 * 
		 * @param directory
		 * @param minutes
		 */
		public static void processAllModifiedFiles(String directory, int minutes)
		{
			// cutoff date:
		    Instant period = Instant.now().minus(minutes, ChronoUnit.MINUTES);
		    List<Path> fileList = new ArrayList<Path>();
		    
		    // find with filter
		    try {
				Files.find(Paths.get(directory), Integer.MAX_VALUE,
				    (p, a) -> {
				        try {
				            return Files.isRegularFile(p)
				                && Files.getLastModifiedTime(p).toInstant().isAfter(period);
				        }
				        catch(IOException e) {
				            throw new RuntimeException(e);
				        }
				    })
				    .forEach(fileList::add);

				fileList.forEach(p -> new UpdateInitialIndex().execute(p.toFile()));
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    
		}
		
		
		/*
		public static void main(String[] args) throws IOException, InterruptedException, ApplicationException {
			PropertyLoader.initialisePropertyLoader();
			processAllModifiedFiles(ApplicationConstants.INPUT_FILE_DIRECTORY, 15);
		}
	*/
	

}
