package org.dice_research.opal.civet.metrics;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
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

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Check if a consistent Dataset Provider/Publisher name is given "
			+ "If dataset has only 1 Publisher then 5 atars are awarded "
			+ "If dataset has more than 1 publishers but they are consistent then 5 stars are awarded "
			+ "If dataset has more than 1 publishers but they are inconsistent then 0 stars are awarded "
			+ "If dataset has an empty blanknode for publlisher then 0 stars are awarded"
			+ "If dataset has a non-empty blanknode for publlisher with publicher info then 5 stars are awarded"
			+ "If dataset has more than 1 non-empty blanknode for publlisher with consistent publisher info then 5 stars are awarded"
			+ "If dataset has more than 1 non-empty blanknode for publlisher with inconsistent publisher info then 0 stars are awarded"
			+ "If dataset has no provider information at all then 0 stars are awarded"
			+ "Before awarding stars, we are checking whether the publishers are of type foaf:Agent"
			+ "If no publisher info but there is a foaf:homePage in the DataCatalog with valid URL then in that case award 5 star";

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

		// Will be used to check for consistency
		HashMap<String, Integer> PublisherScore = new HashMap<String, Integer>();

		Property FoafName = model.createProperty("http://xmlns.com/foaf/0.1/name");
		Property a_Predicate = model.createProperty("a");

		StmtIterator IteratorOverPublisher = model
				.listStatements(new SimpleSelector(null, DCTerms.publisher, (RDFNode) null));

		if (IteratorOverPublisher.hasNext()) {

			while (IteratorOverPublisher.hasNext()) {

				Statement StatementWithPublisher = IteratorOverPublisher.nextStatement();
				RDFNode Publisher = StatementWithPublisher.getObject();
				// Check if the publisher object is a blank node.
				if (Publisher.isAnon()) {
					Resource PublisherBlankNode = (Resource) Publisher;
					/*
					 * If blanknode is of type foaf:agent and has an non-empty name then 5 stars are
					 * awarded.
					 */
					if ((PublisherBlankNode.hasProperty(RDF.type, FOAF.Organization)
							|| PublisherBlankNode.hasProperty(RDF.type, FOAF.Person)
							|| PublisherBlankNode.hasProperty(RDF.type, FOAF.Agent))
							&& (!PublisherBlankNode.getProperty(FOAF.name).getObject().toString().isEmpty())) {
						if (PublisherScore.size() == 0 && !(PublisherScore
								.containsKey(PublisherBlankNode.getProperty(FOAF.name).getObject().toString())))
							PublisherScore.put(PublisherBlankNode.getProperty(FOAF.name).getObject().toString(), 5);
						else if (!(PublisherScore.size() == 0) && !(PublisherScore
								.containsKey(PublisherBlankNode.getProperty(FOAF.name).getObject().toString()))) {
							PublisherScore.put("InconsistentPublishers", 0);
							break;
						}
					}
					/*
					 * If blanknode has a non-empty name but not sure about the type i.e not a
					 * foaf:agent then 4 stars are awarded for not following DCAT recommendations.
					 */
					else if (PublisherBlankNode.hasProperty(FOAF.name)
							&& (!PublisherBlankNode.getProperty(FOAF.name).getObject().toString().isEmpty())) {
						if (PublisherScore.size() == 0 && !(PublisherScore
								.containsKey(PublisherBlankNode.getProperty(FOAF.name).getObject().toString())))
							PublisherScore.put(PublisherBlankNode.getProperty(FOAF.name).getObject().toString(), 4);
						else if (!(PublisherScore.size() == 0) && !(PublisherScore
								.containsKey(PublisherBlankNode.getProperty(FOAF.name).getObject().toString()))) {
							PublisherScore.put("InconsistentPublishers", 0);
							break;
						}
					}
				} else if (Publisher.isURIResource()) {
					Resource PublisherURI = (Resource) Publisher;

					/*
					 * If URI is of type foaf:agent and has an non-empty name then 5 stars are
					 * awarded.
					 */
					if ((PublisherURI.hasProperty(RDF.type, FOAF.Organization)
							|| PublisherURI.hasProperty(RDF.type, FOAF.Person)
							|| PublisherURI.hasProperty(RDF.type, FOAF.Agent))
							&& (!PublisherURI.getProperty(FOAF.name).getObject().toString().isEmpty())) {
						if (PublisherScore.size() == 0 && !(PublisherScore
								.containsKey(PublisherURI.getProperty(FOAF.name).getObject().toString())))
							PublisherScore.put(PublisherURI.getProperty(FOAF.name).getObject().toString(), 5);
						else if (!(PublisherScore.size() == 0) && !(PublisherScore
								.containsKey(PublisherURI.getProperty(FOAF.name).getObject().toString()))) {
							PublisherScore.put("InconsistentPublishers", 0);
							break;
						}
					}

					/*
					 * If URI has a non-empty name but not sure about the type i.e not a foaf:agent
					 * then 4 stars are awarded for not following DCAT recommendations.
					 */

					else if (PublisherURI.hasProperty(FOAF.name)
							&& (!PublisherURI.getProperty(FOAF.name).getObject().toString().isEmpty())) {
						if (PublisherScore.size() == 0 && !(PublisherScore
								.containsKey(PublisherURI.getProperty(FOAF.name).getObject().toString())))
							PublisherScore.put(PublisherURI.getProperty(FOAF.name).getObject().toString(), 4);
						else if (!(PublisherScore.size() == 0) && !(PublisherScore
								.containsKey(PublisherURI.getProperty(FOAF.name).getObject().toString()))) {
							PublisherScore.put("InconsistentPublishers", 0);
							break;
						}
					}
				}
			}
		}

		/*
		 * If PublisherInfo=0 then as a last resort check if foaf:homepage available in
		 * dcat:catalog
		 */
		if (PublisherScore.size() == 0) {

			ResIterator CatalogIterator = model.listSubjectsWithProperty(RDF.type, DCAT.Catalog);
			if (CatalogIterator.hasNext()) {
				while (CatalogIterator.hasNext()) {
					Resource Catalog = CatalogIterator.nextResource();
					if (Catalog.hasProperty(FOAF.homepage)
							&& (!Catalog.getProperty(FOAF.homepage).getObject().toString().isEmpty())) {
						if (PublisherScore.size() == 0 && !(PublisherScore
								.containsKey(Catalog.getProperty(FOAF.name).getObject().toString()))) {
							if (isValidURL(Catalog.getProperty(FOAF.name).getObject().toString()))
								PublisherScore.put(Catalog.getProperty(FOAF.name).getObject().toString(), 5);
						} else if (!(PublisherScore.size() == 0) && !(PublisherScore
								.containsKey(Catalog.getProperty(FOAF.name).getObject().toString()))) {
							PublisherScore.put("InconsistentPublishers", 0);
							break;
						}
					}
				}
			}
		}

		/*
		 * This section is for scoring
		 */

		if (PublisherScore.containsKey("InconsistentPublishers"))
			score = 0;
		else if (PublisherScore.size() == 0)
			score = 0;
		else if (PublisherScore.size() == 1)
			for(String key: PublisherScore.keySet())
				score= PublisherScore.get(key);
		
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