package org.dice_research.opal.civet.access;

import java.util.Arrays;
import java.util.Collection;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.metrics.Metric;
import org.dice_research.opal.civet.metrics.MetricType;

public class AllPropertiesMetric extends Metric {

	private static final String DESCRIPTION = "Requires all properties for tests.";
	private static final String ID = AllPropertiesMetric.class.getSimpleName();
	private static final MetricType METRIC_TYPE = MetricType.COUNTER;
	private static final Collection<String> REQUIRED_PROPERTIES = Arrays.asList(

			DataObjects.NUMBER_OF_CATEGORIES,

			DataObjects.DESCRIPTION, DataObjects.ISSUED, DataObjects.PUBLISHER, DataObjects.THEME, DataObjects.TITLE,

			DataObjects.DISTRIBUTION,

			DataObjects.ACCESS_URL, DataObjects.DOWNLOAD_URL, DataObjects.LICENSE);

	public AllPropertiesMetric() {
		this.description = DESCRIPTION;
		this.id = ID;
		this.metricType = METRIC_TYPE;
		this.requiredProperties = REQUIRED_PROPERTIES;
	}

	@Override
	public float getScore(DataContainer dataContainer) {

		return dataContainer.getIds().size();

	}

}