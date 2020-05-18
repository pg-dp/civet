package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The Description metric compares dct:description and dct:title and gives the
 * stars.
 * 
 * 
 * @author Aamir Mohammed
 */
public class Description implements Metric {

	public static int compareDescWithTitle(String description, String title) {
		if (title.isEmpty() && description.isEmpty()) {
			return 0;
		} else if (title.isEmpty()) {
			return 1;
		} else if (description.isEmpty()) {
			return 1;
		} else if (description.equals(title)) {
			return 1;
		} else if (title.length() < 15) {
			return 1;
		} else if (description.length() <= 25) {
			return 2;
		} else if (description.length() <= 50) {
			return 3;
		} else if (description.length() <= 75) {
			return 4;
		} else {
			return 5;
		}
	}

	private static final Logger logger = LogManager.getLogger();
	private static final String descriptions = "";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		logger.info("Processing dataset " + datasetUri);
		Resource dataSet = model.createResource(datasetUri);

		if (model.isEmpty()) {
			return null;
		}
		int scores = 1;

		if (dataSet.hasProperty(DCTerms.description)
				&& !(dataSet.getProperty(DCTerms.description).getObject().toString().isEmpty())) {
			String dctdescription = dataSet.getProperty(DCTerms.description).getObject().toString();
			String dcttitle = dataSet.getProperty(DCTerms.title).getObject().toString();
			scores = compareDescWithTitle(dctdescription, dcttitle);
		}
		return scores;
	}

	@Override
	public String getDescription() {
		return descriptions;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_CATEGORIZATION.getURI();
	}

}