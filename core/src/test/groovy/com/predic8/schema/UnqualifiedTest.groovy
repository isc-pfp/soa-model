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

package com.predic8.schema

import junit.framework.TestCase
import javax.xml.stream.*
import groovy.xml.*

import com.predic8.xml.util.*
import com.predic8.wstool.creator.*

class UnqualifiedTest extends GroovyTestCase{
  
  def schema
  
  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse("/unqualified-locals.xsd")
}
  
  void testRequestCreatorUnqualified() {
    def strWriter = new StringWriter()
    def creator = new RequestCreator(builder: new MarkupBuilder(strWriter))
    def formParams=["xpath:/address/name":'Alice', "xpath:/address/location/city":"Bonn", "xpath:/address/phone/numbers":"324"]
    schema.getElement('address').create(creator, new RequestCreatorContext(formParams:formParams))
    def request = strWriter.toString()
    assert request =~ /<.*?:address/
    assert request =~ /<name/
    assert request =~ /<location/
    assert request =~ /<city/
    assert request =~ /<phone/    
    assert request =~ /<numbers/
  }
	
  void testRequestCreatorQualified() {
    def strWriter = new StringWriter()
    def creator = new RequestCreator(builder: new MarkupBuilder(strWriter))
    def formParams=["xpath:/address/name":'Alice', "xpath:/address/location/city":"Bonn", "xpath:/address/phone/numbers":"324"]
    schema.elementFormDefault = "qualified"
    schema.getElement('address').create(creator, new RequestCreatorContext(formParams:formParams))
    def request = strWriter.toString()
    assert request =~ /<.*?:address/   
    assert request =~ /<.*?:name/
    assert request =~ /<.*?:location/
    assert request =~ /<.*?:city/
    assert request =~ /<.*?:phone/    
    assert request =~ /<.*?:numbers/
  }

  void testRequestTemplateCreatorQualified() {
    def strWriter = new StringWriter()
    def creator = new RequestTemplateCreator(builder: new MarkupBuilder(strWriter))
    schema.elementFormDefault = "qualified"
    schema.getElement('address').create(creator, new RequestTemplateCreatorContext())
    def request = strWriter.toString()
    assert request =~ /<.*?:address/   
    assert request =~ /<.*?:name/
    assert request =~ /<.*?:location/
    assert request =~ /<.*?:city/
    assert request =~ /<.*?:phone/    
    assert request =~ /<.*?:numbers/    
  }

  void testRequestTemplateCreatorUnqualified() {
    def strWriter = new StringWriter()
    def creator = new RequestTemplateCreator(builder: new MarkupBuilder(strWriter))
    schema.getElement('address').create(creator, new RequestTemplateCreatorContext())
    def request = strWriter.toString()
    assert request =~ /<.*?:address/   
    assert request =~ /<.*?:name/
    assert request =~ /<.*?:location/
    assert request =~ /<.*?:city/
    assert request =~ /<.*?:phone/    
    assert request =~ /<.*?:numbers/    
  }
  
}
