/*
 * Copyright (C) 2018  The Chemistry Development Kit (CDK) project
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version. All we ask is that proper credit is given
 * for our work, which includes - but is not limited to - adding the above
 * copyright notice to the beginning of your source code files, and to any
 * copyright notice that you may distribute with programs based on this work.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package org.openscience.cdk.smarts;

import org.junit.Test;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.isomorphism.matchers.Expr;
import org.openscience.cdk.isomorphism.matchers.IQueryAtomContainer;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainerCreator;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MolToQueryTest {

    private final SmilesParser smipar = new SmilesParser(SilentChemObjectBuilder.getInstance());

    private void test(String expected, String smi, Expr.Type... opts) throws InvalidSmilesException {
        IAtomContainer      mol   = smipar.parseSmiles(smi);
        IQueryAtomContainer query = QueryAtomContainer.create(mol, opts);
        String actual = Smarts.generate(query);
        assertThat(actual, is(expected));
    }

    @Test
    public void noOptsSpecified() throws InvalidSmilesException {
        test("*1~*~*~*~*~*~1~*", "c1cccnc1C");
    }

    @Test
    public void aromaticWithBonds() throws InvalidSmilesException {
        test("a1:a:a:a:a:a:1-A", "c1cccnc1C",
             Expr.Type.IS_AROMATIC,
             Expr.Type.IS_ALIPHATIC,
             Expr.Type.SINGLE_OR_AROMATIC);
    }

    @Test
    public void aromaticElementWithBonds() throws InvalidSmilesException {
        test("c1:c:c:c:n:c:1-*", "c1cccnc1C",
             Expr.Type.AROMATIC_ELEMENT,
             Expr.Type.SINGLE_OR_AROMATIC);
        test("c1:c:c:c:n:c:1-[#6]", "c1cccnc1C",
             Expr.Type.IS_AROMATIC,
             Expr.Type.ELEMENT,
             Expr.Type.SINGLE_OR_AROMATIC);
    }

    @Test
    public void pseudoAtoms() throws InvalidSmilesException {
        test("[#6]~[#6]~*", "CC*",
             Expr.Type.ELEMENT);
    }

    @Test
    public void elementAndDegree() throws InvalidSmilesException {
        test("[#6D2]1~[#6D2]~[#6D2]~[#6D2]~[#7D2]~[#6D3]~1~[#6D]", "c1cccnc1C",
             Expr.Type.ELEMENT, Expr.Type.DEGREE);
    }

    @Test public void test() {
        IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();
        IAtomContainer query2 = builder.newInstance(IAtomContainer.class);
        query2.addAtom(builder.newInstance(IAtom.class, "C"));
        query2.addAtom(builder.newInstance(IAtom.class, "C"));
        query2.addAtom(builder.newInstance(IAtom.class, "C"));
        query2.addAtom(builder.newInstance(IAtom.class, "C"));
        query2.addAtom(builder.newInstance(IAtom.class, "C"));
        query2.addAtom(builder.newInstance(IAtom.class, "C"));
        query2.addBond(0, 1, IBond.Order.DOUBLE);
        query2.addBond(1, 2, IBond.Order.SINGLE);
        query2.addBond(3, 0, IBond.Order.SINGLE);
        query2.addBond(0, 4, IBond.Order.SINGLE);
        query2.addBond(1, 5, IBond.Order.SINGLE);
        System.out.println(Smarts.generate(QueryAtomContainerCreator.createSymbolAndBondOrderQueryContainer(query2)));
    }
}