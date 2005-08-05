/* $RCSfile$
 * $Author$    
 * $Date$    
 * $Revision$
 * 
 * Copyright (C) 2004-2005  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. 
 * 
 */
package org.openscience.cdk.test.exception;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.openscience.cdk.exception.NoSuchAtomException;
import org.openscience.cdk.test.CDKTestCase;

/**
 * Checks the funcitonality of the NoSuchAtomException class.
 *
 * @cdk.module test
 *
 * @see org.openscience.cdk.NoSuchAtomException
 */
public class NoSuchAtomExceptionTest extends CDKTestCase {

    public NoSuchAtomExceptionTest(String name) {
        super(name);
    }

    public void setUp() {}

    public static Test suite() {
        return new TestSuite(NoSuchAtomExceptionTest.class);
    }
    
    public void testNoSuchAtomException_String() {
        final String EXPLANATION = "Buckybull is not an element!";
        NoSuchAtomException exception = new NoSuchAtomException(EXPLANATION);
        assertNotNull(exception);
        assertEquals(EXPLANATION, exception.getMessage());
    }
}
