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
 * Tests {@link KnownLicenseMetric}.
 * 
 * @author Adrian Wilke
 */
public class DataFormatMetricTest {

	TestData testdata;

	// Publisher with empty blank node
	private static final String TestCase1 = "TestCaseDataFormat5stars.ttl";

	// Publisher with non empty blank node
	private static final String TestCase2 = "TestCaseDataFormat2stars.ttl";

	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void TestCase1() throws Exception {

		DataFormatMetric metric = new DataFormatMetric();

		// Compute stars : TestCase1 check for 5 stars --------> MUST PASS
		Integer stars_test = metric.compute(testdata.getModel(TestCase1), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Data Format Test: All distributions have valid file format", 5, stars_test.intValue());
	}
	
	@Test
	public void TestCase2() throws Exception {

		DataFormatMetric metric = new DataFormatMetric();

		// Compute stars : TestCase2 check for 5 stars, but it is actually a 2 star dataset --------> MUST FAIL
		Integer stars_test = metric.compute(testdata.getModel(TestCase2), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Data Format Test: Out of 2 distributions, only 1 has valid file format", 5, stars_test.intValue());
	}

}