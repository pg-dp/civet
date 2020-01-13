package org.dice_research.opal.civet.metrics;


import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

public class ContactEmailMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes the quality of dataset as per contactability criteria."
			+ "Two kinds of ratings are awarded to emails which are following: "
			+ "Stars 5: Email is found and is of a correct format."
			+ "Stars 1: Email is found and is of an incorrect format. "
			+ "Stars 0: If Email is not found or has an incorrect format."
			+ "Stars are stored in the hashmap and an average is calculated to return a final rating.";

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)",
		    		Pattern.CASE_INSENSITIVE);

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		//fetching datasetUri and creating resource
		Resource dataset = ResourceFactory.createResource(datasetUri);
		StmtIterator stmtItr = model.listStatements(new SimpleSelector(dataset, DCAT.contactPoint, (RDFNode) null));

		boolean emailFound = false;
		HashMap<String, Integer> EmailRatingMap = new HashMap<String, Integer>();
		int countEmail = 0;
		Property customEmail = model.createProperty("http://www.w3.org/2006/vcard/ns#hasEmail");

		if (stmtItr.hasNext()) {
			countEmail++;
			Statement stmt = stmtItr.nextStatement();
			RDFNode object = stmt.getObject();
			Resource objectAsResource = (Resource) object;
			if (objectAsResource.hasProperty(customEmail)) {
				String email = objectAsResource.getProperty(customEmail).getObject().toString();
				emailFound = validate(email);
				if (emailFound)
					EmailRatingMap.put(email, 5);
				else
					EmailRatingMap.put(email, 1);
			} else {
				EmailRatingMap.put(null, 0);
			}
		}

		int sumRating = 0;
		int averageRating = 0;
		int finalRating = 0;

		if (EmailRatingMap.isEmpty()) {
			finalRating = 0;
		}
		else {
			for (Integer i : EmailRatingMap.values()) {
				sumRating += i;
			}
			averageRating = sumRating / countEmail;
			finalRating = Math.round(averageRating);
		}
		return finalRating;
	}

	public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr); 
        return matcher.find();
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
