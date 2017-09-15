package com.lostagain.JamGwt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.darkflame.client.interfaces.SSSGenericFileManager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.Response;
import com.lostagain.Jam.RequiredImplementations;
import com.lostagain.Jam.Interfaces.JAMGenericFileManager;
import com.lostagain.JamGwt.GwtJamImplementations.BasicGameInformationImp;
import com.lostagain.JamGwt.JAMtext.JamText;

/** Manages file loading for the Jam game and its semantics.
 *  Includes built in cache and local override support.
 *  
 *  File retrieval can either work directly, or via a textfetcher.php file which hides its location.
 * 
 *  **/
public class GwtFileManagerImp implements JAMGenericFileManager  {


	static Logger Log = Logger.getLogger("JAM.GwtFileManagerImp");

	/** If the game is running from a local source, php is naturally not used.
	 * Because of this, files are accessed directly at this location,
	 * rather then via "textfetcher.php **/
	public static String LocalFolderLocation =   RequiredImplementations.BasicGameInformationImplemention.get().getSecureFolderLocation();
			//JAM.theme.get("localloc");

	//NOTE: we cant use RequiredImplementation to get the locations yet as that might not have been setup
	//so we grab the information from gwt directly
	
	/** The location and name of the textfetcher.php that returns requested text files **/
	public static final String textfetcher_url = RequiredImplementations.BasicGameInformationImplemention.get().getHomedirectory() + "text%20fetcher.php"; //$NON-NLS-1$

			//JAM.homeurl + "text%20fetcher.php"; //$NON-NLS-1$

	/** A cache of all known text files associated with their filename**/
	static HashMap<String,String> allCachedTextFiles = new HashMap<String,String>();

	private static boolean neverUsePost = RequiredImplementations.BasicGameInformationImplemention.get().setToNeverRequestFilesWithPost();
			

	public static void setNeverUsePost(boolean neverUsePost) {
		GwtFileManagerImp.neverUsePost = neverUsePost;
	}



	/** Gets a bit of text using a php file,  <br>
	 * to help hide its location from the player. <br>
	 * You can set if you want this to add to the cache or not. <br>
	 * By default, everything is cached unless you use a POST request  <br>**/
	public static void GetTextFile(final String fileurl,
			final FileCallbackRunnable callback, FileCallbackError onError, boolean forcePOST) {

		if (!forcePOST){
			GetTextFile(fileurl,callback,  onError,  forcePOST, true);
		} else {
			GetTextFile(fileurl,callback,  onError,  forcePOST, false);
		}

	}



	public static void GetTextFile(final String fileurl,
			final FileCallbackRunnable callback, final FileCallbackError onError, boolean forcePOST,final boolean UseCache) {

		//first we check local overrides
		//these are inline files defined at compile time made to override external ones
		String inlineOveride = JamText.getExternalFileOverride(fileurl);
		if (inlineOveride!=null){
			Log.info("contents found in interal override:"+fileurl);
			callback.run(inlineOveride, Response.SC_OK);
			return;
		}

		//continue if not found as override

		//if useCache is on, first we search for the filename in the cache
		if (UseCache){

			String contents = allCachedTextFiles.get(fileurl);

			if (contents!=null){

				Log.info("contents found in cache");
				callback.run(contents, Response.SC_OK);
				//remember to return, as its found in the cache, no need to bother a server with anything
				return;
			}


		}


		//if not a cache, we create a new RequestCallback for a real sever request response
		RequestCallback responseManagment = new RequestCallback(){
			@Override
			public void onResponseReceived(Request request, Response response) {	

				//add to cache if not a 404	
				if (response.getStatusCode()==Response.SC_OK && UseCache){
					Log.info("found file at "+fileurl+", storing in cache");
					allCachedTextFiles.put(fileurl,response.getText());
				}

				Log.info("got file running callback ");
				callback.run(response.getText(), response.getStatusCode());

			}

			@Override
			public void onError(Request request, Throwable exception) {

				onError.run("",exception);


			}

		};


		if (LocalFolderLocation.length()<3) {
			RequestBuilder requestBuilder = new RequestBuilder(
					RequestBuilder.POST, textfetcher_url);

			try {
				requestBuilder.sendRequest("FileURL=" + fileurl, responseManagment);
			} catch (RequestException e) {
				e.printStackTrace();
				System.out
				.println("can not connect to file via php:" + fileurl);
			}
		} else {

			Method requestType = RequestBuilder.GET;

			if (forcePOST && neverUsePost == false) {
				requestType = RequestBuilder.POST;
			}

			String url = fileurl;
			//if its not a absolute path we add the default folder location to it
			if (!isAbsolutePath(url)){
				url = LocalFolderLocation + fileurl;
			} 
			RequestBuilder requestBuilder = new RequestBuilder(requestType,url);

			try {
				requestBuilder.sendRequest("", responseManagment);
			} catch (RequestException e) {
				System.out.println("can not connect to file:" + fileurl); //$NON-NLS-1$
				e.printStackTrace();

				// special runnable commands on error if any
				if (onError != null) {

					onError.run("could not event send request:"+e.getLocalizedMessage(),null);

				}

			}

		}

	}

	/** legacy method. Should be phased out.
	 * Hay, look, I used a deprecated! Didnt do that before whoa...getting to be a real coder now **/	
	@Deprecated 
	public static void GetTextSecurelyOLDMETHOD_DEPR(final String fileurl,
			final RequestCallback callback, final Runnable onError, boolean forcePOST) {

		if (LocalFolderLocation.length()<3) {

			RequestBuilder requestBuilder = new RequestBuilder(
					RequestBuilder.POST, textfetcher_url);

			try {
				requestBuilder.sendRequest("FileURL=" + fileurl, callback);
			} catch (RequestException e) {
				e.printStackTrace();
				System.out
				.println("can not connect to file via php:" + fileurl);
			}
		} else {
			Method requestType = RequestBuilder.GET;
			if (forcePOST) {
				requestType = RequestBuilder.POST;
			}

			String url = fileurl;
			//if its not a absolute path we add the default folder location to it
			if (!isAbsolutePath(url)){
				url = LocalFolderLocation + fileurl;
			} 

			RequestBuilder requestBuilder = new RequestBuilder(requestType,url);

			try {
				requestBuilder.sendRequest("", callback);
			} catch (RequestException e) {
				System.out.println("can not connect to file:" + fileurl); //$NON-NLS-1$
				e.printStackTrace();

				// special runnable commands on error if any
				if (onError != null) {

					onError.run();

				}

			}

		}

	}


	
	
	//Note; from secure is ignored. when via php all non-absolute paths are always secure

	@Override
	public void getText(String location,boolean fromSecureLocation,
			FileCallbackRunnable runoncomplete, FileCallbackError runonerror,
			boolean forcePost, boolean useCache) {

		GwtFileManagerImp.GetTextFile(location, runoncomplete, runonerror, forcePost, useCache);


	}



	@Override
	public void getText(String location,boolean fromSecureLocation,
			FileCallbackRunnable runoncomplete, FileCallbackError runonerror,
			boolean forcePost) {

		GwtFileManagerImp.GetTextFile(location, runoncomplete, runonerror, forcePost);

	}

	@Override
	public void getText(String location,
			FileCallbackRunnable runoncomplete, FileCallbackError runonerror,
			boolean forcePost, boolean useCache) {

		GwtFileManagerImp.GetTextFile(location, runoncomplete, runonerror, forcePost, useCache);

	}



	@Override
	public void getText(String location,
			FileCallbackRunnable runoncomplete, FileCallbackError runonerror,
			boolean forcePost) {

		GwtFileManagerImp.GetTextFile(location, runoncomplete, runonerror, forcePost);

	}

	/** Caches a text file, it will use this instead of taking it from the file system **/
	public String cacheTextFile(String fileurl,String data) {
		return allCachedTextFiles.put(fileurl,data);	
	}

	/** Remove the text file associated with this url from the cache **/
	public String removeTextFile(String fileurl,String data) {
		return allCachedTextFiles.remove(fileurl);
	}


	static public String getabsolute(String base,String relative) {

		LinkedList<String> stack = new LinkedList<String>();

	//	Log.info("base="+base);

		String splitstack[] = base.split("/");
		String parts[] = relative.split("/");

		//Log.info("splitstack="+splitstack.toString());

		for (String string : splitstack) {
			stack.addFirst(string);

		}

		//   stack.addAll(Arrays.asList(splitstack));


	//	Log.info("stack="+stack.toString());
		//  stack.pop(); // remove current file name (or empty string)
		// (omit if "base" is the current folder without trailing slash)

		// stack.removeFirst();

		for (int i=0; i<parts.length; i++) {
			if (parts[i] == ".")
				continue;
			if (parts[i] == "..")	        	
			{
				String removeFirst = stack.removeFirst(); //note we need to remove it even if we dont display the var
			//	Log.info("removing:"+ removeFirst);
			}
			else
			{
				//stack.push(parts[i]);
				stack.addFirst(parts[i]);
			}
		}

		String url = "";
		for (String part : stack) {

		//	Log.info("part="+part);


			url =  part+"/"+url;

		}

		//remove last slash
		if (url.endsWith("/")){
			url = url.substring(0, url.length()-1);
		}

		Log.info("full path is worked out as:"+ url);

		return url;
	}



	/**
	 * due to how the interface is set up we need both a static and non-static implementation of this
	 * it does the same thing, however
	 * @param relativepath
	 * @return
	 */
	public String getAbsolutePath(String relativepath) {

		/*
		Log.info("get full path of:"+relativepath);

		if (isAbsolutePath(relativepath)){
			Log.info("path is likely already absolutee");

			return relativepath;

		}


		//if it starts with a slash we add a dot in front to say its relative to the current directory and not route
		//if (!relativepath.startsWith("/")){
		//	relativepath="/"+relativepath;
		//}



		String fullpath = getabsolute(GWT.getHostPageBaseURL(),relativepath);

		 */

		return getAbsolutePathStatic(relativepath);
	}


	/**
	 * due to how the interface is set up we need both a static and non-static implementation of this
	 * it does the same thing, however
	 * @param relativepath
	 * @return
	 */
	static  public String getAbsolutePathStatic(String relativepath) {

		Log.info("get full path of:"+relativepath);

		if (isAbsolutePath(relativepath)){
			Log.info("path is likely already absolutee");			
			return relativepath;
		}


		String fullpath = getabsolute(GWT.getHostPageBaseURL(),relativepath);


		return fullpath;
	}







	private static boolean isAbsolutePath(String relativepath) {
		//if its a web address ignore it
		//examples of absolute paths would be;
		// www.darkflame.co.uk
		// http://lostagain.nl
		// file://W:/RandomReviewShow/Semantics.ntlist
		// Looking for the :/ seems the best way to find a absolute path for the moment
		if (relativepath.contains(":/") || relativepath.startsWith("www.")){
			Log.info(relativepath+" seems to be a absolute path");

			return true;

		}
		return false;
	}



	/**
	 * we dont use this file saving system, so its left blank
	 */
	@Override
	public boolean saveTextToFile(String location,String contents,FileCallbackRunnable runoncomplete, FileCallbackError runonerror) {
		runonerror.run("NO SAVE SYSTEM IMPLEMENTED",new Throwable());
		return false;

	}



	/** callback class type designed to be used as a response when 
	 * fetching text from a server, we now use the identical class's 
	 * in the SSSGenericFileManager class **/
	//	
	//	public static interface FileCallbackRunnable {		
	//		void run(String responseData, int responseCode);
	//
	//	}
	//	/** callback class type designed to be used when theres an error fetching text from a server */
	//	public static interface FileCallbackError {		
	//		void run(String errorData, Throwable exception);
	//
	//	}
	//	


}



