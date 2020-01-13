package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ContactEmailMetricTest {
	TestData testdata;
	private static final String TEST_Model4881 = "model4881.ttl";
	private static final String TEST_Model1483 = "model1483.ttl";
	private static final String TEST_Model1200 = "model1200.ttl";

	//  dcat:contactPoint  hasAddress hasEmail HasTelephone
	private static final String TEST_DATASET_Model1483 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_pr_aspro_00001_20170127_120846_ds10";

	//  dcat:contactPoint hasAddress   hasEmail   hasTelephone
	private static final String TEST_DATASET_Model1200 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_fr_120066022_jdd_95bc6446_3762_47fe_863f_f11c60faff58";

	//	Test dataset with hasEmail
	private static final String TEST_DATASET_Rating2 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_bristol_city_council_social_housing_valuations";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}
	
	@Test
	public void testDatasetrating3_1() throws Exception {
		ContactEmailMetric metric = new ContactEmailMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_Model1483), TEST_DATASET_Model1483);
		Assert.assertEquals(TEST_Model1483, 5, stars.intValue());
	}
	
	@Test
	public void testDatasetRating3_2() throws Exception {
		ContactEmailMetric metric = new ContactEmailMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_Model1200), TEST_DATASET_Model1200);
		Assert.assertEquals(TEST_Model1200, 5, stars.intValue());
	}
	
	@Test
	public void testDatasetRating2() throws Exception {
		ContactEmailMetric metric = new ContactEmailMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_Model4881), TEST_DATASET_Rating2);
		Assert.assertEquals(TEST_Model4881, 5, stars.intValue());
	}
}
