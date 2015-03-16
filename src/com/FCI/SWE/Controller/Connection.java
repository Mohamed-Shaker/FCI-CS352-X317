package com.FCI.SWE.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is connection class which defines connection attributes and properties 
 * that could be used to make a connection to a services .
 *
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 */

public class Connection {

	/**
	 * This method is used make a connection to specific services .
	 * @param serviceUrl Service Link
	 * @param urlParameters Service parameter to send to request 
	 * @param methodType Service method to use 
	 * @param contentType Specify service Content Type attributes as how to deal with data and how to encrypt them   
	 * @return a <code>String</code> parsed connection properties .   
	 */
	public static String connect(String serviceUrl, String urlParameters,
			String methodType, String contentType) {
		//System.out.println(serviceUrl);
		try {
			URL url = new URL(serviceUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod(methodType);
			connection.setConnectTimeout(60000); // 60 Seconds
			connection.setReadTimeout(60000); // 60 Seconds
			connection.setRequestProperty("Content-Type", contentType);
			/*
			 * connection.setDoOutput(true);
			 *  If they include a request body. 
			 *  Transmit data by writing to the stream returned by getOutputStream().
			 */
			OutputStreamWriter writer = new OutputStreamWriter( connection.getOutputStream() );
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			/*
			 * connection.getInputStream()
			 * Read the response. 
			 * Response headers typically include metadata .
			 * such as the response body's content type and length, modified dates and session cookies. 
			 * The response body may be read from the stream returned by getInputStream(). 
			 * If the response has no body, that method returns an empty stream.
			 */
			BufferedReader reader = new BufferedReader 
					               (new InputStreamReader(connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				retJson += line;
			}
			return retJson;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}

}
