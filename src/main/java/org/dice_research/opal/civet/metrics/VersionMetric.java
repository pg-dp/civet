package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.OWL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;


/**
 * The VersionMetric awards stars based on the version given in
 * datasets.
 */

public class VersionMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes if Version Number is there "
			+ "If Version Number is given as a property VersionInfo," +
            "5 stars are awarded. "
			+ "If the version number is given in the property conformsTo ," +
            "4 stars are awarded"
            + "If the version number is given in the property download or access url ," +
            "3 stars are awarded "
            + "Else null is returned";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

        LOGGER.info("Processing dataset " + datasetUri);

		Resource dataset = ResourceFactory.createResource(datasetUri);

		StmtIterator iter = model.listStatements
				(dataset, DCAT.distribution ,(RDFNode) null);

		Statement versionInfoAsProperty = model.getProperty(dataset, OWL.versionInfo);

        String versionInfoAsPropertyValue = "";
        if(versionInfoAsProperty != null)
			versionInfoAsPropertyValue = versionInfoAsProperty.
					getObject().toString();

        boolean versionInfoAsPropertyFound = false;
        if(!versionInfoAsPropertyValue.equals(""))
			versionInfoAsPropertyFound = true;

        boolean versionStringFound = false;
        String downloadUrl = "";
        String accessUrl = "";
        while (iter.hasNext()) {
			Statement stmt = iter.nextStatement();
			RDFNode object = null ;

			if(stmt != null)
				object = stmt.getObject();

			if (object != null && object.isURIResource())
			{
				Resource distributionURI = (Resource) object;
				Statement statementAccessUrl = model.getProperty(distributionURI,
						DCAT.accessURL);
				Statement statementDownloadUrl = model.getProperty(distributionURI,
						DCAT.downloadURL);

				if (statementAccessUrl != null)
					accessUrl = String.valueOf(statementAccessUrl.getObject());

				if (statementDownloadUrl != null)
					downloadUrl = String.valueOf(statementDownloadUrl.getObject());

				if (accessUrl.toLowerCase().contains("version") ||
						downloadUrl.toLowerCase().contains("version"))
					versionStringFound = true;
			}
		}

		StmtIterator conformsToItr = model.listStatements
				(dataset,DCTerms.conformsTo,(RDFNode) null);

        boolean versionInfoFound = false;
        if(conformsToItr.hasNext())
		{
			Statement statementConformsTo = conformsToItr.nextStatement();
			RDFNode object = statementConformsTo.getObject();
			Resource objectAsResource = (Resource) object ;
			if(objectAsResource.hasProperty(OWL.versionInfo))
			{
                String versionInfo = objectAsResource.getProperty
                        (OWL.versionInfo).getObject().toString();

                if(!versionInfo.equals(""))
					versionInfoFound =  true ;
			}
		}

		if(versionInfoAsPropertyFound)
			return 5;
		else if (versionInfoFound)
			return 4;
		else if (versionStringFound)
			return 3;
		else
			return null;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_VERSION_NUMBERING.getURI();
	}
}