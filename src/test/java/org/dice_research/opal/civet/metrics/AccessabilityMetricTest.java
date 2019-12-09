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
 * Tests {@link Accessibility}.
 * 
 * This files contain testcases to perform Unit testing on Accessibility.java 
 * 
 * @author Amit Kumar
 */
public class AccessabilityMetricTest {

	TestData testdata;

	private static final String TEST_EDP_ICE = "model4881.ttl";
	
//	It should return a status code 200
	private static final String TEST_DATASET_RATING5_POSITIVE = "http://projekt-opal.de/distribution/https___europeandataportal_eu_set_distribution_aaa86820_459c_4636_9cc4_5c55236fc898";
	
//	It should return a status code 200
	private static final String TEST_DATASET_RATING5_NEGATIVE = "http://projekt-opal.de/distribution/https___europeandataportal_eu_set_distribution_d70fadc9_d37f_409f_b9f7_43277841a237";
	
//	It should return status code 301: “The requested resource has been moved permanently.” 
	private static final String TEST_DATASET_STATUS301 = "http://projekt-opal.de/distribution/https___europeandataportal_eu_set_distribution_4456ca73_dfec_4da7_9b81_7bbec4ad8882";
	
//	It should return status code 500: “There was an error on the server and the request could not be completed.”	
	private static final String TEST3_DATASET_STATUS500 = "http://projekt-opal.de/distribution/https___europeandataportal_eu_set_distribution_c2a31c1d_3e1f_456d_9798_1f3dfa915791";
	
//	It should return status code 400: Bad Request
	private static final String TEST4_DATASET_STATUS400 = "http://projekt-opal.de/distribution/https___europeandataportal_eu_set_distribution_b627ee7c_9ac6_4462_85b1_c6eee1526fd5";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void testRating5Response200() throws Exception {
		// Compute stars
		AccessibilityMetric metric = new AccessibilityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST_DATASET_RATING5_POSITIVE);
		Assert.assertEquals(TEST_EDP_ICE, 5, stars.intValue());
	}
	
//	This JUnit test should fail. It expects a value 0 but receives 5.
	@Test
	public void testRating0Response200() throws Exception {
		// Compute stars
		AccessibilityMetric metric = new AccessibilityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST_DATASET_RATING5_NEGATIVE);
		Assert.assertEquals(TEST_EDP_ICE, 0, stars.intValue());
	}
	
	@Test
	public void testRating5Response301() throws Exception {
		// Compute stars
		AccessibilityMetric metric = new AccessibilityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST_DATASET_STATUS301);
		Assert.assertEquals(TEST_EDP_ICE, 5, stars.intValue());
	}
	
	@Test
	public void testStatus500() throws Exception {
		// Compute stars
		AccessibilityMetric metric = new AccessibilityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST3_DATASET_STATUS500);
		Assert.assertEquals(TEST_EDP_ICE, 1, stars.intValue());
	}
	
	@Test
	public void testStatus400() throws Exception {
		AccessibilityMetric metric = new AccessibilityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST4_DATASET_STATUS400);
		Assert.assertEquals(TEST_EDP_ICE, 2, stars.intValue());
	}

}