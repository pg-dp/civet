package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

import java.util.List;

/**
 * The CategorizationMetric awards stars based on the number of keywords and
 * themes of a dataset.
 * 
 * Keywords/tags are literals. Themes/categories should be of type skos:Concept.
 * 
 * @author Adrian Wilke
 */
public class LanguageErrorMetric implements Metric {

	private static final String DESCRIPTION = "Computes a score based on the description " +
			"given and how well the description is given";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		Resource dataset = ResourceFactory.createResource(datasetUri);
		JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());

		int score = 0 ;
		// Count keywords and check types
		Statement statement = model.getProperty(dataset, DCTerms.description);
		List<RuleMatch> matches = langTool.check(String.valueOf(statement));

		for (RuleMatch match : matches) {
			System.out.println("Potential error at characters " +
					match.getFromPos() + "-" + match.getToPos() + ": " +
					match.getMessage());
			System.out.println("Suggested correction(s): " +
					match.getSuggestedReplacements());
		}

		return score;
	}

	@Override
	public String getDescription() throws Exception {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_CATEGORIZATION.getURI();
	}

}