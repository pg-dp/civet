package org.dice_research.opal.civet.metrics;

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
 * Tests {@link KnownLicenseMetric}.
 * 
 * @author Adrian Wilke
 */
public class KnownLicenseMetricTest {

	TestData testdata;

	private static final String TEST_EDP_ICE = "Govdata-Allermoehe.ttl";
	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}
	
	
	@Test
	public void testGov() throws Exception {
		
		// Compute stars
		KnownLicenseMetric metric = new KnownLicenseMetric();
		
		//It is supposed to pass with 5 stars as dcat:Dataset has license information
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST_EDP_ICE_DATASET); 
		Assert.assertEquals("KnownLicense Test for Govdata-Allermoehe", 5, stars.intValue());
		
	}


	@Test
	public void testEdpIce() throws Exception {
		
		// Compute stars
		KnownLicenseMetric metric = new KnownLicenseMetric();
		
		//It is supposed to fail as it has only 50% of license information
		Integer stars = metric.compute(testdata.getModel("Europeandataportal-Iceland.ttl"), TEST_EDP_ICE_DATASET); 
		Assert.assertEquals("KnownLicense Test for EuropeanDataportal Iceland", 5, stars.intValue());
		
		
	}

}