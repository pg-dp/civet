package org.dice_research.opal.civet.metrics;

import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The ProviderIdentity gives a star rating to a dataset based on any available
 * publisher of the dataset which is provided through the predicate
 * dct:publisher. If dct:publisher predicate is not present or empty then check
 * for a landing page in the dataset which is given by a predicate
 * dcat:landingPage. If still no publisher is found then check for access URL in
 * each distributions which is given by a predicate dcat:accessURL. A zero star
 * is awarded in case of no availability of a publisher.
 * 
 * @author Gourab Sahu
 */

public class ProviderIdentityMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "If dataset has a Publisher which is of type FOAF.person or FOAF.organization "
			+ "or FOAF.Agent then 5 atars are awarded "

			+ "If dataset has a Publisher which is not of type FOAF.person or FOAF.organization "
			+ "or FOAF.Agent then 4 atars are awarded "

			+ "If dct:publisher predicate does not provide a publisher then in that case if "
			+ "dcat:Landingpage predicate provides a valid URL then award 5 stars. Landing page " + "has a FOAF range."

			+ "As a last resort check if all distributions have a access URL and based on the "
			+ "percentage of availability of accessURL award a star rating ";

	public static boolean isValidURL(String checkURL) {
		/*
		 * Here we check whether the URL of foaf:homepage is a valid URL or not.
		 */
		try {
			new URL(checkURL).toURI();
			return true;
		}

		catch (Exception e) {
			return false;
		}
	}

	public int evaluatePublisher(Resource publisher) {

		int publisherScore = 0;

		boolean publisherIsFoafResource = false;

		if (publisher.hasProperty(RDF.type, FOAF.Agent) || (publisher.hasProperty(RDF.type, FOAF.Person))
				|| (publisher.hasProperty(RDF.type, FOAF.Organization))
				|| (publisher.hasProperty(RDF.type, FOAF.Group)))
			publisherIsFoafResource = true;

		String foafName = publisher.hasProperty(FOAF.name) ? publisher.getProperty(FOAF.name).getObject().toString()
				: "";

		/*
		 * If a publisher is of type foaf:org or foaf:person and has an non-empty name
		 * then 5 stars are awarded.
		 */
		if (publisherIsFoafResource && !foafName.isEmpty())
			publisherScore = 5;
		/*
		 * If resource is not a FOAF:organisation or FOAF:Person or FOAF:Agent but has a
		 * publisher then 4 stars are awarded for not following DCAT recommendations.
		 */
		else if (!publisherIsFoafResource && !foafName.isEmpty())
			publisherScore = 4;

		/*
		 * Last case check if dct:publisher has been given in the form of a URL. 4 stars
		 * for not following DCAT recommendations.
		 */
		else if (isValidURL(publisher.toString()))
			publisherScore = 4;

		return publisherScore;

	}

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		int publisherScore = 0;
		int totalPercentageOfAccessURL = 0;
		int numberOfDistributions = 0;

		LOGGER.info("Processing dataset " + datasetUri);

		Resource dataset = model.createResource(datasetUri);

		NodeIterator publishers = model.listObjectsOfProperty(dataset, DCTerms.publisher);

		while (publishers.hasNext()) {

			RDFNode publisher = publishers.next();

			if (publisher.isAnon() || publisher.isURIResource()) {
				publisherScore = evaluatePublisher((Resource) publisher);
				if (publisherScore == 5)
					break;
			}
		}

		/*
		 * If publisherScore=0 then check if dcat:landingPage is available in
		 * dcat:catalog
		 */
		if (publisherScore == 0) {

			// It could be possible that more than 1 landing pages exist in a dataset.
			NodeIterator landingPages = model.listObjectsOfProperty(dataset, DCAT.landingPage);

			while (landingPages.hasNext()) {

				Object landingPage = landingPages.next();

				if (isValidURL(landingPage.toString())) {
					publisherScore = 5;
					break;
				}

			}
		}

		// If LandingPage is not there then check for AccessURL in the distribution
		if (publisherScore == 0) {

			int numberOfAccessUrl = 0;

			NodeIterator distributions = model.listObjectsOfProperty(dataset, DCAT.distribution);

			while (distributions.hasNext()) {
				RDFNode distribution = distributions.next();

				if (distribution.isResource()) {
					Resource distributionResource = (Resource) distribution;
					if (distributionResource.hasProperty(DCAT.accessURL)
							&& isValidURL(distributionResource.getProperty(DCAT.accessURL).getObject().toString())) {
						numberOfAccessUrl++;
					}
				}
				numberOfDistributions++;
			}

			if (numberOfDistributions > 0) {
				totalPercentageOfAccessURL = (numberOfAccessUrl * 100) / numberOfDistributions;
			}

			if (totalPercentageOfAccessURL == 100)
				publisherScore = 5;
			else if (totalPercentageOfAccessURL < 100 && totalPercentageOfAccessURL >= 75)
				publisherScore = 4;
			else if (totalPercentageOfAccessURL < 75 && totalPercentageOfAccessURL >= 50)
				publisherScore = 3;
			else if (totalPercentageOfAccessURL < 50 && totalPercentageOfAccessURL >= 25)
				publisherScore = 2;
			else if (totalPercentageOfAccessURL < 25 && totalPercentageOfAccessURL > 0)
				publisherScore = 1;

		}

		return publisherScore;

	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_PROVIDER_IDENTITY.getURI();
	}

}
