
package com.oracle.xmlns.apps.hcm.hwr.coreservice.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="initializationInformation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "initializationInformation"
})
@XmlRootElement(name = "initializeWLAAsync")
public class InitializeWLAAsync {

    @XmlElement(required = true)
    protected String initializationInformation;

    /**
     * Gets the value of the initializationInformation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitializationInformation() {
        return initializationInformation;
    }

    /**
     * Sets the value of the initializationInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitializationInformation(String value) {
        this.initializationInformation = value;
    }

}
