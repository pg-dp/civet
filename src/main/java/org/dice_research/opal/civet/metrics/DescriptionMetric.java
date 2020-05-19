package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The Description metric compares two RDF (Resource Description Framework)
 * properties dct:description, dct:title and gives the stars based on the
 * information it provides.
 * 
 * Rating Criteria is as follows: "If there is empty dct:description and
 * dct:title then give 0 star"+ "If there is dct:description and dct:title empty
 * then give 1 star"+ "If there is dct:title and dct:description empty then give
 * 1 star"+ "If there is equal length of both dct:description and dct:title then
 * give 1 star"+ "If length of dct:title is less than 15 then give 1 star"+ "If
 * length of dct:description is less than or equal to 25 then give 2 star"+ "If
 * length of dct:description is less than or equal to 50 then give 3 star"+ "If
 * length of dct:description is less than or equal to 75 then give 4 star"+ "If
 * length of dct:description is more than 75 then give 5 star"
 * 
 * @author Aamir Mohammed
 */
public class DescriptionMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTIONS = "If there is empty dct:description and dct:title then give 0 star"
			+ " If there is dct:description and dct:title empty then give 1 star"
			+ " If there is dct:title and dct:description empty then give 1 star"
			+ " If there is equal length of both dct:description and dct:title then give 1 star"
			+ " If length of dct:title is less than 15 then give 1 star"
			+ " If length of dct:description is less than or equal to 25 then give 2 stars"
			+ " If length of dct:description is less than or equal to 50 then give 3 stars"
			+ " If length of dct:description is less than or equal to 75 then give 4 stars"
			+ " If length of dct:description is more than 75 then give 5 star";

	public static int compareDescriptionWithTitle(String description, String title) {
		/*
		 * In this function, it compares dct:title and dct:description according to the
		 * conditions given in statements
		 * 
		 */

		if (title.isEmpty() && description.isEmpty()) {
			// No title and description
			return 0;
		}

		else if (title.isEmpty() && !(description.isEmpty())) {
			// Atleast description
			return 1;
		}

		else if (!(title.isEmpty()) && description.isEmpty()) {
			// Atleast description
			return 1;
		}

		else if (description.equals(title)) {
			// Bad usage of text
			return 1;
		}

		else if (title.length() < 15) {
			// Bad title
			return 1;
		}

		else if (description.length() > 15 && description.length() <= 25) {
			// Below average description
			return 2;
		}

		else if (description.length() > 25 && description.length() <= 50) {
			// Average description
			return 3;
		}

		else if (description.length() > 50 && description.length() <= 75) {
			// Above average description
			return 4;
		}

		else {
			// Good description
			return 5;
		}
	}

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		LOGGER.info("Processing dataset " + datasetUri);
		Resource dataset = model.createResource(datasetUri);
		int scores = 1;
		if (dataset.hasProperty(DCTerms.title) && !(dataset.hasProperty(DCTerms.description))) {
			return scores;
		} else if (dataset.hasProperty(DCTerms.title) && (dataset.hasProperty(DCTerms.description))) {
			String dct_description = dataset.getProperty(DCTerms.description).getObject().toString();
			String dct_title = dataset.getProperty(DCTerms.title).getObject().toString();
			scores = compareDescriptionWithTitle(dct_description, dct_title);
			return scores;
		}
		return scores;
	}

	@Override
	public String getDescription() {
		return DESCRIPTIONS;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_CATEGORIZATION.getURI();
	}

}