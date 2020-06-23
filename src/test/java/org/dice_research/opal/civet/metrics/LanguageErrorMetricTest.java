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

    private final String TEST_FILE = "TestDataLanguageErrorMetric.ttl";
    private Model model;
    private LanguageErrorMetric metric = new LanguageErrorMetric();

    @Before
    public void setUp() {
        TestData testdata = new TestData();
        model = testdata.getModel(TEST_FILE);
    }

    @Test
    public void testStarOne() throws Exception {

        final String TEST_DATASET = "http://projekt-opal.de/dataset/https___" +
                "europeandataportal_eu_set_data_agricultural_land_classification_detailed_post1988_survey_alcr00995";
        Integer stars = metric.compute(model, TEST_DATASET);
        Assert.assertEquals(TEST_FILE, 1, stars.intValue());
    }

    @Test
    public void testStarTwo() throws Exception {

        final String TEST_DATASET = "http://projekt-opal.de/dataset/https___" +
                "europeandataportal_eu_set_data_agricultural_land_classification_detailed_post1988_survey_alcr008951";
        Integer stars = metric.compute(model, TEST_DATASET);
        Assert.assertEquals(TEST_FILE, 2, stars.intValue());
    }

    @Test
    public void testStarThree() throws Exception {

        final String TEST_DATASET = "http://projekt-opal.de/dataset/https___" +
                "europeandataportal_eu_set_data_14f6d44d_c5d4_5225_96d9_f42a0879c8f1";
        Integer stars = metric.compute(model, TEST_DATASET);
        Assert.assertEquals(TEST_FILE, 3, stars.intValue());
        System.out.println();
    }

    @Test
    public void testStarFour() throws Exception {

        final String TEST_DATASET = "http://projekt-opal.de/dataset/https___" +
                "europeandataportal_eu_set_data_02b05847_1f02_d38a_bd41_6bdc832c94ca";
        Integer stars = metric.compute(model, TEST_DATASET);
        Assert.assertEquals(TEST_FILE, 4, stars.intValue());
    }

    @Test
    public void testStarsFive() throws Exception {

        final String TEST_DATASET = "http://projekt-opal.de/dataset/https___" +
                "europeandataportal_eu_set_data_agricultural_land_classification_detailed_post1988_survey_alcl16793";
        Integer stars = metric.compute(model, TEST_DATASET);
        Assert.assertEquals(TEST_FILE, 5, stars.intValue());
    }

    @Test
    public void testNoLanguageTag() throws Exception {

        final String TEST_DATASET = "http://projekt-opal.de/dataset/https___" +
                "europeandataportal_eu_set_data_029d4698_6c32_46a1_ae7b_89beb74f8b14";
        Integer stars = metric.compute(model, TEST_DATASET);
        Assert.assertNull(TEST_FILE, stars);
    }
}