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
	private static final String TEST_DATASET_1 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_pr_aspro_00001_20170127_120846_ds10";
	private static final String TEST_DATASET_2 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_fr_120066022_jdd_95bc6446_3762_47fe_863f_f11c60faff58";
	private static final String TEST_DATASET_3 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_bristol_city_council_social_housing_valuations";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}
	
	@Test
	public void testDatasetrating3_1() throws Exception {
		ContactEmailMetric metric = new ContactEmailMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_Model1483), TEST_DATASET_1);
		Assert.assertEquals(TEST_Model1483, 2, stars.intValue());
	}
	
	@Test
	public void testDatasetRating3_2() throws Exception {
		ContactEmailMetric metric = new ContactEmailMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_Model1200), TEST_DATASET_2);
		Assert.assertEquals(TEST_Model1200, 1, stars.intValue());
	}
	
	@Test
	public void testDatasetRating3_3() throws Exception {
		ContactEmailMetric metric = new ContactEmailMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_Model4881), TEST_DATASET_3);
		Assert.assertEquals(TEST_Model4881, 2, stars.intValue());
	}
}
