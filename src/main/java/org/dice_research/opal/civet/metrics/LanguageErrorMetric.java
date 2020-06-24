package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCTerms;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.language.French;
import org.languagetool.language.GermanyGerman;
import org.languagetool.rules.RuleMatch;

import java.util.List;

/**
 * Checks the grammar of the description of the model and score the model accordingly.
 * JLANGTOOl lib is used for the grammar checking.
 * {@see https://languagetool.org/development/api/index.html?org/languagetool/JLanguageTool.html}
 * 3 Languages are checked as of now, which are English, German and French,
 * but any number of languages can be added using this library.
 * If the model has some kind of description with the language tag,
 * it has given 1 star else null with no description or with no language Tag
 * Scoring Scheme is calculated as error per description length. Refer {@code scoringScheme}
 * for the further analysis of the scoring distribution
 *
 * @author Vikrant Singh
 */

public class LanguageErrorMetric implements Metric {

    private static final String DESCRIPTION = "Computes a score based on the description " +
            "given and how well the description is given," +
            " Works for English, German and French " +
            "support for other languages could be added easily";

    private static final JLanguageTool langToolEnglish = new JLanguageTool(new AmericanEnglish()) ;
    private static final JLanguageTool langToolGerman = new JLanguageTool(new GermanyGerman());
    private static final JLanguageTool langToolFrench = new JLanguageTool(new French());


    @Override
    public Integer compute(Model model, String datasetUri) throws Exception {
        Resource dataset = ResourceFactory.createResource(datasetUri);
        String language ;
        String description ;
        int score;

        Statement statement = model.getProperty(dataset, DCTerms.description);
        if (statement == null) {
            return null;
        }

        RDFNode languageNode = statement.getObject();
        if (languageNode.isLiteral()) {
            language = languageNode.asLiteral().getLanguage();
            description = languageNode.asLiteral().getString();
        }
        else
            return null;

        String[] wordsCount = description.split(" ");
        double wordsCountLength = wordsCount.length;

        //makes the JLangTool Object for a particular Language
        List<RuleMatch> matches;

        switch (language.toLowerCase()) {
            case "en":
                matches = langToolEnglish.check(description);
                break;
            case "fr":
                matches = langToolFrench.check(description);
                break;
            case "de":
                matches = langToolGerman.check(description);
                break;
            default:
                return null;
        }

        double errorCount = matches.size();
        double errorPerDescription = errorCount / wordsCountLength;

        score = scoringScheme(errorPerDescription);
        return score;
    }

    private int scoringScheme(double errorCount) {
        //calculates the score
        int score = 0;

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
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getUri() {
        return Opal.OPAL_METRIC_LANGUAGE_ERRORS.getURI();
    }
}