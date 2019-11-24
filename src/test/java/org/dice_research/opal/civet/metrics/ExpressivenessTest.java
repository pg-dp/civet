package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link Expressiveness}.
 *  @author Aamir Mohammed
 */
public class ExpressivenessTest {

	TestData testdata;

	// If dataset has description and if string is not empty and length of description > length of title then award 5 star
	private static final String TestCase1 = "TestCaseForKnownLicense5stars_5dist_5rights.ttl";

	// If dataset has description but length of desc = length of title give 4 star.
	private static final String TestCase2 = "TestCaseForKnownLicense4stars_5dist_4rights.ttl";

	// If dataset has description but length of desc < length of title give 3 star.
	private static final String TestCase3 = "TestCaseForKnownLicense5stars_dataset_has_licenseInfo.ttl";
	
	// If dataset does not has description or its property is null then give 1 star.
	private static final String TestCase4 = "TestCaseForKnownLicense4stars_5dist_2license_2rights.ttl";
	
	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}
	
	
	@Test
	public void TestCase1() throws Exception {
   
		Expressiveness metric = new Expressiveness();
		System.out.println("testdata.getModel(TestCase1)+"+testdata.getModel(TestCase1));
		Integer stars = metric.compute(testdata.getModel(TestCase1), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dataset has description and if string is not empty and length of description > length of title", 5, stars.intValue());
	}

	@Test
	public void TestCase2() throws Exception {
		Expressiveness metric = new Expressiveness();
		Integer stars = metric.compute(testdata.getModel(TestCase2), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dataset has description but length of desc = length of title", 4, stars.intValue());
	}

	@Test
	public void TestCase3() throws Exception {
		Expressiveness metric = new Expressiveness();
		Integer stars = metric.compute(testdata.getModel(TestCase3), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dataset has description but length of desc < length of title", 3, stars.intValue());
	}
	
	@Test
	public void TestCase4() throws Exception {
		Expressiveness metric = new Expressiveness();
		Integer stars = metric.compute(testdata.getModel(TestCase4), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dataset does not has description or its property is null", 1, stars.intValue());
	}
}