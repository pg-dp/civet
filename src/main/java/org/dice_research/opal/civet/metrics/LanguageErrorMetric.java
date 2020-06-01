package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.DCTerms;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

import java.util.List;

public class LanguageErrorMetric implements Metric {

    private static final String DESCRIPTION = "Computes a score based on the description " +
            "given and how well the description is given";

    @Override
    public Integer compute(Model model, String datasetUri) throws Exception {
        Resource dataset = ResourceFactory.createResource(datasetUri);

        //TODO check the tag at the description and evaluate the for the language accordingly
        //Could be @en , @fr , @de, @es

        JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());

        int score = 0;
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
        return Opal.OPAL_METRIC_LANGUAGE_ERRORS.getURI();
    }

}