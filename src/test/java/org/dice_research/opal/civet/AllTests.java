package org.dice_research.opal.civet;

import org.dice_research.opal.civet.example.ExampleTest;
import org.dice_research.opal.civet.metrics.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

		// Civet

		MetricComputationTest.class,

		// Metrics

		CategorizationMetricTest.class,

		MultipleSerializationsMetricTest.class,

		UpdateRateMetricTest.class,

		VersioningMetricTest.class,

		TimelinessMetricTest.class,

		ReadabilityMetricTest.class,

		AccessabilityMetricTest.class,

		AvailabilityOfLicensesMetricTest.class,

		ContactClassicMetricTest.class,

		ContactEmailMetricTest.class,

		ContactURLMetricTest.class,

		DataFormatMetricTest.class,

		DateformatTest.class,

		DescriptionTest.class,

		ProviderIdentityMetricTest.class,



		// Metric aggregation

		MetadataQualityMetricTest.class,

		// Minimal working example

		ExampleTest.class

})

public class AllTests {

}