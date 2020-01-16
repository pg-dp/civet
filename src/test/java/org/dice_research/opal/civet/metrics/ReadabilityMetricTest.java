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
 * Tests {@link ReadabilityMetric}.
 */
public class ReadabilityMetricTest {

	TestData testdata;

	private static final String TEST_EDP_ICE = "Europeandataportal-Iceland.ttl";
	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	private static final String TEST_MODEL1080 = "model1080.ttl";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void testEdpIce() throws Exception {
		
		// Compute stars
		ReadabilityMetric metric = new ReadabilityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST_EDP_ICE_DATASET);

		Assert.assertEquals(TEST_EDP_ICE, null, stars);

		final String TEST_MODEL1080_DATASET = "http://projekt-opal.de/dataset/https___" +
				"europeandataportal_eu_set_data_designated_neighbourhood_plan_areas49";
		metric = new ReadabilityMetric();
		stars = metric.compute(testdata.getModel(TEST_MODEL1080), TEST_MODEL1080_DATASET);
		Assert.assertEquals(TEST_MODEL1080, 3, stars.intValue());
	}
}