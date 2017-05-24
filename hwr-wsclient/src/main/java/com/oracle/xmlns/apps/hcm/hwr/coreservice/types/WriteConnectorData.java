
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
 *         &lt;element name="DataWriter" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "dataWriter"
})
@XmlRootElement(name = "writeConnectorData")
public class WriteConnectorData {

    @XmlElement(name = "DataWriter", required = true)
    protected String dataWriter;

    /**
     * Gets the value of the dataWriter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataWriter() {
        return dataWriter;
    }

    /**
     * Sets the value of the dataWriter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataWriter(String value) {
        this.dataWriter = value;
    }

}
