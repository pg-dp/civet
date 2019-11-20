package org.dice_research.opal.civet.metrics;

import java.net.URL;
import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The CategorizationMetric awards stars based on the number of keywords of a
 * dataset.
 * 
 * @author Adrian Wilke
 */
public class ProviderIdentityMetric implements Metric {

	public static boolean isValidLicenseURL(String LicenseURL) {
		/*
		 * new URL tries to create a new URL with provided license URL. If invalid
		 * License URL then catch exception and return false.
		 */
		try {
			new URL(LicenseURL).toURI();
			return true;
		}

		catch (Exception e) {
			return false;
		}
	}

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Check if a consistent Dataset Provider name is given "
			+ "If dataset has only 1 Publisher then 5 atars are awarded "
			+ "If dataset has more than 1 publishers but they are consistent then 5 stars are awarded "
			+ "If dataset has more than 1 publishers but they are inconsistent then 0 stars are awarded "
			+ "If dataset has an empty blanknode for publlisher then 0 stars are awarded"
			+ "If dataset has a non-empty blanknode for publlisher with publicher info then 5 stars are awarded"
			+ "If dataset has more than 1 non-empty blanknode for publlisher with consistent publisher info then 5 stars are awarded"
			+ "If dataset has more than 1 non-empty blanknode for publlisher with inconsistent publisher info then 0 stars are awarded"
			+ "If dataset has no provider information at all then 0 stars are awarded.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		// Resource dataset = ResourceFactory.createResource(datasetUri);

		// NodeIterator nodeIterator = model.listObjectsOfProperty(dataset,
		// DCAT.keyword);

        //Score to return
		int score = 0;

		// This is incremented if and only if we have a solid name for provider.
		int TotalPublisherInfo = 0;

		// Consistency check for Provider
		Boolean InconsistentPublishers = false;

		// Will be used to check for consistency among providers
		ArrayList<String> ListOfPublishers = new ArrayList<String>();

		Property FoafName = model.createProperty("http://xmlns.com/foaf/0.1/name");

		StmtIterator IteratorOverPublisher = model
				.listStatements(new SimpleSelector(null, DCTerms.publisher, (RDFNode) null));

		if (IteratorOverPublisher.hasNext()) {

			while (IteratorOverPublisher.hasNext()) {

				Statement StatementWithPublisher = IteratorOverPublisher.nextStatement();
				RDFNode Publisher = StatementWithPublisher.getObject();
				// Check if the publisher object is a blank node.
				if (Publisher.isAnon()) {
					Resource PublisherBlankNode = (Resource) Publisher;
					// After checking for Property FoafName--->Check if the respective Subject is
					// not empty
					if (PublisherBlankNode.hasProperty(FoafName)) {
						if (!(PublisherBlankNode.getProperty(FoafName).getObject().toString().isEmpty())) {
							TotalPublisherInfo++;
							ListOfPublishers.add(PublisherBlankNode.getProperty(FoafName).getObject().toString());
						}
					}
				} else {
					// If it is not a blank node, then check if the respective object is not empty
					if (!Publisher.toString().isEmpty()) {
						TotalPublisherInfo++;
						ListOfPublishers.add(Publisher.toString());
					}

				}

			}
		}

		/*
		 * This section is for scoring
		 */

		// First check if more than one publishers then are they consistent ?
		if (TotalPublisherInfo > 1) {
			for (int count = 0; count < ListOfPublishers.size() - 1; count++) {
				if (!(ListOfPublishers.get(count).trim().equals(ListOfPublishers.get(count + 1).trim()))) {
					// System.out.println(ListOfPublishers.get(count) +" AND "+
					// ListOfPublishers.get(count+1));
					InconsistentPublishers = true;
					break;
				}
			}
		}

		if (TotalPublisherInfo == 0 || InconsistentPublishers)
			score = 0;
		// If Publishers have a consistent name then give 5 stars
		else if (TotalPublisherInfo > 1 && !InconsistentPublishers)
			score = 5;
		else if (TotalPublisherInfo == 1)
			score = 5;

		return score;

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