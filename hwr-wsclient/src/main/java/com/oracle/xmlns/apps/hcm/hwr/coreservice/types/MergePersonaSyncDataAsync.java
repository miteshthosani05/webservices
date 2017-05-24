
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
 *         &lt;element name="PersonaSyncdata" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "personaSyncdata"
})
@XmlRootElement(name = "mergePersonaSyncDataAsync")
public class MergePersonaSyncDataAsync {

    @XmlElement(name = "PersonaSyncdata", required = true)
    protected String personaSyncdata;

    /**
     * Gets the value of the personaSyncdata property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonaSyncdata() {
        return personaSyncdata;
    }

    /**
     * Sets the value of the personaSyncdata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonaSyncdata(String value) {
        this.personaSyncdata = value;
    }

}
