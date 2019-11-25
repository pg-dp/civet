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
public class ProviderIdentityMetricTest {

	TestData testdata;

	// Publisher with empty blank node
	private static final String TestCase1 = "TestCaseforProviderIdentityEmptyBlanknode.ttl";

	// Publisher with non empty blank node
	private static final String TestCase2 = "TestCaseforProviderIdentityNonemptyBlanknode.ttl";

	// Publishers with non-empty blanknodes with consistent information
	private static final String TestCase3 = "TestCaseforProviderIdentityNonemptyBlanknodeConsistentProviders.ttl";

	// Publishers with non-empty blanknodes with inconsistent information
	private static final String TestCase4 = "TestCaseforProviderIdentityNonemptyBlanknodeInconsistentProviders.ttl";

	// Publishers with inconsistent providers
	private static final String TestCase5 = "TestCaseforProviderIdentityInconsistentProviders.ttl";

	// 1 consistent provider
	private static final String TestCase6 = "TestCaseforProviderIdentity1consistentProvider.ttl";

	// 1 consistent provider but no FOAF:Agent
	private static final String TestCase7 = "TestCaseforProviderIdentity4StarsNoFoafAgent.ttl";

	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void TestCase1() throws Exception {

		ProviderIdentityMetric metric = new ProviderIdentityMetric();

		// Compute stars : TestCase1 check for 1 star --------> MUST Pass
		Integer stars_test1 = metric.compute(testdata.getModel(TestCase1), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: One Blank Node", 5, stars_test1.intValue());
	}

	@Test
	public void TestCase2() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		// Compute stars : TestCase2 check for 5 stars --------> MUST PASS
		Integer stars_test2 = metric.compute(testdata.getModel(TestCase2), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: One NonEmpty Blank Node with Info", 5, stars_test2.intValue());
	}

	@Test
	public void TestCase3() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		// Compute stars : TestCase3 check for 5 stars --------> MUST PASS
		Integer stars_test3 = metric.compute(testdata.getModel(TestCase3), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: More than One NonEmpty Blank Node with Consistent Info", 5,
				stars_test3.intValue());
	}

	@Test
	public void TestCase4() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		// Compute stars : TestCase4 check for 0 stars --------> MUST PASS
		Integer stars_test4 = metric.compute(testdata.getModel(TestCase4), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: More than One NonEmpty Blank Node with Inconsistent Info", 0,
				stars_test4.intValue());
	}

	@Test
	public void TestCase5() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		// Compute stars : TestCase5 check for 5 stars --------> MUST PASS
		Integer stars_test5 = metric.compute(testdata.getModel(TestCase5), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: Inconsistent Providers ", 0, stars_test5.intValue());
	}

	@Test
	public void TestCase6() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		// Compute stars : TestCase6 check for 5 stars --------> MUST PASS
		Integer stars_test6 = metric.compute(testdata.getModel(TestCase6), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: Consistent Providers ", 5, stars_test6.intValue());
	}

	@Test
	public void TestCase7() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		// Compute stars : TestCase7 check for 5 stars --------> MUST PASS
		Integer stars_test7 = metric.compute(testdata.getModel(TestCase7), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: 4star Providers because no foaf:agent ", 4, stars_test7.intValue());

	}

}