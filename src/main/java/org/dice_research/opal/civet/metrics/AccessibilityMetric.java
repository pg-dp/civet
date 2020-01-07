package org.dice_research.opal.civet.metrics;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The Accessibility awards stars based on the Accessibility of metadata.
 * A connection is made using the URL of metadata and ratings are evaluated based on returned status code.
 * 
 * @author Amit Kumar
 */

//HTTP 200: “Everything is OK.”
//401: “Unauthorized” or “Authorization Required.”
//403: “Access to that resource is forbidden.”
//404: “The requested resource was not found.” 
//499: “Client closed request.”

public class AccessibilityMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes the quality of dataset as per contactability metric." 
			+ "Four kinds of ratings are awarded to the dataset which are following: "
			+ "5 Stars: If the URL is in the dataset and returns a successfull code HTTP 200: “Everything is OK.” upon making connection."
			+ "2 Stars: If the URL is in the dataset and returns a status code HTTP 401: “Unauthorized” or “Authorization Required.” "
			+ "1 Stars: If the URL is in the dataset and it return a status code 403: “Access to that resource is forbidden.” "
			+ "0 Stars: If the URL is not found in the dataset.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		LOGGER.info("Processing dataset " + datasetUri);
		Resource dataset = ResourceFactory.createResource(datasetUri);
		NodeIterator distributionObjectsIterator = model.listObjectsOfProperty(dataset,DCAT.distribution);
 		int result = 0;
 		HashMap<URL,  Integer> URLRatingMap=new HashMap<URL,Integer>();    


		while(distributionObjectsIterator.hasNext()) {	
			System.out.println("it exists");

			Resource distribution = (Resource) distributionObjectsIterator.next();
			RDFNode accessUrl = distribution.getProperty(DCAT.accessURL).getObject();
// 		if(distribution != null)
 		
			URL urlObj = new URL(accessUrl.toString());
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
			System.out.println("URL is: "+urlObj);
			try {
				con.setRequestMethod("HEAD");
	//			Set connection timeout
				con.setConnectTimeout(3000);
				con.connect();
				int responseCode = con.getResponseCode();
	
				if (200 <= responseCode && responseCode <= 399) {
						result = 5;
				}
				else if (400 <= responseCode && responseCode < 500) {
					result = 2;
				}
				else if(500 <= responseCode && responseCode <= 521) {
					result = 1;
				}
				else {
					result = 0;
				}
			} 
			catch (MalformedURLException e) {
		        e.printStackTrace();
				result = 0;
			} 
			catch (IOException e) {
		        e.printStackTrace();
				result = 0;
			}
			catch (Exception e) {
				result = 0;
			}
			finally {
		        if (con != null) {
		        	con.disconnect();
		        }
			}
			
			
			/*** 
			 * 
			 *  calculating percentage of ratings if multiple URL exists
			 *  
			 * URLRatingMap.put(urlObj, result);   
			System.out.println(Arrays.asList(URLRatingMap)); // method 1
			int hmapsize= URLRatingMap.size();
			int count=0;
			Collection<Integer> values = URLRatingMap.values();
	        for (Integer v : values) {
	        	count=+v;
	        }
	        int averageRAting=(count/hmapsize)*100;
	        System.out.print("averageRAting :"+averageRAting);
			 
			 *
			 *
			 */
			
			
	      return result;
		}
		return result;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_CATEGORIZATION.getURI();
	}
}