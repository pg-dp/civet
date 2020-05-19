package org.dice_research.opal.civet.metrics;

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
public class DescriptionMetricTest {

	TestData testdata;
	final String MODEL = "TestDescriptionMetric.ttl";
	Model model;

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
		model = testdata.getModel(MODEL);
	}

	@Test
	public void checkEmptyTitleDescription() throws Exception {

		DescriptionMetric metric = new DescriptionMetric();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_standortederdauerzhlstellenradverkehrindsseldorf";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Empty description and title", 0, stars.intValue());
	}

	@Test
	public void atleastDescription() throws Exception {
		DescriptionMetric metric = new DescriptionMetric();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_vieljhrlicherasterdesmittlerenvegetationsbeginnsindeutschland";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Atleast description", 1, stars.intValue());
	}

	@Test
	public void atleastTitle() throws Exception {
		DescriptionMetric metric = new DescriptionMetric();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_tglichestationsmessungendersonnenscheindauerinstunden";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Atleast title", 1, stars.intValue());
	}

	@Test
	public void equalTitleDescription() throws Exception {
		DescriptionMetric metric = new DescriptionMetric();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_vbb-fahrplandatenfebruarbisdezember2016";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("If the length of title is equal to description", 1, stars.intValue());
	}

	@Test
	public void shortTitle() throws Exception {
		DescriptionMetric metric = new DescriptionMetric();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_stadtmoersbersichtskartederinnenstadtvonmoers";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("If the length of title is less than 15", 1, stars.intValue());
	}

	@Test
	public void shortDescription() throws Exception {
		DescriptionMetric metric = new DescriptionMetric();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_vieljhrigerasterderrealenevapotranspirationbergrasundsandigemlehmperkalendermonat";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("If the length of description is less than or equal to 25", 2, stars.intValue());
	}

	@Test
	public void averageDescription() throws Exception {
		DescriptionMetric metric = new DescriptionMetric();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/bb5292fc-c6c2-455f-879c-3d05ed957176";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("If the length of description is less than or equal to 50", 3, stars.intValue());
	}

	@Test
	public void goodDescription() throws Exception {
		DescriptionMetric metric = new DescriptionMetric();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_brckenundsonstigeingenieurbauwerke";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("If the length of description is less than or equal to 75", 4, stars.intValue());
	}

	@Test
	public void bestDescription() throws Exception {
		DescriptionMetric metric = new DescriptionMetric();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_contispreferredareas";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("If the length of description is greater than 75", 5, stars.intValue());
	}

	@Test
	public void titleWithNoDescription() throws Exception {
		DescriptionMetric metric = new DescriptionMetric();
		final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/_mcloudde_flussgebietseinheiten-dewrrl";
		Integer stars = metric.compute(model, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Check If the title exists and no dct:description", 1, stars.intValue());
	}

}