package org.dice_research.opal.civet.metrics;

import org.apache.jena.ext.com.google.common.annotations.VisibleForTesting;
import org.apache.jena.rdf.model.Model;
import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DescriptionMetric-Improved}.
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
	public void checkLessPosTags() throws Exception {

		DescriptionMetricImproved metric = new DescriptionMetricImproved();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_vieljhrlicherasterdesmittlerenvegetationsbeginnsindeutschland";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("It contains less posTags", 2, stars.intValue());
	}

	@Test
	public void checkVeryLessPosTags() throws Exception {

		DescriptionMetricImproved metric = new DescriptionMetricImproved();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_vbb-fahrplandatenfebruarbisdezember2016";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("It contains very less posTags", 1, stars.intValue());
	}

	@Test
	public void checkEmptyDescription() throws Exception {

		DescriptionMetricImproved metric = new DescriptionMetricImproved();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_standortederdauerzhlstellenradverkehrindsseldorf";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("It contains empty description value", 1, stars.intValue());
	}

	@Test
	public void checkAveragePosTags() throws Exception {

		DescriptionMetricImproved metric = new DescriptionMetricImproved();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_elektroladestandortehamburg";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("It contains average posTags", 3, stars.intValue());
	}

	@Test
	public void checkAboveAveragePosTags() throws Exception {

		DescriptionMetricImproved metric = new DescriptionMetricImproved();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_tglicherasterderrealenevapotranspirationbergrasundsandigemlehm";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("It contains above average posTags", 4, stars.intValue());
	}

	@Test
	public void checkBestPosTags() throws Exception {

		DescriptionMetricImproved metric = new DescriptionMetricImproved();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_wettervorhersagemodellcosmo-de";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("It contains best posTags", 5, stars.intValue());
	}

}