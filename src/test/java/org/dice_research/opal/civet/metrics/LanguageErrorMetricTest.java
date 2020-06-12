package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link LanguageErrorMetric}.
 */

public class LanguageErrorMetricTest {

    final String TEST_FILE = "TestDataUpdateRateMetric.ttl";
    TestData testdata;
    Model model;

    @Before
    public void setUp() throws Exception {
        testdata = new TestData();
        model = testdata.getModel(TEST_FILE);
    }

    @Test
    public void test5Starts() throws Exception {

        final String TEST_DATASET = "http://projekt-opal.de/dataset/https___" +
                "europeandataportal_eu_set_data_agricultural_land_classification_" +
                "detailed_post1988_survey_alcl16793";
        LanguageErrorMetric metric = new LanguageErrorMetric();
        Integer stars = metric.compute(model, TEST_DATASET);
        Assert.assertEquals(TEST_FILE, 5, stars.intValue());
    }
//
//    @Test
//    public void testStarOne() throws Exception {
//
//        final String TEST_DATASET = "http://projekt-opal.de/dataset/https___" +
//                "europeandataportal_eu_set_data_de68f4fc_2886_4775_9537_2bf21d81dff7";
//        LanguageErrorMetric metric = new LanguageErrorMetric();
//        Integer stars = metric.compute(model, TEST_DATASET);
//        Assert.assertEquals(TEST_FILE, 1, stars.intValue());
//    }
//
//    @Test
//    public void testStarTwo() throws Exception {
//
//        final String TEST_DATASET = "http://projekt-opal.de/dataset/https___" +
//                "europeandataportal_eu_set_data_de68f4fc_2886_4775_9537_2bf21d81dff7";
//        LanguageErrorMetric metric = new LanguageErrorMetric();
//        Integer stars = metric.compute(model, TEST_DATASET);
//        Assert.assertEquals(TEST_FILE, 2, stars.intValue());
//    }
//
//    @Test
//    public void testStarThree() throws Exception {
//
//        final String TEST_DATASET = "http://projekt-opal.de/dataset/https___" +
//                "europeandataportal_eu_set_data_de68f4fc_2886_4775_9537_2bf21d81dff7";
//        LanguageErrorMetric metric = new LanguageErrorMetric();
//        Integer stars = metric.compute(model, TEST_DATASET);
//        Assert.assertEquals(TEST_FILE, 3, stars.intValue());
//    }
//
//    @Test
//    public void testStarFour() throws Exception {
//
//        final String TEST_DATASET = "http://projekt-opal.de/dataset/https___" +
//                "europeandataportal_eu_set_data_de68f4fc_2886_4775_9537_2bf21d81dff7";
//        LanguageErrorMetric metric = new LanguageErrorMetric();
//        Integer stars = metric.compute(model, TEST_DATASET);
//        Assert.assertEquals(TEST_FILE, 4, stars.intValue());
//    }
//
//    @Test
//    public void testStarFive() throws Exception {
//
//        final String TEST_DATASET = "http://projekt-opal.de/dataset/https___" +
//                "europeandataportal_eu_set_data_de68f4fc_2886_4775_9537_2bf21d81dff7";
//        LanguageErrorMetric metric = new LanguageErrorMetric();
//        Integer stars = metric.compute(model, TEST_DATASET);
//        Assert.assertEquals(TEST_FILE, 5, stars.intValue());
//    }
}