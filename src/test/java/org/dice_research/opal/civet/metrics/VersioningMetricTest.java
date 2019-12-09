package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link VersionMetric}.
 */
public class VersioningMetricTest {

	TestData testdata;

	private static final String TEST_GOV_AMH = "Govdata-Allermoehe.ttl";
	private static final String TEST_GOV_AMH_DATASET =
			"http://projekt-opal.de/dataset/https___" +
					"ckan_govdata_de_001c9703_3556_4f62_a376_85804f18ab52";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	/**
	 * Tests all 3 cases.
	 */
	@Test
	public void testCases() throws Exception {
		VersionMetric metric = new VersionMetric();

		Model model = ModelFactory.createDefaultModel();
		String datasetUri = "https://example.org/dataset-1";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.dataset);

		Assert.assertEquals("No Version Info", null,
				metric.compute(model, datasetUri));

//		model.addLiteral(dataset, DCAT.distribution, ResourceFactory.createPlainLiteral
//				("version5"));
//		Assert.assertEquals("Version Found In a Property", 5,
//				metric.compute(model, datasetUri).intValue());
//
//		model.addLiteral(dataset, DCAT.distribution, ResourceFactory.createPlainLiteral
//				("version4"));
//		Assert.assertEquals("Version Found Through ConformsTo Property", 4,
//				metric.compute(model, datasetUri).intValue());
//
//		model.addLiteral(dataset, DCAT.distribution, ResourceFactory.createPlainLiteral
//				("version3"));
//		Assert.assertEquals("Version Found through Access/download Url", 3,
//				metric.compute(model, datasetUri).intValue());

	}


	@Test
	public void testEdpIce() throws Exception {
		
		// Compute stars
		VersionMetric metric = new VersionMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_GOV_AMH),
				TEST_GOV_AMH_DATASET);

		//Expected 3
		Assert.assertEquals(TEST_GOV_AMH, 3, stars.intValue());
	}

}