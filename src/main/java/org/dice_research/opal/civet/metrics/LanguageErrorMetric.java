package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;
import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.RuleMatch;

import java.util.List;

public class LanguageErrorMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes language errors";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);
		
		JLanguageTool langTool = new JLanguageTool(new BritishEnglish());

		List<RuleMatch> matches = langTool.check(
				"A sentence with a error in the Hitchhiker's Guide tot he Galaxy");
		for (RuleMatch match : matches) {
			System.out.println("Potential error at characters " +
					match.getFromPos() + "-" + match.getToPos() + ": " +
					match.getMessage());
			System.out.println("Suggested correction(s): " +
					match.getSuggestedReplacements());
		}

		return null ;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_LANGUAGE_ERRORS.getURI();
	}

}