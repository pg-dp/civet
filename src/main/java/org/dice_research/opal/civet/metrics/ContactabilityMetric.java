package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The ContactabilityMetric awards stars based on the availability 
 * of contact details in the dataset.
 * 
 * @author Amit Kumar
 */
public class ContactabilityMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes the quality of dataset as per contactability metric." 
			+ "Two kinds of ratings are awarded to the dataset which are following: "
			+ "Stars 5: Name, Email, Telephone Number and Address is in the dataset."
			+ "Stars 4: If any three of the Name, Email, Address or Telephone number is in the dataset."
			+ "Stars 3: If any two of the Name, Email, Telephone Number or Address is in the dataset."
			+ "Stars 2: If any one of the Name, Email, Telephone Number or Address is in the dataset."
			+ "Stars 0: If none of the Name, Email and Address is in the dataset.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		//creating a dataset
		LOGGER.info("Processing dataset " + datasetUri);
		Resource distribution = ResourceFactory.createResource(datasetUri);
		
		StmtIterator stmtItr = model.listStatements(new SimpleSelector
				(distribution,DCAT.contactPoint,(RDFNode) null));
		
		boolean nameFound=false;
		boolean nameFound2=false;
		boolean emailFound=false;
		boolean adrFound=false;
		boolean telephoneFound=false;

//		hasTelephone

		Property customName = model.createProperty("http://xmlns.com/foaf/0.1/name");
		Property customName2 = model.createProperty("http://www.w3.org/2006/vcard/ns#fn");
		Property customEmail = model.createProperty("http://www.w3.org/2006/vcard/ns#hasEmail");
		Property customAdr = model.createProperty("http://www.w3.org/2006/vcard/ns/ns#hasAddress");
		Property customTelephone = model.createProperty("http://www.w3.org/2006/vcard/ns#hasTelephone");

		
		if(stmtItr.hasNext())
		{
			Statement stmt = stmtItr.nextStatement();
			RDFNode object = stmt.getObject();
			Resource objectAsResource = (Resource) object ;
			if(objectAsResource.hasProperty(customEmail))
			{
                String email = objectAsResource.getProperty(customEmail).getObject().toString();
                if(!email.equals(""))          
					emailFound =  true ;
			}
			if(objectAsResource.hasProperty(customName))
			{
                String name = objectAsResource.getProperty(customName).getObject().toString();
                if(!name.equals(""))
					nameFound =  true ;
			}
			if(objectAsResource.hasProperty(customName2))
			{
                String name = objectAsResource.getProperty(customName2).getObject().toString();
                if(!name.equals(""))
                	nameFound2 =  true ;
			}
			if(objectAsResource.hasProperty(customAdr))
			{
                String adr = objectAsResource.getProperty(customAdr).getObject().toString();
                if(!adr.equals(""))
					adrFound =  true ;
			}
			if(objectAsResource.hasProperty(customTelephone))
			{
                String adr = objectAsResource.getProperty(customTelephone).getObject().toString();
                if(!adr.equals(""))
                	telephoneFound =  true ;
			}
		}
		
		if((nameFound || nameFound2) && emailFound && adrFound && telephoneFound) {
			return 5;
		}
		else if((nameFound || nameFound2) && emailFound && adrFound
				|| (nameFound || nameFound2) && adrFound && telephoneFound
				|| emailFound && adrFound && telephoneFound
				)
		{
			return 4;
		}
		else if(((nameFound || nameFound2) && emailFound)
				||((nameFound || nameFound2) && adrFound)
				||((nameFound || nameFound2) && telephoneFound)
				||(emailFound && adrFound)
				||(emailFound && telephoneFound)
				||(adrFound && telephoneFound)) {
			return 3;
		}
		else if((nameFound || nameFound2) || emailFound || adrFound || telephoneFound){
			return 2;
		}
		else 
			return 0;
		}
	
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_CATEGORIZATION.getURI();
	}
}



