package org.dice_research.opal.civet.metrics;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The ContactabilityMetric awards stars based on the availability 
 * of contact details in the dataset.
 * 
 * @author Amit Kumar
 */
public class ContactURLMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes the quality of dataset as per contactability metric." 
			+ "Two kinds of ratings are awarded to the dataset which are following: "
			+ "Stars 5: Email is in the dataset."
			+ "Stars 0: If none of the Name, Email and Address is in the dataset."
			+ "Above given ratings are saved in the hashmap"
			+ "An average of those ratings are computed and a final rating is returned" +
			"" +
			"Looking into DCAT.landingPage and DCAT.accessURL for finding ContactURL";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		Resource dataset = ResourceFactory.createResource(datasetUri);
		NodeIterator distributionObjectsIterator = model.listObjectsOfProperty(dataset,DCAT.distribution);
		StmtIterator stmtItr = model.listStatements(new SimpleSelector(dataset,DCAT.landingPage,(RDFNode) null));

		boolean contactUrl=false;
 		HashMap<RDFNode,  Integer> URLRatingMap = new HashMap<RDFNode,Integer>();    
 		int urlCount = 0;
		
 		while(distributionObjectsIterator.hasNext()) {	
			Resource distribution = (Resource) distributionObjectsIterator.next();
			if(distribution.hasProperty(DCAT.accessURL))
			{
				RDFNode accessUrl = distribution.getProperty(DCAT.accessURL).getObject();
				if(accessUrl !=null) {
					urlCount++;
					URLRatingMap.put(accessUrl, 5);
				}
			}
			else
			{
				RDFNode accessUrl = distribution.getProperty(DCAT.accessURL).getObject();
				if(accessUrl !=null) {
					urlCount++;
					URLRatingMap.put(accessUrl, 0);
				}
			}
		}
 		
 		
// Iterating through dcat:landingPage to fetch contract URL
 		while(stmtItr.hasNext())
		{
			Statement stmt = stmtItr.nextStatement();
			if(stmt.getObject().isAnon()){
				urlCount++;
				URLRatingMap.put(stmt.getObject(), 0);
			}
			else {
				urlCount++;
				URLRatingMap.put(stmt.getObject(), 5);
			}
		}

 		int sumRating=0;
		int finalRating=0;
		int result2=0;
		if(URLRatingMap.isEmpty())
			{
				result2=0;
			}
		else {
			for (Integer i : URLRatingMap.values()) {
					sumRating+=i;
			}
			finalRating=sumRating/urlCount;
			result2 = Math.round(finalRating);
		}
return result2;
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



