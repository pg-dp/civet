package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCTerms;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;
import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.language.French;
import org.languagetool.language.GermanyGerman;
import org.languagetool.rules.RuleMatch;

import java.util.List;
/**
 * Checks the grammar of the description of the model and rate the model accordingly .
 * JLANGTOOl lib is used for the grammar checking .
 * 3 Languages are checked as of now  which are English , German and French,
 * but any number of languages can be added using this library.
 * If the model has some kind of description with the language tag ,
 * it has given 1 star else null with no description or no language Tag
 * Scoring Scheme is calculated as error per description length. Refer {@code scoringScheme}
 * for the further analysis of the scoring distribution
 * @author Vikrant Singh
 */

public class LanguageErrorMetric implements Metric {

    private static final String DESCRIPTION = "Computes a score based on the description " +
            "given and how well the description is given," +
            " Works for english , german and french " +
            "support for other languages could be added easily";

    @Override
    public Integer compute(Model model, String datasetUri) throws Exception, LiteralRequiredException {

        Resource dataset = ResourceFactory.createResource(datasetUri);
        JLanguageTool langTool ;
        String language = " ";
        int score  ;

        Statement statement = model.getProperty(dataset, DCTerms.description);
        if (statement == null) {
            return null;
        }

        String description = statement.getObject().toString();
        String[] wordsCount = description.split(" ");
        double wordsCountLength = wordsCount.length;

        NodeIterator languageIterator = model.listObjectsOfProperty(dataset, DCTerms.description);

        //get the language from language Tag
        while (languageIterator.hasNext()) {
            RDFNode languageNode = languageIterator.next();
            if (languageNode.isLiteral())
                language = languageNode.asLiteral().getLanguage();
            else
                return null;
        }

        //makes the JLangTool Object for a particular Language
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
            default:
                return  null ;
        }

        //makes a list of errors
        List<RuleMatch> matches = langTool.check(description);
        double errorCount = matches.size();
        double errorPerDescription = errorCount / wordsCountLength;

        score = scoringScheme(errorPerDescription);
        return score;
    }

    private int scoringScheme(double errorCount) {
        //calculates the score
        int score = 0 ;

        if (errorCount > 0.75)
            score = 1;
        else if (errorCount > 0.5)
            score = 2;
        else if (errorCount > 0.25)
            score = 3;
        else if (errorCount > 0.05)
            score = 4;
        else if (errorCount >= 0)
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