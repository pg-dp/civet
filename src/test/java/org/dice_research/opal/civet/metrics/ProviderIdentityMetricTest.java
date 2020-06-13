package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ProviderIdentityMetric}.
 * 
 * @author Gourab Sahu
 */


public class ProviderIdentityMetricTest {

	private TestData testdata;
	private ProviderIdentityMetric metric;
	
	/*
	 *  Dataset has no dct:publisher and no accessURL at distributions but 
	 *  the dataset has a landingPage which as per Data Catalog can be 
	 *  treated as publisher information ---> 5 stars
	 */
	private static final String TESTCASE_5STARS = "TestCaseForPI_5Stars.ttl";
	private static final String TEST_DATASET_5STARS = "http://projekt-opal.de/dataset/https___ckan_govdata_de_0af72fb1_b058_4de4_b99e_369a5a463f24";
	
	
	/*
	 * 4 stars: dct:Publisher is not of FOAF:Person or Organization or Agent
	 */
	private static final String TESTCASE_4STARS = "TestCaseForPI_4Stars__synthetic.ttl";
	private static final String TEST_DATASET_4STARS = "http://projekt-opal.de/dataset/_mcloudde_vieljhrlicherasterdesmittlerenvegetationsbeginnsindeutschland";
	
	
	/*
	 * The dataset has no dcat:landingPage, no dct:publisher and out of 
	 * 2 distributions only 1 has dcat:accessURL. As per Data Catalog, 
	 * accessURL can be used as landingPage is distributions are accessed 
	 * only through accessURL.
	 */
	private static final String TESTCASE_3STARS = "TestCaseForPI_3Stars__synthetic.ttl";
	private static final String TEST_DATASET_3STARS = "http://projekt-opal.de/dataset/https___ckan_govdata_de_34a3e19c_323f_474a_b21b_b03be924093a";
	
	
	/*
	 * Dataset has no publisher info. 2 out of 5 distributions have a valid access_url.
	 */
	private static final String TESTCASE_2STARS = "TestCaseForPI_2Stars__synthetic.ttl";
	private static final String TEST_DATASET_2STARS = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_464819ee_0d2a_4ca4_b5a4_026e34488404";
	
	
	/*
	 * Dataset has no publisher info. 1 out of 5 distributions have a valid access_url.
	 */
	private static final String TESTCASE_1STARS = "TestCaseForPI_1Stars__synthetic.ttl";
	private static final String TEST_DATASET_1STARS = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_2013_03_visas_delivres_aux_etudiants";
	
	
	/*
	 * No publisher information at all
	 */
	private static final String TESTCASE_0STARS = "TestCaseForPI_0Stars__synthetic.ttl";
	private static final String TEST_DATASET_0STARS = "http://projekt-opal.de/dataset/_mcloudde_vieljhrlicherasterdesmittlerenvegetationsbeginnsindeutschland";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
		metric = new ProviderIdentityMetric();
	}
	
	@After
	public void tearDown() {
		testdata = null;
		metric = null;
	}

	
	@Test
	public void testCase0() throws Exception {
		Integer stars = metric.compute(testdata.getModel(TESTCASE_0STARS), TEST_DATASET_0STARS);
		Assert.assertEquals("Provider Identity Test: Test Case 0 stars", 0, stars.intValue());
	}
	
	@Test
	public void testCase1() throws Exception {
		Integer stars = metric.compute(testdata.getModel(TESTCASE_1STARS), TEST_DATASET_1STARS);
		Assert.assertEquals("Provider Identity Test: Test Case 1 stars", 1, stars.intValue());
	}
	
	@Test
	public void testCase2() throws Exception {
		Integer stars = metric.compute(testdata.getModel(TESTCASE_2STARS), TEST_DATASET_2STARS);
		Assert.assertEquals("Provider Identity Test: Test Case 2 stars", 2, stars.intValue());
	}
	
	
	@Test
	public void testCase3() throws Exception {

		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars = metric.compute(testdata.getModel(TESTCASE_3STARS), TEST_DATASET_3STARS);
		Assert.assertEquals("Provider Identity Test: Test Case 3 stars", 3, stars.intValue());
	}

	
	@Test
	public void testCase4() throws Exception {

		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars = metric.compute(testdata.getModel(TESTCASE_4STARS), TEST_DATASET_4STARS);
		Assert.assertEquals("Provider Identity Test: Test Case 4 stars", 4, stars.intValue());
	}
	
	
	@Test
	public void testCase5() throws Exception {

		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars = metric.compute(testdata.getModel(TESTCASE_5STARS), TEST_DATASET_5STARS);
		Assert.assertEquals("Provider Identity Test: Test Case 5 stars", 5, stars.intValue());
	}


}