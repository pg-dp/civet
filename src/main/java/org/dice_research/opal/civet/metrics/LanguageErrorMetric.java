package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCTerms;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.language.BritishEnglish;
import org.languagetool.language.French;
import org.languagetool.language.GermanyGerman;
import org.languagetool.rules.RuleMatch;

import java.util.List;
/**
 * Checks the grammar of the description of the model and rate the model accordingly .
 * JLANGTOOl lib is used for the grammar checking .
 * 3 Languages are checked as of now ,
 * but any number of languages can be added using this library.
 * @author Vikrant Singh
        */

public class LanguageErrorMetric implements Metric {

    private static final String DESCRIPTION = "Computes a score based on the description " +
            "given and how well the description is given," +
            " Works for english , german and french , " +
            "support for other languages could be added easily";

    @Override
    public Integer compute(Model model, String datasetUri) throws Exception, LiteralRequiredException {

        Resource dataset = ResourceFactory.createResource(datasetUri);
        JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());
        String language = " ";
        int score = 0;

        Statement statement = model.getProperty(dataset, DCTerms.description);
        if (statement == null) {
            return null;
        }

        NodeIterator languageIterator = model.listObjectsOfProperty(dataset, DCTerms.description);

        while (languageIterator.hasNext()) {
            RDFNode languageNode = languageIterator.next();
            if (languageNode.isLiteral())
                language = languageNode.asLiteral().getLanguage();
            else
                return null;
        }

        String dct_description = statement.getObject().toString();
        dct_description.replaceAll(".", " ");
        String[] wordsCount = dct_description.split(" ");
        double wordsCountLength = wordsCount.length;

        switch (language.toLowerCase()) {
            case "en":
                langTool = new JLanguageTool(new BritishEnglish());
                break;
            case "fr":
                langTool = new JLanguageTool(new French());
                break;
            case "de":
                langTool = new JLanguageTool(new GermanyGerman());
                break;
        }

        List<RuleMatch> matches = langTool.check(dct_description);
        double errorCount = matches.size();
        double errorPerDescription = errorCount / wordsCountLength;

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
        else if (errorPerDescription >= 0)
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