package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCAT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.common.vocabulary.Opal;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.DCAT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

public class ContactEmailMetric implements Metric {
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
		Resource distribution = ResourceFactory.createResource(datasetUri);
		Statement statement = model.getProperty(distribution, DCAT.accessURL);

		String accessUrl = "";
 		if(statement != null)
 			accessUrl = String.valueOf(statement.getObject());
 		
 		int result = 0;

		URL urlObj = new URL(accessUrl);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		try {
			con.setRequestMethod("HEAD");
//			Set connection timeout
			con.setConnectTimeout(3000);
			con.connect();
			int responseCode = con.getResponseCode();

			if (200 <= responseCode && responseCode <= 399) 
				result = 5;
			else if (400 <= responseCode && responseCode < 500)
				result = 2;
			else if(500 <= responseCode && responseCode <= 521) 
				result = 1; 
			else 
				result = 0;
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
