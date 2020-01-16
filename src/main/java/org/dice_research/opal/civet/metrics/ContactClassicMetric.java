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
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The ContactClassicMetric awards stars based on the availability 
 * of classical contact details in the dataset like name, address and telephone number.
 *
 * Url metrics can be found under dcat:contactPoint and DCTerms.publisher
 *
 * There are three sub metrics under contactability(Contact URL, Contact Email and Classical contact information)
 *
 * @author Amit Kumar
 */

public class ContactClassicMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes the quality of dataset as per contactability criteria."
			+ "Four kinds of ratings are awarded to the dataset which are following: "
			+ "Stars 5: Name, Telephone and Address is found."
			+ "Stars 4: Any two of the Name, Telephone and Address is found."
			+ "Stars 2: Any one of the Name, Telephone and Address is in the dataset."
			+ "Stars 0: Nothing is found"; 

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		//creating a dataset
		LOGGER.info("Processing dataset " + datasetUri);
		Resource dataset = ResourceFactory.createResource(datasetUri);
		
		StmtIterator stmtItr = model.listStatements(new SimpleSelector
				(dataset,DCAT.contactPoint,(RDFNode) null));
		NodeIterator publisherObjectsIterator = model.listObjectsOfProperty(dataset,DCTerms.publisher);

		boolean nameFound=false;
		boolean nameFound2=false;
		boolean adrFound=false;
		boolean telephoneFound=false;

		//		iterating through DCTerms.publisher to find Name and Phone
		while(publisherObjectsIterator.hasNext()) {
			RDFNode Publisher = publisherObjectsIterator.next();
			Resource PublisherBlankNode = (Resource) Publisher;
			if(PublisherBlankNode.hasProperty(FOAF.name)) {
				if (PublisherBlankNode.getProperty(FOAF.name).getObject() != null) {
					nameFound = true;
					}
				}
			else if(PublisherBlankNode.hasProperty(FOAF.firstName)||PublisherBlankNode.hasProperty(FOAF.familyName)
						||PublisherBlankNode.hasProperty(FOAF.lastName)){
					nameFound = true;										
				}
			if (PublisherBlankNode.hasProperty(FOAF.phone)) {
					if (PublisherBlankNode.getProperty(FOAF.phone).getObject() != null) {
						telephoneFound = true;					
						}
				}
		}
		
		Property customName = model.createProperty("http://xmlns.com/foaf/0.1/name");
		Property customName2 = model.createProperty("http://www.w3.org/2006/vcard/ns#fn");
		Property customAdr = model.createProperty("http://www.w3.org/2006/vcard/ns/ns#hasAddress");
		Property customTelephone = model.createProperty("http://www.w3.org/2006/vcard/ns#hasTelephone");

		//		iterating through dcat:contactpoint to find Name, Address and Phone
		while(stmtItr.hasNext())
		{
			Statement stmt = stmtItr.nextStatement();
			RDFNode object = stmt.getObject();
			Resource objectAsResource = (Resource) object ;
			
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
		
		//	checking what boolean values are true and returning appropriate ratings
		if((nameFound || nameFound2)  && adrFound && telephoneFound) {
			return 5;
		}
		
		else if((nameFound || nameFound2)  && adrFound
				|| (nameFound || nameFound2) && telephoneFound
				||  adrFound && telephoneFound
				)
		{
			return 4;
		}
		else if((nameFound || nameFound2 )
				|| adrFound
				||telephoneFound) {
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



