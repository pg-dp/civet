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
			+ "If dataset has more than 1 publishers but they are inconsistent then 3 stars are awarded, 2 star less for inconsistency "
			+ "If dataset has an empty blanknode for publlisher then 1 stars are awarded, 4 stars less for no Publisher info in the blanknNode"
			+ "If dataset has a non-empty blanknode for publlisher with publisher info then 5 stars are awarded"
			+ "If dataset has more than 1 non-empty blanknode for publlisher with consistent publisher info then 5 stars are awarded"
			+ "If dataset has more than 1 non-empty blanknode for publlisher with inconsistent publisher info then 3 stars are awarded, 2 star less"
			+ "for Inconsistency."
			+ "If dataset has no provider information at all then 0 stars are awarded"
			+ "Before awarding stars, we are checking whether the publishers are of type Foaf:Organization or Foaf:Person. IF this info is"
			+ "not there then additionaly 1 more star will be deducted from scoring"
			+ "If no publisher info given then check for dcat:landingPage and if found award 5 star"
	        + "If no publisher info, dcat:landingPage found then check for dcat:accessURL and award 5 stars if found.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		// Resource dataset = ResourceFactory.createResource(datasetUri);

		// NodeIterator nodeIterator = model.listObjectsOfProperty(dataset,
		// DCAT.keyword);

        //Score to return
		int score = 0;

		// Will be used to check for consistency
		HashMap<String, Integer> PublisherScore = new HashMap<String, Integer>();

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
					if ((PublisherBlankNode.hasProperty(RDF.type, FOAF.Organization)|| PublisherBlankNode.hasProperty(RDF.type, FOAF.Person))
							&& (!PublisherBlankNode.getProperty(FOAF.name).getObject().toString().isEmpty())) {
						
						/*
						 * If we do not have a publisher yet, then rate the first publisher
						 * ( of type foaf:org /foaf:name) 5 stars. 
						 */
						if (PublisherScore.size() == 0 )
							PublisherScore.put(PublisherBlankNode.getProperty(FOAF.name).getObject().toString(), 5);
						/*
						 * If we already have a publisher and then we discover an another new publisher then
						 * the publisher information is not consistent, because of inconsistency we award 3/5.
						 * 
						 * 2 star less for being inconsistent
						 */
						else if (!(PublisherScore.size() == 0) && !(PublisherScore
								.containsKey(PublisherBlankNode.getProperty(FOAF.name).getObject().toString()))) {
							PublisherScore.put("InconsistentPublishers", 3);
							break;
						}
					}
					/*
					 * If blanknode has a non-empty name but not sure about the type i.e not a
					 * foaf:organisation or foaf:name then 4 stars are awarded for not following DCAT recommendations.
					 */
					else if (PublisherBlankNode.hasProperty(FOAF.name)
							&& (!PublisherBlankNode.getProperty(FOAF.name).getObject().toString().isEmpty())) {
						//If we find the first publisher but no mention of foaf:type then award 4 stars.
						//1 star less for not following DCAT's Foaf recommendations
						if (PublisherScore.size() == 0 && !(PublisherScore
								.containsKey(PublisherBlankNode.getProperty(FOAF.name).getObject().toString())))
							PublisherScore.put(PublisherBlankNode.getProperty(FOAF.name).getObject().toString(), 4);
						/*
						 * If we already have a publisher and then we discover an another new publisher then
						 * the publisher information is not consistent, because of inconsistency we award 2/5 because
						 * of inconsistency + not following DCAT recommendation(foaf:type)
						 * 
						 * 2 star less for being inconsistent + 1 star less for not following DCAT foaf recommendation
						 */
						else if (!(PublisherScore.size() == 0) && !(PublisherScore
								.containsKey(PublisherBlankNode.getProperty(FOAF.name).getObject().toString()))) {
							PublisherScore.put("InconsistentPublishers", 2);
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
							|| PublisherURI.hasProperty(RDF.type, FOAF.Person))
							&& (!PublisherURI.getProperty(FOAF.name).getObject().toString().isEmpty())) {
						/*
						 * If we find the 1st publisher which is of valid foaf type and contains a name then 
						 * award 5 stars.
						 */
						if (PublisherScore.size() == 0 )
							PublisherScore.put(PublisherURI.getProperty(FOAF.name).getObject().toString(), 5);
						
						/*
						 * If we have two different URI's with publisher name then the publishers are inconsistent
						 * but since informations are there in form of URI, there is a possibility to verify. However
						 * since DCAT recommendation was not followed we give 4/5.
						 * 
						 * 2 star less for inconsistency
						 */
						else if (!(PublisherScore.size() == 0) && !(PublisherScore
								.containsKey(PublisherURI.getProperty(FOAF.name).getObject().toString()))) {
							PublisherScore.put("InconsistentPublishers", 3);
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
							//2 star less for inconsistency + 1 star less for not following recommendation
							PublisherScore.put("InconsistentPublishers", 2);
							break;
						}
					}
				}
				else
					/*
					 * 1. A Publisher could be an URI of type foaf:org or foaf:name. We checked that.
					 * 2. A Publisher could be a blanknode of type foaf:org or foaf:name. We checked that.
					 * 3. A Publisher could be an empty blank node.
					 */
					//This is the 3rd case. Award 1 star for only dct:publisher predicate and 4 stars less for no 
					//Information for dct:Publishers.
					PublisherScore.put("EmptyBlankNodeForPublisher",1);
			}
		}

		/*
		 * If PublisherInfo=0 then check if dcat:landingPage is available in
		 * dcat:catalog
		 */
		if (PublisherScore.size() == 0 || PublisherScore.containsKey("EmptyBlankNodeForPublisher")) {

			ResIterator DatasetIterator = model.listSubjectsWithProperty(RDF.type, DCAT.Dataset);
			if (DatasetIterator.hasNext()) {
				while (DatasetIterator.hasNext()) {
					Resource Dataset = DatasetIterator.nextResource();
					if (Dataset.hasProperty(DCAT.landingPage)
							&& (!Dataset.getProperty(DCAT.landingPage).getObject().toString().isEmpty())) {
						if (PublisherScore.size() == 0 && !(PublisherScore
								.containsKey(Dataset.getProperty(DCAT.landingPage).getObject().toString()))) {
							if (isValidURL(Dataset.getProperty(DCAT.landingPage).getObject().toString()))
								//Award 5 stars if we find a landingPage
								PublisherScore.put("PublisherLandingPage", 5);
						} 
					}
				}
			}
			
			//If LandingPage is not there then check for AccessURL in the distribution
			if(!(PublisherScore.containsKey("PublisherLandingPage"))) {
				
				ResIterator DistributionIterator = model.listSubjectsWithProperty(RDF.type, DCAT.accessURL);
				if (DistributionIterator.hasNext()) {
					while (DistributionIterator.hasNext()) {
						Resource Distribution = DistributionIterator.nextResource();
						if (Distribution.hasProperty(DCAT.accessURL)
								&& (!Distribution.getProperty(DCAT.accessURL).getObject().toString().isEmpty())) {
							if (PublisherScore.size() == 0 && !(PublisherScore
									.containsKey(Distribution.getProperty(DCAT.accessURL).getObject().toString()))) {
								if (isValidURL(Distribution.getProperty(DCAT.accessURL).getObject().toString()))
									//Award 5 stars if we find a landingPage
									PublisherScore.put("PublisherAccessURL", 5);
							} 
						}
					}
				}
			}
			
		}

		/*
		 * This section is for scoring
		 */

		if (PublisherScore.containsKey("InconsistentPublishers"))
			score = PublisherScore.get("InconsistentPublishers");
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