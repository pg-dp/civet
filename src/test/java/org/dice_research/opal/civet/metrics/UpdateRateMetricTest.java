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
 * Tests {@link UpdateRateMetric}.
 */
public class UpdateRateMetricTest {

	TestData testdata;

	private static final String TEST_EDP_ICE = "Europeandataportal-Iceland.ttl";
	private static final String TEST_EDP_ICE_DATASET =
			"http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void testCases() throws Exception {
		UpdateRateMetric metric = new UpdateRateMetric();

		Model model = ModelFactory.createDefaultModel();
		String datasetUri = "https://example.org/dataset-1";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.Dataset);

		Assert.assertEquals("No Update Rate", null, metric.compute(model, datasetUri));
	}

	@Test
	public void testEdpIce() throws Exception {
		
		// Compute stars
		UpdateRateMetric metric = new UpdateRateMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST_EDP_ICE_DATASET);
		Assert.assertEquals(TEST_EDP_ICE, 5, stars.intValue());

		//TODO test Cases for null, 0,1,2,3,4 Stars
	}

}