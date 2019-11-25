package org.dice_research.opal.civet.metrics;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The CategorizationMetric awards stars based on the number of keywords of a
 * dataset.
 * 
 * @author Adrian Wilke
 */
public class DataFormatMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Check if all distributions in a dataset has valid format and accordingly award a score "
            + "Check if dct:format keyword with respective object present. If present then check if it is of valid format(IANA File format) like XML or PDF or CSV etc. If \"YES\", then award 5 stars to the distribution "
			+" Check if dct:format keyword with respective object present. If present then check if it is of valid URI format as prescribed in \"https://op.europa.eu/en/web/eu-vocabularies/at-dataset/-/resource/dataset/file-type?target=About\"."
			+ "If object is of above type, then award 5 stars.  "
			+" If dct:format keyword with respective object present(Object is not empty for sure) but it does not match any file format. Then award 4 stars for not following recommended procedures."
			+ "If dct:format keyword is absent then check dcat:mediaType in the distribution: this procedure is same as dct:format "
			+ "If both dct:format and dcat:keyword are not there in distributions or even if they are present but the respective objects are empty(Like empty String) then give \"0 Stars\".";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		// Resource dataset = ResourceFactory.createResource(datasetUri);

		// NodeIterator nodeIterator = model.listObjectsOfProperty(dataset,
		// DCAT.keyword);

		// Total number of distributions in a dataset.
		int TotalDistributions = 0;

		// Store a score for each distribution, we will use it for final evaluation.
		HashMap<String, Integer> DistributionsAndScores = new HashMap<String, Integer>();

		// URIs for filetype
		ArrayList<String> UriFileType = new ArrayList<String>();
		// Strings for filetype
		ArrayList<String> StringFileType = new ArrayList<String>();

		// Model with file type information
		Model ModelFileType = ModelFactory.createDefaultModel();
		/*
		 * To evaluate file format, we will use an RDF file "filetypes-skos.ttl" provided by 
		 * https://op.europa.eu/en/web/eu-vocabularies/at-dataset/-/resource/dataset/file-type?target=About.
		 * This turtle file contains information about all kinds of valid file formats.
		 */
		ModelFileType.read(getClass().getClassLoader().getResource("filetypes-skos.ttl").getFile(), "TURTLE");

		// Property for Skos and PreferredLabel(we will extract string file type from
		// here)
		Property SkosConcept = ModelFileType.createProperty("http://www.w3.org/2004/02/skos/core#Concept");
		Property PrefLabel = ModelFileType.createProperty("http://www.w3.org/2004/02/skos/core#prefLabel");

		// FillUp UriFileType and StringFileType
		StmtIterator iterator = ModelFileType.listStatements(null, null, (RDFNode) SkosConcept);
		while (iterator.hasNext()) {
			Statement ST = iterator.nextStatement();
			UriFileType.add(ST.getSubject().toString().toLowerCase());
			StringFileType.add(ST.getSubject().getProperty(PrefLabel).getObject().toString().split("@")[0]);
		}

		/*
		 * A dataset can have many distributions. So first check each distributions for
		 * object of dct:format if none then check dcat:mediatype
		 */
		NodeIterator DistributionsIterator = model.listObjectsOfProperty(DCAT.distribution);

		if (DistributionsIterator.hasNext()) {

			while (DistributionsIterator.hasNext()) {

				// Transform distribution objects into distribution resource
				Resource Distribution = (Resource) DistributionsIterator.nextNode();
				if (Distribution.hasProperty(DCTerms.format)) {
					// If file type is of valid URI then the respective distribution is awarded 5
					// stars
					if (UriFileType.contains(Distribution.getProperty(DCTerms.format).getObject().toString()))
						DistributionsAndScores.put(Distribution.toString(), 5);
					// If file type is of valid string format like CSV or PDF then respective
					// distribution is awarded 5 stars
					else if (StringFileType
							.contains(Distribution.getProperty(DCTerms.format).getObject().toString().toUpperCase()))
						DistributionsAndScores.put(Distribution.toString(), 5);
					// Else 4 stars are awarded for not following the standard procedure
					else if (!(Distribution.getProperty(DCTerms.format).getObject().toString().isEmpty()))
						DistributionsAndScores.put(Distribution.toString(), 4);
					// In this case either the dct:format is not there or the object value is empty
					// then 0 star is given
					else if (Distribution.getProperty(DCTerms.format).getObject().toString().isEmpty())
						DistributionsAndScores.put(Distribution.toString(), 0);
				}
				/*
				 * This elseif will be executed when dct:format is absent or it is present but
				 * it's object is empty
				 */
				else if (!Distribution.hasProperty(DCTerms.format)
						|| DistributionsAndScores.get(Distribution.toString()) == 0) {

					// Then check if the distribution has property Dcat:mediaType
					if (Distribution.hasProperty(DCAT.mediaType)) {
						// Check If file type is of valid URI then the respective distribution is
						// awarded 5 stars
						if (UriFileType.contains(Distribution.getProperty(DCAT.mediaType).getObject().toString()))
							DistributionsAndScores.put(Distribution.toString(), 5);
						// If file type is of valid string format like CSV or PDF then respective
						// distribution is awarded 5 stars
						else if (StringFileType.contains(
								Distribution.getProperty(DCAT.mediaType).getObject().toString().toUpperCase()))
							DistributionsAndScores.put(Distribution.toString(), 5);
						// Check if they are in this format, "shp"^^dct:MediaTypeOrExtent add 5 star if
						// valid file type found
						else if (StringFileType.contains(Distribution.getProperty(DCAT.mediaType).getObject()
								.asLiteral().getString().toUpperCase())) {
							DistributionsAndScores.put(Distribution.toString(), 5);
						}
						// Check if they are in this format, text/csv(As seen in DCAT page)---> add 5
						// star if valid file type found
						else if (Distribution.getProperty(DCAT.mediaType).getObject().toString().contains("/")) {
							if (StringFileType.contains(
									Distribution.getProperty(DCAT.mediaType).getObject().toString().split("/")[0]))
								DistributionsAndScores.put(Distribution.toString(), 5);
							else if (StringFileType.contains(
									Distribution.getProperty(DCAT.mediaType).getObject().toString().split("/")[1]
											.toUpperCase()))
								DistributionsAndScores.put(Distribution.toString(), 5);
							// In last resort, check if dcat:mediatype not empty and give 4 stars for not
							// following recommended file type
							else if (!(Distribution.getProperty(DCAT.mediaType).getObject().toString().isEmpty()))
								DistributionsAndScores.put(Distribution.toString(), 4);
							// If object is an empty string then give 0 stars
							else if (Distribution.getProperty(DCAT.mediaType).getObject().toString().isEmpty())
								DistributionsAndScores.put(Distribution.toString(), 0);
						}

					}
				}
				// If both dct:format and dcat:mediatype are absent then give 0 stars to the
				// distribution
				else if (!(Distribution.hasProperty(DCTerms.format)) && !(Distribution.hasProperty(DCAT.mediaType)))
					DistributionsAndScores.put(Distribution.toString(), 0);

				// To calculate how many distributions in a dataset. It will be used for
				// scoring.
				TotalDistributions++;
			}
		}

		/**
		 * Score Evaluation: Total Number of distributions in Dataset = x Total
		 * aggregated scores of all distributions = y Overall Score = y/x
		 */
		int AggregatedScoreOfAllDistributions = 0;
		int OverallScore = 0;
		for (String key : DistributionsAndScores.keySet()) {
			AggregatedScoreOfAllDistributions += DistributionsAndScores.get(key);
			System.out.println(key + ":" + DistributionsAndScores.get(key));
		}

		OverallScore = AggregatedScoreOfAllDistributions / TotalDistributions;

		return OverallScore;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_CATEGORIZATION.getURI();
	}

}