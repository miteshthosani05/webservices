
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
 *         &lt;element name="pGlobalProfileIds" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "pGlobalProfileIds"
})
@XmlRootElement(name = "getUserToEndorse")
public class GetUserToEndorse {

    @XmlElement(required = true)
    protected String pGlobalProfileIds;

    /**
     * Gets the value of the pGlobalProfileIds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPGlobalProfileIds() {
        return pGlobalProfileIds;
    }

    /**
     * Sets the value of the pGlobalProfileIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPGlobalProfileIds(String value) {
        this.pGlobalProfileIds = value;
    }

}
