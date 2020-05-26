
package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * For the Date Format metric, the following will be checked: Check, if the date
 * format is according to the W3c standards or not.
 * 
 * According to DCAT https://www.w3.org/TR/vocab-dcat-2/#bib-datetime the
 * correct formats are here: https://www.w3.org/TR/NOTE-datetime. All formats
 * have to be checked.
 * 
 * Here the implemented code checks the turtle file whether the given date
 * format is according to W3c standard or not, if it is of the standard format
 * it gives 5 stars else 0 star.
 * 
 * Finally, an average of the following 4 cases has to be computed for the final
 * score: dataset: issued + modified, distributions: issued + modified
 * 
 * @author Aamir Mohammed
 */
public class DateFormatMetric implements Metric {

	private static final String DESCRIPTION = "If the date format is in the correct format then give 5 stars else 0 star"
			+ "Finally, an average of the following 4 cases has to be computed for the finalscore: dataset: issued + modified, distributions: issued + modified";

	public static int checkDateFormat(String issued) {
		/*
		 * Correct date formats list w.r.t "w3.org". Checking different date formats
		 * using regular expression.
		 */
		if (issued.matches("\\d{4}-\\d{2}-\\d{2}")) {
			// "YYYY-MM-DD"
			return 5;
		} else if (issued.matches("\\d{4}")) {
			// "YYYY"
			return 5;
		} else if (issued.matches("\\d{4}-\\d{2}")) {
			// "YYYY-MM"
			return 5;
		} else if (issued.matches("\\d{4}-\\d{2}-\\d{2}[T]\\d{2}:\\d{2}:\\d{2}[+]\\d{2}:\\d{2}")) {
			// "YYYY-MM-DDThh:mm:ssTZD"
			return 5;
		} else if (issued.matches("\\d{4}-\\d{2}-\\d{2}[T]\\d{2}:\\d{2}[+]\\d{2}:\\d{2}")) {
			// "YYYY-MM-DDThh:mmTZD"
			return 5;
		} else if (issued.matches("\\d{4}-\\d{2}-\\d{2}[T]\\d{2}:\\d{2}:\\d{2}[.]\\d+[+]\\d{2}:\\d{2}")) {
			// "YYYY-MM-DDThh:mm:ss.sTZD"
			return 5;
		}
		return 0;
	}

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		int result = 0;
		int countIssued = 0;
		int countModified = 0;

		Resource dataSet = model.createResource(datasetUri);
		Boolean checkDctIssued = dataSet.hasProperty(DCTerms.issued);
		Boolean checkDctModified = dataSet.hasProperty(DCTerms.modified);

		// Checking issued and modified RDF property in DCTerms
		countIssued = checkDctIssued ? countIssued += 1 : countIssued;
		countModified = checkDctModified ? countModified += 1 : countModified;

		if (checkDctIssued && !(dataSet.getProperty(DCTerms.issued).getObject().toString().isEmpty())) {
			String dct_issued = dataSet.getProperty(DCTerms.issued).getObject().toString();
			result += checkDateFormat(dct_issued);
		}

		if (checkDctModified && !(dataSet.getProperty(DCTerms.modified).getObject().toString().isEmpty())) {
			String dateissued = dataSet.getProperty(DCTerms.modified).getObject().toString();
			result += checkDateFormat(dateissued);
		}

		StmtIterator distribution = model
				.listStatements(new SimpleSelector(dataSet, DCAT.distribution, (RDFNode) null));

		while (distribution.hasNext()) {
			Statement distributionSentence = distribution.nextStatement();
			Resource distDataSet = distributionSentence.getObject().asResource();
			Boolean checkDistIssued = distDataSet.hasProperty(DCTerms.issued);
			Boolean checkDistModified = distDataSet.hasProperty(DCTerms.modified);

			// Checking issued and modified RDF property in DCAT distributions
			countIssued = checkDistIssued ? countIssued += 1 : countIssued;
			countModified = checkDistModified ? countModified += 1 : countModified;

			if (checkDistIssued && !(distDataSet.getProperty(DCTerms.issued).getObject().toString().isEmpty())) {
				String dateissued = distDataSet.getProperty(DCTerms.issued).getObject().toString();
				result += checkDateFormat(dateissued);
			}

			if (checkDistModified && !(distDataSet.getProperty(DCTerms.modified).getObject().toString().isEmpty())) {
				String dateissued = distDataSet.getProperty(DCTerms.modified).getObject().toString();
				result += checkDateFormat(dateissued);
			}
		}
		return result / (countIssued + countModified);
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_DATE_FORMAT.getURI();
	}

}