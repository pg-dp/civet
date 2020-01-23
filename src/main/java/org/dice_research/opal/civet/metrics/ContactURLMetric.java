package org.dice_research.opal.civet.metrics;

import java.util.HashMap;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The ContactURLMetric awards stars based on the availability
 * of URL from provider in the dataset.
 *
 * Url metrics can be found under dcat:contactPoint and dcat:landing page
 *
 *  There are three sub metrics under contactability(Contact URL, Contact Email and Classical contact information)
 *
 *  These will be used for Metadata Quality Assurance
 *
 * @author Amit Kumar
 */

public class ContactURLMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes the quality of dataset as per contactability metric."
			+ "Two kinds of ratings are awarded to the dataset which are following: "
			+ "Stars 5: Contact URL is in DCAT.landingPage or DCAT.accessURL."
			+ "Stars 0: Contact URL is not in DCAT.landingPage or DCAT.accessURL."
			+ "Above given ratings are saved in the hashmap"
			+ "Final Rating: An average of the ratings are computed by using hashmap" ;
	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		Resource dataset = ResourceFactory.createResource(datasetUri);
		NodeIterator distributionObjectsIterator = model.listObjectsOfProperty(dataset,DCAT.distribution);
		StmtIterator stmtItr = model.listStatements(new SimpleSelector(dataset,DCAT.landingPage,(RDFNode) null));

		boolean contactUrl=false;
		HashMap<RDFNode,  Integer> URLRatingMap = new HashMap<RDFNode,Integer>();
		int urlCount = 0;

		// Iterating through dcat:accessURL to fetch contract URL
		while(distributionObjectsIterator.hasNext()) {
			Resource distribution = (Resource) distributionObjectsIterator.next();
			if(distribution.hasProperty(DCAT.accessURL))
			{
				RDFNode accessUrl = distribution.getProperty(DCAT.accessURL).getObject();
				urlCount++;

				if(accessUrl !=null) {
					URLRatingMap.put(accessUrl, 5);
				}
			}
		}

		// Iterating through dcat:landingPage to fetch contract URL
		while(stmtItr.hasNext())
		{
			Statement stmt = stmtItr.nextStatement();
			urlCount++;

			if(!stmt.getObject().isAnon()){
				URLRatingMap.put(stmt.getObject(), 5);
			}
		}

		int sumRating=0, finalRating=0;

		if(URLRatingMap.isEmpty())
		{
			finalRating=0;
		}
		else {
			for (Integer i : URLRatingMap.values()) {
				sumRating+=i;
			}
			finalRating=Math.round(sumRating/urlCount);
		}
		return finalRating;
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