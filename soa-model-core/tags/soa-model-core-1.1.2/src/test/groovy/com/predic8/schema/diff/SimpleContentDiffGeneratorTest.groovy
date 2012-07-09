/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.schema.diff


import com.predic8.soamodel.Consts;
import javax.xml.stream.*
import groovy.xml.*
import com.predic8.xml.util.*
import com.predic8.schema.*
import com.predic8.schema.restriction.BaseRestriction
import com.predic8.schema.restriction.facet.*

class SimpleContentDiffGeneratorTest extends GroovyTestCase{

  def a
  def b

  void setUp() {
    a = new SimpleContent(restriction: new BaseRestriction(base: new QName(Consts.SCHEMA_NS,'string')))
    b = new SimpleContent(restriction: new BaseRestriction(base: new QName(Consts.SCHEMA_NS,'string')))
    a.restriction.facets << new MaxLengthFacet(value : 10)
    a.restriction.facets << new LengthFacet(value : 3)
    b.restriction.facets << new LengthFacet(value : 9)
    a.restriction.facets << new EnumerationFacet(value : 'red')
    a.restriction.facets << new EnumerationFacet(value : 'green')
    b.restriction.facets << new EnumerationFacet(value: 'green')
    b.restriction.facets << new EnumerationFacet(value: 'blue')
    b.restriction.facets << new MinLengthFacet(value : 1)
  }

  void testEqual(){
    def diffGen = new SimpleContentDiffGenerator(a: a, b: b, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertTrue(diffs.diffs.description.toString().contains('EnumerartionFacet with value: red removed.'))
    assertTrue(diffs.diffs.description.toString().contains('EnumerartionFacet with value: blue added.'))
    assertTrue(diffs.diffs.description.toString().contains('Value of LengthFacet changed from 3 to 9.'))
    assertTrue(diffs.diffs.description.toString().contains('Facet MaxLengthFacet removed.'))
    assertTrue(diffs.diffs.description.toString().contains('Facet MinLengthFacet added.'))
  }

//  void testElementremoved(){
//    def diffGen = new ChoiceDiffGenerator(a: a , b: c, generator : new SchemaDiffGenerator())
//    def diffs = diffGen.compare()
//    assertEquals(1, diffs.size())
//    assertEquals(1, diffs[0].diffs.size())
//    assertTrue(diffs.diffs.description.toString().contains('removed'))
//  }
//
//  void testElementAdded(){
//    def diffGen = new ChoiceDiffGenerator(a: a , b: d, generator : new SchemaDiffGenerator())
//    def diffs = diffGen.compare()
//    assertEquals(1, diffs.size())
//    assertEquals(1, diffs[0].diffs.size())
//    assertTrue(diffs.diffs.description.toString().contains('added'))
//  }
//
//  void testSequenceAdded(){
//    def diffGen = new ChoiceDiffGenerator(a: a , b: e, generator : new SchemaDiffGenerator())
//    def diffs = diffGen.compare()
//    assertEquals(1, diffs.size())
//    assertEquals(1, diffs[0].diffs.size())
//    assertTrue(diffs.diffs.description.toString().contains('added'))
//  }
//
//  void testSequenceRemoved(){
//    def diffGen = new ChoiceDiffGenerator(a: e , b: a, generator : new SchemaDiffGenerator())
//    def diffs = diffGen.compare()
//    assertEquals(1, diffs.size())
//    assertEquals(1, diffs[0].diffs.size())
//    assertTrue(diffs.diffs.description.toString().contains('removed'))
//  }

}