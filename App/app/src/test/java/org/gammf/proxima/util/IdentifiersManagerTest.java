package org.gammf.proxima.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class IdentifiersManagerTest {

    @Test
    public void initialStateTest() {
        assertEquals("", IdentifiersManager.getRescuerIdentifier());
        assertEquals("", IdentifiersManager.getPatientIdentifier());
    }

    @Test
    public void settersAndGettersTest() {
        final String expectedRescuer = "rescuer1";
        final String expectedPatient = "patient1";

        IdentifiersManager.setRescuerIdentifier(expectedRescuer);
        IdentifiersManager.setPatientIdentifier(expectedPatient);
        assertEquals(expectedRescuer, IdentifiersManager.getRescuerIdentifier());
        assertEquals(expectedPatient, IdentifiersManager.getPatientIdentifier());

        IdentifiersManager.setPatientIdentifier("");
        IdentifiersManager.setRescuerIdentifier("");
    }

}