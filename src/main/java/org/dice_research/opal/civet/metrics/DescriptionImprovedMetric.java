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
 * order. If there is no description predicate, then check the same for
 * dct:title and rate the dataset.
 * 
 * Rating Criteria is as follows: "If dct:description contains <5 nouns, verbs
 * or adjectives then 1 star" + "else if dct:description contains >5 and <10
 * nouns, verbs or adjectives then 2 stars" + "else if dct:description contains
 * >10 and <15 nouns, verbs or adjectives then 3 stars" + "else if
 * dct:description contains >15 and <20 nouns, verbs or adjectives then 4 stars"
 * + "else if dct:description contains >20 nouns, verbs or adjectives then 5
 * stars"
 * 
 * @see:"https://opennlp.apache.org/docs/1.9.0/manual/opennlp.html"
 * @see:"http://csjarchive.cogsci.rpi.edu/Proceedings/2013/papers/0263/paper0263.pdf"
 * @see:"https://pressbooks.bccampus.ca/technicalwriting/chapter/importanceverbs/"
 * @see:"https://www.diva-portal.org/smash/get/diva2:231227/fulltext01.pdf"
 * 
 * @author Aamir Mohammed
 */

public class DescriptionImprovedMetric implements Metric {

	private static final String DESCRIPTION = "If dct:description contains <5 nouns, verbs or adjectives then 1 star"
			+ "else if dct:description contains >5 and <10 nouns, verbs or adjectives then 2 stars"
			+ "else if dct:description contains >10 and <15 nouns, verbs or adjectives then 3 stars"
			+ "else if dct:description contains >15 and <20 nouns, verbs or adjectives then 4 stars"
			+ "else if dct:description contains >20 nouns, verbs or adjectives then 5 stars"
			+ "else if no dct:description but dct:title then evaluate title and give stars";

	public int checkPartsOfSpeech(String rdfPredicate) throws IOException {

		// Loading Parts of speech-maxent model
		InputStream inputStream = new FileInputStream("src/main/resources/de-pos-maxent.bin");
		POSModel model = new POSModel(inputStream);

		// Instantiating POSTaggerME class
		POSTaggerME tagger = new POSTaggerME(model);

		// Tokenizing the sentence using WhitespaceTokenizer class
		WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;
		String[] tokens = whitespaceTokenizer.tokenize(rdfPredicate);

		// Generating tags
		String[] tags = tagger.tag(tokens);

		// Instantiating the POSSample class
		POSSample predicateTags = new POSSample(tokens, tags);
		String posTags = predicateTags.toString();

		// German parts of speech tags for noun, verb and adjective
		final String nounTag = "NN", verbTag = "VVFIN", adjectiveTag = "ADJA";

		// Counting occurrences of nouns
		int countNouns = 0;
		int lastIndexNoun = 0;

		while (lastIndexNoun != -1) {
			lastIndexNoun = posTags.indexOf(nounTag, lastIndexNoun);

			if (lastIndexNoun != -1) {
				countNouns++;
				lastIndexNoun += nounTag.length();
			}
		}

		// Counting occurrences of verbs
		int countVerbs = 0;
		int lastIndexVerb = 0;

		while (lastIndexVerb != -1) {
			lastIndexVerb = posTags.indexOf(verbTag, lastIndexVerb);

			if (lastIndexVerb != -1) {
				countVerbs++;
				lastIndexVerb += verbTag.length();
			}
		}

		// Counting occurrences of adjectives
		int countAdjectives = 0;
		int lastIndexAdjective = 0;

		while (lastIndexAdjective != -1) {
			lastIndexAdjective = posTags.indexOf(adjectiveTag, lastIndexAdjective);

			if (lastIndexAdjective != -1) {
				countAdjectives++;
				lastIndexAdjective += adjectiveTag.length();
			}
		}

		// Sum up all posTags in one variable to rate the metric
		int totalPosTags = countNouns + countVerbs + countAdjectives;

		// Now rating for the description is given
		if (totalPosTags <= 5) {
			// Very less posTags
			return 1;
		}

		else if (totalPosTags <= 10) {
			// less posTags
			return 2;
		}

		else if (totalPosTags <= 15) {
			// average posTags
			return 3;
		}

		else if (totalPosTags <= 20) {
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
		int score = 1;
		Resource dataset = model.createResource(datasetUri);
		if (!(dataset.hasProperty(DCTerms.description)) && (dataset.hasProperty(DCTerms.title))) {
			score = checkPartsOfSpeech(dataset.getProperty(DCTerms.title).getObject().toString());
		} else if (dataset.hasProperty(DCTerms.description)) {
			score = checkPartsOfSpeech(dataset.getProperty(DCTerms.description).getObject().toString());
		}
		return score;
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