package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
 * Tests {@link ContactabilityMetricTest}.
 * 
 * @author Amit Kumar
 */
public class ContactURLMetricTest  {

	TestData testdata;
	private static final String TEST_Model4881 = "model4881.ttl";
	
	private static final String TEST_Model1483 = "model1483.ttl";
	
	private static final String TEST_Model1200 = "model1200.ttl";

	private static final String TEST_Case_DATASET_1 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_brownfield_register_ealing";
	
	private static final String TEST_Case_DATASET_2 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_btopoctettehhn_pa3ttopedntejin_c_biodxet_b_obwnha_bopobo";

	//  Test dataset with has dcat:contactPoint  hasAddress hasEmail HasTelephone
	private static final String TEST_DATASET_3 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_pr_aspro_00001_20170127_120846_ds10";

	//  Test Dataset with  hasAddress   hasEmail   hasTelephone
	private static final String TEST_DATASET_4 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_fr_120066022_jdd_95bc6446_3762_47fe_863f_f11c60faff58";

	//	Test dataset with hasEmail
	private static final String TEST_DATASET_5 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_bsii1_6dds";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void testDatasetAccessURL_1() throws Exception {
		ContactURLMetric metric = new ContactURLMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_Model4881), TEST_Case_DATASET_2);
		Assert.assertEquals(TEST_Model4881, 5, stars.intValue());
	}
	
	@Test
	public void testDatasetAccessURL_2() throws Exception {
		ContactURLMetric metric = new ContactURLMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_Model4881), TEST_Case_DATASET_1);
		Assert.assertEquals(TEST_Model4881, 5, stars.intValue());
	}
	
	@Test
	public void testDatasetLandingPage_1() throws Exception {
		ContactURLMetric metric = new ContactURLMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_Model1483), TEST_DATASET_3);
		Assert.assertEquals(TEST_Model1483, 5, stars.intValue());
	}
	
	@Test
	public void testDatasetLandingPage_2() throws Exception {
		ContactURLMetric metric = new ContactURLMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_Model1200), TEST_DATASET_4);
		Assert.assertEquals(TEST_Model1200, 5, stars.intValue());
	}
	
	@Test
	public void testDatasetAccessURL_3() throws Exception {
		ContactURLMetric metric = new ContactURLMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_Model4881), TEST_DATASET_5);
		Assert.assertEquals(TEST_Model4881, 5, stars.intValue());
	}
}