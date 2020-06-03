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
import org.languagetool.language.French;
import org.languagetool.language.German;
import org.languagetool.language.GermanyGerman;
import org.languagetool.rules.RuleMatch;

import java.util.ArrayList;
import java.util.List;

public class LanguageErrorMetric implements Metric {

    private static final String DESCRIPTION = "Computes a score based on the description " +
            "given and how well the description is given";

    @Override
    public Integer compute(Model model, String datasetUri) throws Exception {

        Resource dataset = ResourceFactory.createResource(datasetUri);
        JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());

        int score = 0;

//        if (!dataset.hasProperty(DCTerms.description))
//            return null;

        Statement statement = model.getProperty(dataset, DCTerms.description);
        String dct_description = statement.getObject().toString();
        String[] wordsCount = dct_description.split(" ");
        int wordsCountLength = wordsCount.length;

        if (dct_description.contains("@en"))
            langTool = new JLanguageTool(new AmericanEnglish());
        else if (dct_description.contains("@fr"))
            langTool = new JLanguageTool(new French());
        else if (dct_description.contains("@de"))
            langTool = new JLanguageTool(new GermanyGerman());

        List<RuleMatch> matches = langTool.check(dct_description);

        for (RuleMatch match : matches) {
            System.out.println("Potential error at characters " +
                    match.getFromPos() + "-" + match.getToPos() + ": " +
                    match.getMessage());
            System.out.println("Suggested correction(s): " +
                    match.getSuggestedReplacements());
        }

        int errorPerDescription = wordsCountLength / matches.size();

        if (errorPerDescription > 0.9)
            score = 0;
        else if (errorPerDescription > 0.75)
            score = 1;
        else if (errorPerDescription > 0.5)
            score = 2;
        else if (errorPerDescription > 0.25)
            score = 3;
        else if (errorPerDescription > 0.05)
            score = 4;
        else if (errorPerDescription > 0)
            score = 5;

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