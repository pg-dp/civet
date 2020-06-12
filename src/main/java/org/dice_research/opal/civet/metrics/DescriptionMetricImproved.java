package org.dice_research.opal.civet.metrics;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

/**
 * The Description metric - Improved version checks the RDF property
 * dct:description. If the dct:description contains more number nouns, verbs and
 * adjectives then high stars are awarded, and less are awarded in a descending
 * order.
 * 
 * Rating Criteria is as follows: "If dct:description contains <5 nouns, verbs
 * and adjectives then 1 star" + "else if dct:description contains >5 and <10
 * then 2 stars" + "else if dct:description contains >10 and <15 then 3 stars" +
 * "else if dct:description contains >15 and <20 then 4 stars" + "else if
 * dct:description contains >20 then 5 stars"
 * 
 * @Reference:"https://opennlp.apache.org/docs/1.9.0/manual/opennlp.html"
 * @Reference:"http://csjarchive.cogsci.rpi.edu/Proceedings/2013/papers/0263/paper0263.pdf"
 * @Reference:"https://pressbooks.bccampus.ca/technicalwriting/chapter/importanceverbs/"
 * @Reference:"https://www.diva-portal.org/smash/get/diva2:231227/fulltext01.pdf"
 * 
 * @author Aamir Mohammed
 */

public class DescriptionMetricImproved implements Metric {

	private static final String DESCRIPTION = "If dct:description contains <5 nouns, verbs and adjectives then 1 star"
			+ "else if dct:description contains >5 and <10 then 2 stars"
			+ "else if dct:description contains >10 and <15 then 3 stars"
			+ "else if dct:description contains >15 and <20 then 4 stars"
			+ "else if dct:description contains >20 then 5 stars";

	// German parts of speech tags for noun, verb and adjective
	private static String nounTag = "NN";
	private static String verbTag = "VVFIN";
	private static String adjectiveTag = "ADJA";
	private int lastIndex = 0;
	private int countNouns = 0;
	private int countVerbs = 0;
	private int countAdjectives = 0;

	public int posTagger(String dct_description) throws IOException {

		// Loading Parts of speech-maxent model
		InputStream inputStream = new FileInputStream("src/main/resources/de-pos-maxent.bin");
		POSModel model = new POSModel(inputStream);

		// Instantiating POSTaggerME class
		POSTaggerME tagger = new POSTaggerME(model);

		// Tokenizing the sentence using WhitespaceTokenizer class
		WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;
		String[] tokens = whitespaceTokenizer.tokenize(dct_description);

		// Generating tags
		String[] tags = tagger.tag(tokens);

		// Instantiating the POSSample class
		POSSample description_tags = new POSSample(tokens, tags);
		String pos_tags = description_tags.toString();

		// Counting occurrences of nouns
		while (lastIndex != -1) {
			lastIndex = pos_tags.indexOf(nounTag, lastIndex);

			if (lastIndex != -1) {
				countNouns++;
				lastIndex += nounTag.length();
			}
		}

		// Counting occurrences of verbs
		while (lastIndex != -1) {
			lastIndex = pos_tags.indexOf(verbTag, lastIndex);

			if (lastIndex != -1) {
				countVerbs++;
				lastIndex += verbTag.length();
			}
		}

		// Counting occurrences of adjectives
		while (lastIndex != -1) {
			lastIndex = pos_tags.indexOf(adjectiveTag, lastIndex);

			if (lastIndex != -1) {
				countAdjectives++;
				lastIndex += adjectiveTag.length();
			}
		}

		// Sum up all three posTags in one variable to rate metrics
		int allPosTags = countNouns + countVerbs + countAdjectives;

		// Now rating for the description is given
		if (allPosTags <= 5) {
			// Very less posTags
			return 1;
		}

		else if (allPosTags > 5 && allPosTags <= 10) {
			// less posTags
			return 2;
		}

		else if (allPosTags > 10 && allPosTags <= 15) {
			// average posTags
			return 3;
		}

		else if (allPosTags > 15 && allPosTags <= 20) {
			// above average posTags
			return 4;
		}

		else {
			// Best use of posTags
			return 5;
		}
	}

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		int scores = 1;
		Resource dataset = model.createResource(datasetUri);
		if (dataset.hasProperty(DCTerms.description)) {
			String dct_description = dataset.getProperty(DCTerms.description).getObject().toString();
			scores = posTagger(dct_description);
			return scores;
		}
		return scores;
	}

	@Override
	public String getDescription() throws Exception {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_DESCRIPTION.getURI();
	}

}