/*
 *  $RCSfile$
 *  $Author$
 *  $Date$
 *  *
 *  Copyright (C) 1997-2004  The Chemistry Development Kit (CDK) project
 *
 *  Contact: cdk-devel@list.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.openscience.cdk.test.modeling.forcefield;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;
import java.lang.*;
import java.util.*;
import javax.vecmath.*;

import org.openscience.cdk.modeling.forcefield.*;
import org.openscience.cdk.*;
import org.openscience.cdk.tools.HydrogenAdder;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.AtomTools;

/**
 *  Check results of GeometricMinimizer using some examples.
 *
 *@author         vlabarta
 *@created        February 14, 2005
 *@cdk.module     test
 *@cdk.created    2005-01-17
 */
public class ForceFieldTests extends TestCase {

	AtomContainer ac = null;
	GVector acCoordinates = new GVector(3);
	GeometricMinimizer gm = new GeometricMinimizer();
	Hashtable mmff94Tables = null;

	double[] molecule3Coord = {9, 9, 0};
	GVector molecule3Coordinates = new GVector(molecule3Coord);

	TestPotentialFunction tpf = new TestPotentialFunction();

	double[] testResult3C = {0, 0, 0};


	/**
	 *  Constructor for GeometricMinimizerTest object
	 */
	public ForceFieldTests() { }


	/**
	 *  A unit test suite for JUnit
	 *
	 *@return    The test suite
	 */
	public static Test suite() {
		return new TestSuite(ForceFieldTests.class);
	}


	/**
	 *  A unit test for JUnit (Steepest Descents Method minimization)
	 */
	public void testSteepestDescentsMinimization() {
		//System.out.println("");
		//System.out.println("FORCEFIELDTESTS with Steepest Descents Minimization");

		gm.setConvergenceParametersForSDM(5, 0.001);
		gm.steepestDescentsMinimization(molecule3Coordinates, tpf);

		for (int i = 0; i < molecule3Coordinates.getSize(); i++) {
			assertEquals(testResult3C[i], gm.getSteepestDescentsMinimum().getElement(i), 0.1);
		}
	}


	/**
	 *  A unit test for JUnit (Conjugate Gradient Method minimization)
	 */
	public void testConjugateGradientMinimization() {
		//System.out.println("");
		//System.out.println("FORCEFIELDTESTS with Conjugate Gradient Minimization");

		gm.setConvergenceParametersForCGM(2, 0.0001);
		gm.conjugateGradientMinimization(molecule3Coordinates, tpf);

		for (int i = 0; i < molecule3Coordinates.getSize(); i++) {
			assertEquals(testResult3C[i], gm.getConjugateGradientMinimum().getElement(i), 0.00001);
		}
	}


	/**
	 *  A unit test for JUnit (Newton-Raphson Method minimization)
	 */
	public void testNewtonRaphsonMinimization() {
		//System.out.println("");
		//System.out.println("FORCEFIELDTESTS with Newton-Raphson Minimization");

		gm.setConvergenceParametersForNRM(1, 0.0001);
		gm.newtonRaphsonMinimization(molecule3Coordinates, tpf);

		for (int i = 0; i < molecule3Coordinates.getSize(); i++) {
			assertEquals(testResult3C[i], gm.getNewtonRaphsonMinimum().getElement(i), 0.00001);
		}
	}


	public void createTestMolecule() throws ClassNotFoundException, CDKException, java.lang.Exception {
		HydrogenAdder hAdder = new HydrogenAdder();
		SmilesParser sp = new SmilesParser();
		ac = sp.parseSmiles("CC");
		hAdder.addExplicitHydrogensToSatisfyValency((Molecule) ac);
		Atom a = new Atom();
		a = ac.getAtomAt(0);
		Point3d atomCoordinate0 = new Point3d(1,0,0);
		a.setPoint3d(atomCoordinate0);
		ac.setAtomAt(0, a);
		a = ac.getAtomAt(1);
		Point3d atomCoordinate1 = new Point3d(2,0,0);
		a.setPoint3d(atomCoordinate1);
		ac.setAtomAt(1, a);
		AtomTools at = new AtomTools();
		at.add3DCoordinates1(ac);
		ForceField ff = new ForceField();
		acCoordinates.setSize(ac.getAtomCount() * 3);
		acCoordinates.set(ff.getCoordinates3xNVector(ac));
	}


	/**
	 *  A unit test for JUnit (BondStretching)
	 *
	 *@exception  ClassNotFoundException  Description of the Exception
	 *@exception  CDKException            Description of the Exception
	 *@exception  java.lang.Exception     Description of the Exception
	 */
	public void testBondStretching() throws ClassNotFoundException, CDKException, java.lang.Exception {
		/*System.out.println("");
		System.out.println("FORCEFIELDTESTS with Bond Stretching");
		*/
		double testResult_SumEB = 228.51003288118426;
		double[] testResult_gradientSumEB = {-1665.773538328216,-1665.773538328216,-1665.773538328216,
						-1665.773538328216,-1665.773538328216,-1665.773538328216,-1665.773538328216,
						-1665.773538328216,-1665.773538328216,-1665.773538328216,-1665.773538328216,
						-1665.773538328216,-1665.773538328216,-1665.773538328216,-1665.773538328216,
						-1665.773538328216,-1665.773538328216,-1665.773538328216,-1665.773538328216,
						-1665.773538328216,-1665.773538328216,-1665.773538328216,-1665.773538328216,
						-1665.773538328216};

		createTestMolecule();	
		
		gm.setMMFF94Tables(ac);
		mmff94Tables = gm.getMMFF94Tables();

		BondStretching bs = new BondStretching();
		bs.setMMFF94BondStretchingParameters(ac, mmff94Tables);

		//System.out.println("bs.functionMMFF94SumEB_InPoint(ac) = " + bs.functionMMFF94SumEB_InPoint(ac));
		assertEquals(testResult_SumEB, bs.functionMMFF94SumEB_InPoint(ac), 0.00001);
		
		//System.out.println("bs.gradientMMFF94SumEB_InPoint(ac) = " + bs.gradientMMFF94SumEB_InPoint(ac));
		
		for (int i = 0; i < testResult_gradientSumEB.length; i++) {
			assertEquals(testResult_gradientSumEB[i], bs.gradientMMFF94SumEB_InPoint(ac).getElement(i), 0.00001);
		}
		
		//System.out.println("hessian = " + bs.hessianInPoint(ac));


	}


	/**
	 *  A unit test for JUnit (AngleBending)
	 *
	 *@exception  ClassNotFoundException  Description of the Exception
	 *@exception  CDKException            Description of the Exception
	 *@exception  java.lang.Exception     Description of the Exception
	 */
	public void testAngleBending() throws ClassNotFoundException, CDKException, java.lang.Exception {
		/*System.out.println("");
		System.out.println("FORCEFIELDTESTS with Angle Bending");
		*/
		double testResult_SumEA = 2.6627825055933344E8;
		double[] testResult_gradientSumEA = {-7254575.574502064,-7254575.574502064,-7254575.574502064,-7254575.574502064,
						-7254575.574502064,-7254575.574502064,-7254575.574502064,-7254575.574502064,
						-7254575.574502064,-7254575.574502064,-7254575.574502064,-7254575.574502064,
						-7254575.574502064,-7254575.574502064,-7254575.574502064,-7254575.574502064,
						-7254575.574502064,-7254575.574502064,-7254575.574502064,-7254575.574502064,
						-7254575.574502064,-7254575.574502064,-7254575.574502064,-7254575.574502064};

		createTestMolecule();	

		gm.setMMFF94Tables(ac);
		mmff94Tables = gm.getMMFF94Tables();

		AngleBending ab = new AngleBending();
		ab.setMMFF94AngleBendingParameters(ac, mmff94Tables);

		//System.out.println("ab.functionMMFF94SumEA_InPoint(ac) = " + ab.functionMMFF94SumEA_InPoint(ac));
		assertEquals(testResult_SumEA, ab.functionMMFF94SumEA_InPoint(ac), 0.00001);
		
		//System.out.println("ab.gradientMMFF94SumEA_InPoint(ac) = " + ab.gradientMMFF94SumEA_InPoint(ac));
		
		for (int i = 0; i < testResult_gradientSumEA.length; i++) {
			assertEquals(testResult_gradientSumEA[i], ab.gradientMMFF94SumEA_InPoint(ac).getElement(i), 0.00001);
		}
		
		//System.out.println("hessian = " + ab.hessianInPoint(acCoordinates));
	}


	/**
	 *  A unit test for JUnit (StretchBendInteraction)
	 *
	 *@exception  ClassNotFoundException  Description of the Exception
	 *@exception  CDKException            Description of the Exception
	 *@exception  java.lang.Exception     Description of the Exception
	 */
	public void testStretchBendInteraction() throws ClassNotFoundException, CDKException, java.lang.Exception {
		/*
		System.out.println("");
		System.out.println("FORCEFIELDTESTS with StretchBendInteraction");
		*/
		double testResult_SumEBA = 18795.199851224905;
		double[] testResult_gradientSumEBA = {-81376.99250979327,-81376.99250979327,-81376.99250979327,-81376.99250979327,
						-81376.99250979327,-81376.99250979327,-81376.99250979327,-81376.99250979327,
						-81376.99250979327,-81376.99250979327,-81376.99250979327,-81376.99250979327,
						-81376.99250979327,-81376.99250979327,-81376.99250979327,-81376.99250979327,
						-81376.99250979327,-81376.99250979327,-81376.99250979327,-81376.99250979327,
						-81376.99250979327,-81376.99250979327,-81376.99250979327,-81376.99250979327};

		createTestMolecule();

		gm.setMMFF94Tables(ac);
		mmff94Tables = gm.getMMFF94Tables();

		StretchBendInteractions sbi = new StretchBendInteractions();
		sbi.setMMFF94StretchBendParameters(ac, mmff94Tables);

		//System.out.println("sbi.functionMMFF94SumEBA_InPoint(ac) = " + sbi.functionMMFF94SumEBA_InPoint(ac));
		assertEquals(testResult_SumEBA, sbi.functionMMFF94SumEBA_InPoint(ac), 0.00001);
		
		//System.out.println("sbi.gradientMMFF94SumEBA_InPoint(ac) = " + sbi.gradientMMFF94SumEBA_InPoint(ac));
		
		for (int i = 0; i < testResult_gradientSumEBA.length; i++) {
			assertEquals(testResult_gradientSumEBA[i], sbi.gradientMMFF94SumEBA_InPoint(ac).getElement(i), 0.00001);
		}
		
		//System.out.println("hessian = " + sbi.hessianInPoint(acCoordinates));
	}

}
