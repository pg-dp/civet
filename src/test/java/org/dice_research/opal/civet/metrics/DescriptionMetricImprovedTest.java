package org.dice_research.opal.civet.metrics;

import org.apache.jena.ext.com.google.common.annotations.VisibleForTesting;
import org.apache.jena.rdf.model.Model;
import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DescriptionMetric}.
 * 
 * @author Aamir Mohammed
 */
public class DescriptionMetricImprovedTest {

	@VisibleForTesting
	TestData testdata;
	@VisibleForTesting
	final String MODEL = "TestDescriptionMetric.ttl";
	@VisibleForTesting
	Model model;

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
		model = testdata.getModel(MODEL);
	}

	@Test
	public void checkDemo() throws Exception {

		DescriptionMetricImproved metric = new DescriptionMetricImproved();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_vieljhrlicherasterdesmittlerenvegetationsbeginnsindeutschland";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		System.out.println(stars);
		Assert.assertEquals("Demo", 5, stars.intValue());
	}

}