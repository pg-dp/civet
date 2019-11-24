//package org.dice_research.opal.civet.metrics;
//
//import org.dice_research.opal.civet.TestData;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * Tests {@link Expressiveness}.
// * 
// * @author Aamir Mohammed
// */
//public class RepresentationTest {
//
//	TestData testdata;
//
//	// Dateformat is in the standard format then give 5 Stars
//	private static final String TestCase1 = "Test_data_for_dateformat_correct.ttl";
//
//    // Date format is not in the standard format then give 1 star 
//	private static final String TestCase2 = "Test_data_for_incorrect.ttl";
//
//	// Metadata field dct:issued is empty then return null
//	private static final String TestCase3 = "Test_data_for_issued_is_null.ttl";
//	
//	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";
//
//	@Before
//	public void setUp() throws Exception {
//		testdata = new TestData();
//	}
//	
//	
//	@Test
//	public void TestCase1() throws Exception {
//   
//		Representation metric = new Representation();
//		System.out.println("..............."+testdata.getModel(TestCase1));
//		Integer stars = metric.compute(testdata.getModel(TestCase1), TEST_EDP_ICE_DATASET);
//		System.out.println("Test case 1:"+stars);
//		Assert.assertEquals("Dateformat is according to W3C standards ", 5, stars.intValue());
//	}
//
//	@Test
//	public void TestCase2() throws Exception {
//		Representation metric = new Representation();
//		Integer stars = metric.compute(testdata.getModel(TestCase2), TEST_EDP_ICE_DATASET);
//		System.out.println("Test case 2:"+stars);
//		Assert.assertEquals("Dateformat is not according to W3C standards", 1, stars.intValue());
//	}
//
//	@Test
//	public void TestCase3() throws Exception {
//		Representation metric = new Representation();
//		System.out.println("testdata.getModel(TestCase3)    "+ testdata.getModel(TestCase3));
//		Integer stars = metric.compute(testdata.getModel(TestCase3), TEST_EDP_ICE_DATASET);
//		System.out.println("Test case 3:"+stars);
//		Assert.assertEquals("Dct:issued field is empty", null, stars);
//	}
//
//}