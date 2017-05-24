/*******************************************************************************
* Copyright 2011, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
* rights, and intellectual property rights in and to this software remain with Oracle Corporation.
* Oracle Corporation hereby reserves all rights in and to this software. This software may not be
* copied, modified, or used without a license from Oracle Corporation. This software is
* by international copyright laws and treaties, and may be protected by other laws. Violation of
* copyright laws may result in civil liability and criminal penalties.
******************************************************************************/
package oracle.apps.hcm.hwr.ws.client.service;

import static org.junit.Assert.*;

import org.apache.avalon.framework.service.ServiceException;
import org.junit.Test;

/**
 * The Class RegisterUserRequestIntegrationTest.
 */
public class RegisterUserRequestIntegrationTest extends WsClientTest {
    
    /**
     * Test egrcm import.
     * @throws ServiceException 
     */
    @Test
    public void testRegisterUserRequest()  {
        String myInput = "H4sIAAAAAAAAAHVRXW+CMBR991cQ3hXq0DKDmPJlNJm6wDbXtwpl4L" +
                "B1paj468fiMjSL9+2ec+7XudbktCuUAxVlztlYBT1dVSiLeZKzj7H6EgVdU1VKSVhCCs7oWK1pqU7sjrUXPM0LGtYsnrGUu5xJkj" +
                "Mq7I7ShNXkjMaSC7TfF3lMZNN+QXbUDhxLu0teale3rS/oNTPzbKA3YeiDAYQPEOqW1lKt3COShLwS8Q/caG7yVoYqmUX8kzIbIe" +
                "Rgl0PfnZ5LB82r9+0QIx/WYp3lJwjl0agjhN00C5aL+WkzffKAt5H+8VwJ8hrXSbL+2k6Hq0IE3FwZ2w12iZ/GsyDC7vpROse3p3" +
                "QZQezussEz9rBnae3wdiFXUCJplDeO9HXQ7wK9C0AEzFEfjADsGcDs6nCkNxddKdvykBxoKBv87ynar7PaP2st7f4jvwEzbD+JHA" +
                "IAAA==";
        
        String myResponseStr = mRequest.registerUser(mConnectionInfo, myInput);
        String myResponseExpected = "H4sIAAAAAAAAALOxr8jNUShLLSrOzM+zVTLUM1BSSM1Lzk/JzEu3VQoNcdO1UFIoLknMS0nMyc9LtVWqTC1WsrfjsilKLS7NKbHjUgACmyAwxzk/JdXOwEYficdlow9TCADN8yvdagAAAA==";
        assertEquals(myResponseExpected, myResponseStr);
    }
    
    @Test
    public void testInitializeWLA()  {
        String myResponseStr = mRequest.initializeWla(mConnectionInfo, "");
        
        LOGGER.info("Response from server: '" + myResponseStr + "'");
        assertTrue("The response from the server shouldn't be null.", !myResponseStr.equals("null"));
    }
}
