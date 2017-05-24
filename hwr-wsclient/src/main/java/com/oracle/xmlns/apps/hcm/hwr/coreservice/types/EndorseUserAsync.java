
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
 *         &lt;element name="pEndorsement" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "pEndorsement"
})
@XmlRootElement(name = "EndorseUserAsync")
public class EndorseUserAsync {

    @XmlElement(required = true)
    protected String pEndorsement;

    /**
     * Gets the value of the pEndorsement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPEndorsement() {
        return pEndorsement;
    }

    /**
     * Sets the value of the pEndorsement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPEndorsement(String value) {
        this.pEndorsement = value;
    }

}
