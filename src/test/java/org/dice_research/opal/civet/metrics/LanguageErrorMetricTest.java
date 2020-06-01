package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link UpdateRateMetric}.
 */
public class LanguageErrorMetricTest {

	TestData testdata;
	final String TEST_FILE = "TestDataUpdateRateMetric.ttl";
	Model model;

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
		model = testdata.getModel(TEST_FILE);
	}

	@Test
	public void testStarNull() throws Exception {

		final String TEST_DATASET = "http://projekt-opal.de/dataset/https___"
				+ "europeandataportal_eu_set_data_debat_dorientations_budgetaires_2019";

		LanguageErrorMetric metric = new LanguageErrorMetric();
		Integer stars = metric.compute(model, TEST_DATASET);

		//TODO Create cases

	}

}