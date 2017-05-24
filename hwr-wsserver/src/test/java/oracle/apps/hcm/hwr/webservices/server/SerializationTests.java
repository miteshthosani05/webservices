/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.webservices.server;

import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WebservicesResultSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * @author Yenal Kal
 */
public class SerializationTests {

    /** The logger instance */
    private static final Log LOGGER = LogFactory.getLog(SerializationTests.class);

    @Test
    public void testDeserialization() {
        String mySerializedObject =
            "H4sIAAAAAAAAAF2Rb2+CMBDG3/spLrzYq/lniS82BQzDOolMGNQ4syykys2wQdG2Gv32KxCNW5Mm93uud70+NUenIocjCpmV3DIeOj0DkG/KNONby1jQSfvRAKkYT1lecrSMM0pjZLdMgfKQK7sFeplRDW6Zot3um90bbPJEiFK8opRsi3YocMcEprFiCgvkymV5vmabnyGsWQrxmw9bwYqCCfhYhGOHEpguoyQkekcTP4lXcxdiQsFZ0GlCgxmZW6P7hmLiRoRekbyHXuRQL5gnVZ9Kj6kOKnIqeiXUuUKdqgLfiWnSXD1Onlf/pGurW80PXjw9BiynJCIQRsHE80kyI7r4cwgcpcIU8LTBndJGQybhmx1ZR+7zjn5wfOaKnWqXyOXMAILIafd6T73+AO72h1IN6wGbcAAZP7I8SyFLtYXZV4aiZXb/GK358ku/mmZOXucBAAA=";

        WebservicesResult myResult =
            new WebservicesResultSerializer()
                .deserialize(mySerializedObject);

        LOGGER.debug(myResult.toString());
        LOGGER.debug("Error message: " + myResult.getErrorMessage());
    }

}
