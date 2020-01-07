package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;
import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link VersionMetric}.
 */
public class VersioningMetricTest {

	private TestData testdata;

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void test3Stars() throws Exception {

		String testFile = "Govdata-Allermoehe.ttl";
		String testFileDataset =
				"http://projekt-opal.de/dataset/https___" +
						"ckan_govdata_de_001c9703_3556_4f62_a376_85804f18ab52";

		VersionMetric metric = new VersionMetric();
		Integer stars = metric.compute(testdata.getModel(testFile),
				testFileDataset);

		Assert.assertEquals(testFile, 3, stars.intValue());
	}

	@Test
	public void test4Stars() throws Exception {

		String testFile = "testData1.ttl";
		String testFileDataset = "http://projekt-opal.de/dataset/https___" +
				"europeandataportal_eu_set_data_f37cb664_6e96_48cb_8db9_" +
				"7942ea08130d";

		VersionMetric metric = new VersionMetric();
		Integer stars = metric.compute(testdata.getModel(testFile),
				testFileDataset);

		Assert.assertEquals(testFile, 4, stars.intValue());
	}

	@Test
	public void test5Stars() throws Exception {

		String testFile = "testData1.ttl";
		String testFileDataset = "http://projekt-opal.de/dataset/https___" +
				"europeandataportal_eu_set_data_f368cc99_e791_47b4_ba4f_5c148140d00e";

		VersionMetric metric = new VersionMetric();
		Integer stars = metric.compute(testdata.getModel(testFile),
				testFileDataset);

		Assert.assertEquals(testFile, 5, stars.intValue());
	}

	@Test
	public void testNullStars() throws Exception {

		String testFile = "Europeandataportal-Iceland.ttl";
		String testFileDataset = "http://projekt-opal.de/dataset/http___" +
				"europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_" +
				"818e8ef3111701";

		VersionMetric metric = new VersionMetric();
		Integer stars = metric.compute(testdata.getModel(testFile),
				testFileDataset);

		Assert.assertNull(testFile, stars);
	}
}