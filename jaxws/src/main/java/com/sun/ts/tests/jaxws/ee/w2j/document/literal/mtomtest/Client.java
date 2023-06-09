/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.mtomtest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.common.AttachmentHelper;

import java.net.*;

import java.awt.Image;
import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.SOAPBinding;
import javax.xml.namespace.QName;

import jakarta.activation.DataHandler;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import java.util.Properties;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.common.*;

public class Client extends ServiceEETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.mtomtest.";

  // service and port information
  private static final String NAMESPACEURI = "http://mtomtestservice.org/wsdl";

  private static final String SERVICE_NAME = "MTOMTestService";

  private static final String PORT_NAME1 = "MTOMTestPort";

  private static final String PORT_NAME2 = "MTOMTestTwoPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME1 = new QName(NAMESPACEURI, PORT_NAME1);

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private URL docURL1 = null;

  private URL docURL2 = null;

  private URL docURL3 = null;

  private URL docURL4 = null;

  private URL docURL11 = null;

  private URL docURL12 = null;

  private URL docURL13 = null;

  private URL docURL14 = null;

  private URL docURLBigJpeg = null;

  String SDOC1 = "WSW2JDLMTOMTesttext.xml";

  String SDOC2 = "WSW2JDLMTOMTestapplication.xml";

  String SDOC3 = "WSW2JDLMTOMTestattach.html";

  String SDOC4 = "WSW2JDLMTOMTestattach.jpg";

  String SDOC11 = "WSW2JDLMTOMTesttext2.xml";

  String SDOC12 = "WSW2JDLMTOMTestapplication2.xml";

  String SDOC13 = "WSW2JDLMTOMTestattach2.html";

  String SDOC14 = "WSW2JDLMTOMTestattach2.jpg";

  String SDOCBigJpeg = "WSW2JDLMTOMTestbig.jpg";

  // URL properties used by the test
  private static final String ENDPOINT_URL = "w2jdlmtomtest.endpoint.1";

  private static final String WSDLLOC_URL = "w2jdlmtomtest.wsdlloc.1";

  private static final String CTXROOT = "w2jdlmtomtest.ctxroot.1";

  private String url = null;

  private URL wsdlurl = null;

  private String ctxroot = null;

  private MTOMTest port = null;

  private MTOMClientTwo client2;

  static MTOMTestService service = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    ctxroot = JAXWS_Util.getURLFromProp(CTXROOT);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
    TestUtil.logMsg("Context Root:         " + ctxroot);
  }

  private void getPortStandalone() throws Exception {
    port = (MTOMTest) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        MTOMTestService.class, PORT_QNAME1, MTOMTest.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
    SOAPBinding binding = (SOAPBinding) ((BindingProvider) port).getBinding();
    binding.setMTOMEnabled(true);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (MTOMTest) service.getPort(MTOMTest.class);
    SOAPBinding binding = (SOAPBinding) ((BindingProvider) port).getBinding();
    binding.setMTOMEnabled(true);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    JAXWS_Util.dumpTargetEndpointAddress(port);
    // JAXWS_Util.setSOAPLogging(port);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
    boolean pass = true;
    client2 = (MTOMClientTwo) ClientFactory.getClient(MTOMClientTwo.class, p,
        this, service);

    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);

      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;

      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getPortStandalone();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (MTOMTestService) getSharedObject();
        getTestURLs();
        getPortJavaEE();
      }
      docURL1 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC1);
      docURL2 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC2);
      docURL3 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC3);
      docURL4 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC4);
      docURL11 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC11);
      docURL12 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC12);
      docURL13 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC13);
      docURL14 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC14);
      docURLBigJpeg = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOCBigJpeg);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
    }

    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: MTOMInTest
   *
   * @assertion_ids: JAXWS:SPEC:10013; JAXWS:SPEC:10014; WS4EE:SPEC:5006;
   * WS4EE:SPEC:4013; JAXWS:SPEC:10022; JAXWS:SPEC:2080; JAXWS:SPEC:3054;
   *
   * @test_Strategy: Test MTOM attachments on input.
   */
  public void MTOMInTest() throws Fault {
    TestUtil.logMsg("MTOMInTest");
    boolean pass = true;

    try {
      DataType data = new DataType();

      data.setDocName1(SDOC1);
      data.setDocName2(SDOC2);
      data.setDocName3(SDOC3);
      data.setDocName4(SDOC4);

      data.setDocUrl1(docURL1.toString());
      data.setDocUrl2(docURL2.toString());
      data.setDocUrl3(docURL3.toString());
      data.setDocUrl4(docURL4.toString());

      StreamSource doc1 = AttachmentHelper.getSourceDoc(docURL1);
      StreamSource doc2 = AttachmentHelper.getSourceDoc(docURL2);
      DataHandler doc3 = AttachmentHelper.getDataHandlerDoc(docURL3);
      Image doc4 = AttachmentHelper.getImageDoc(docURL4);

      data.setDoc1(doc1);
      data.setDoc2(doc2);
      data.setDoc3(doc3);
      data.setDoc4(doc4);

      TestUtil
          .logMsg("Send 4 documents using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Documents to send: [" + SDOC1 + "," + SDOC2 + "," + SDOC3
          + "," + SDOC4 + "]");
      String result = port.mtomIn(data);
      if (!result.equals("")) {
        TestUtil
            .logErr("An error occurred with one or more of the attachments");
        TestUtil.logErr("result=" + result);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("MTOMInTest failed");
  }

  /*
   * @testName: MTOMInOutTest
   *
   * @assertion_ids: JAXWS:SPEC:10013; JAXWS:SPEC:10014; WS4EE:SPEC:5006;
   * WS4EE:SPEC:4013; JAXWS:SPEC:10022; JAXWS:SPEC:2080; JAXWS:SPEC:3054;
   *
   * @test_Strategy: Test MTOM attachments on input and output.
   */
  public void MTOMInOutTest() throws Fault {
    TestUtil.logMsg("MTOMInOutTest");
    boolean pass = true;

    try {
      Holder<String> hDocName1 = new Holder<String>(SDOC1);
      Holder<String> hDocName2 = new Holder<String>(SDOC2);
      Holder<String> hDocName3 = new Holder<String>(SDOC3);
      Holder<String> hDocName4 = new Holder<String>(SDOC4);

      Holder<String> hDocUrl1 = new Holder<String>(docURL1.toString());
      Holder<String> hDocUrl2 = new Holder<String>(docURL2.toString());
      Holder<String> hDocUrl3 = new Holder<String>(docURL3.toString());
      Holder<String> hDocUrl4 = new Holder<String>(docURL4.toString());
      Holder<String> hDocUrl11 = new Holder<String>(docURL11.toString());
      Holder<String> hDocUrl12 = new Holder<String>(docURL12.toString());
      Holder<String> hDocUrl13 = new Holder<String>(docURL13.toString());
      Holder<String> hDocUrl14 = new Holder<String>(docURL14.toString());

      StreamSource doc1 = AttachmentHelper.getSourceDoc(docURL1);
      StreamSource doc2 = AttachmentHelper.getSourceDoc(docURL2);
      DataHandler doc3 = AttachmentHelper.getDataHandlerDoc(docURL3);
      Image doc4 = AttachmentHelper.getImageDoc(docURL4);

      Holder<Source> hDoc1 = new Holder<Source>(doc1);
      Holder<Source> hDoc2 = new Holder<Source>(doc2);
      Holder<DataHandler> hDoc3 = new Holder<DataHandler>(doc3);
      Holder<Image> hDoc4 = new Holder<Image>(doc4);
      Holder<String> hResult = new Holder<String>("");
      TestUtil.logMsg(
          "Send and receive 4 documents using MTOM via webservice method mtomInOut()");
      TestUtil.logMsg("Documents to send: [" + SDOC1 + "," + SDOC2 + "," + SDOC3
          + "," + SDOC4 + "]");
      TestUtil.logMsg("Documents to receive: [" + SDOC11 + "," + SDOC12 + ","
          + SDOC13 + "," + SDOC14 + "]");
      port.mtomInOut(hDocName1, hDocName2, hDocName3, hDocName4, hDocUrl1,
          hDocUrl2, hDocUrl3, hDocUrl4, hDocUrl11, hDocUrl12, hDocUrl13,
          hDocUrl14, hDoc1, hDoc2, hDoc3, hDoc4, hResult);
      if (!(hResult.value).equals("")) {
        TestUtil.logErr("Server-side errors occurred:\n" + hResult.value);
        pass = false;
      }
      TestUtil.logMsg("Verify the contents of the received documents");

      doc1 = AttachmentHelper.getSourceDoc(docURL11);
      doc2 = AttachmentHelper.getSourceDoc(docURL12);
      doc3 = AttachmentHelper.getDataHandlerDoc(docURL13);
      doc4 = AttachmentHelper.getImageDoc(docURL14);

      // Now test the documents that were sent back by Server
      String tmpRes = AttachmentHelper.validateAttachmentData(doc1, hDoc1.value,
          SDOC11);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error: " + tmpRes);
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc2, hDoc2.value,
          SDOC12);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error: " + tmpRes);
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc3, hDoc3.value,
          SDOC13);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error: " + tmpRes);
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc4, hDoc4.value,
          SDOC14);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error: " + tmpRes);
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("All received documents are as expected (ok)");

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("MTOMInOutTest failed");
  }

  /*
   * @testName: MTOMOutTest
   *
   * @assertion_ids: JAXWS:SPEC:10013; JAXWS:SPEC:10014; WS4EE:SPEC:5006;
   * WS4EE:SPEC:4013; JAXWS:SPEC:10022; JAXWS:SPEC:2080; JAXWS:SPEC:3054;
   *
   * @test_Strategy: Test MTOM attachments on output.
   */
  public void MTOMOutTest() throws Fault {
    TestUtil.logMsg("MTOMOutTest");
    boolean pass = true;

    try {

      StreamSource doc1 = AttachmentHelper.getSourceDoc(docURL1);
      StreamSource doc2 = AttachmentHelper.getSourceDoc(docURL2);
      DataHandler doc3 = AttachmentHelper.getDataHandlerDoc(docURL3);
      Image doc4 = AttachmentHelper.getImageDoc(docURL4);

      String urls = docURL1.toString() + "," + docURL2.toString() + ","
          + docURL3.toString() + "," + docURL4.toString();
      TestUtil.logTrace("urls=" + urls);
      TestUtil.logMsg(
          "Receive 4 documents using MTOM via webservice method mtomOut()");
      TestUtil.logMsg("Documents to receive: [" + SDOC1 + "," + SDOC2 + ","
          + SDOC3 + "," + SDOC4 + "]");
      DataType data = port.mtomOut(urls);
      TestUtil.logMsg("Verify the contents of the received documents");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc1,
          data.getDoc1(), SDOC1);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error: " + tmpRes);
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc2, data.getDoc2(),
          SDOC2);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error: " + tmpRes);
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc3, data.getDoc3(),
          SDOC3);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error: " + tmpRes);
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc4, data.getDoc4(),
          SDOC4);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error: " + tmpRes);
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("All received documents are as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("MTOMOutTest failed");
  }

  /*
   * @testName: MTOMInOut2RequestCheckHttpHeadersTest
   *
   * @assertion_ids: JAXWS:SPEC:10013; JAXWS:SPEC:10014; WS4EE:SPEC:5006;
   * WS4EE:SPEC:4013; JAXWS:SPEC:10022; JAXWS:SPEC:10024; JAXWS:SPEC:2080;
   * JAXWS:SPEC:3054;
   *
   * @test_Strategy: Test MTOM HTTP Content-Type Header for correctness.
   */
  public void MTOMInOut2RequestCheckHttpHeadersTest() throws Fault {
    TestUtil.logMsg("MTOMInOut2RequestCheckHttpHeadersTest");
    boolean pass = true;

    try {
      Image doc = AttachmentHelper.getImageDoc(docURLBigJpeg);
      Holder<Image> hDoc = new Holder<Image>(doc);
      Holder<String> hString = new Holder<String>(
          "MTOMInOut2RequestCheckHttpHeadersTest");
      TestUtil.logMsg("Check Content-Type HTTP Request header for correctness");
      client2.mtomInOut2(hString, hDoc);
      TestUtil.logMsg("Content-Type HTTP Request header was correct (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("MTOMInOut2RequestCheckHttpHeadersTest failed");
  }

  /*
   * @testName: MTOMInOut2ResponseCheckHttpHeadersTest
   *
   * @assertion_ids: JAXWS:SPEC:10013; JAXWS:SPEC:10014; WS4EE:SPEC:5006;
   * WS4EE:SPEC:4013; JAXWS:SPEC:10022; JAXWS:SPEC:10024;
   *
   * @test_Strategy: Test MTOM HTTP Content-Type Header for correctness.
   */
  public void MTOMInOut2ResponseCheckHttpHeadersTest() throws Fault {
    TestUtil.logMsg("MTOMInOut2ResponseCheckHttpHeadersTest");
    boolean pass = true;

    try {
      Image doc = AttachmentHelper.getImageDoc(docURLBigJpeg);
      Holder<Image> hDoc = new Holder<Image>(doc);
      Holder<String> hString = new Holder<String>(
          "MTOMInOut2ResponseCheckHttpHeadersTest");
      TestUtil
          .logMsg("Check Content-Type HTTP Response header for correctness");
      client2.mtomInOut2(hString, hDoc);
      TestUtil.logMsg("Content-Type HTTP Response header was correct (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("MTOMInOut2ResponseCheckHttpHeadersTest failed");
  }

  /*
   * @testName: MTOMOut2Test
   *
   * @assertion_ids: JAXWS:SPEC:10013; JAXWS:SPEC:10014; WS4EE:SPEC:5006;
   * WS4EE:SPEC:4013; JAXWS:SPEC:2080; JAXWS:SPEC:3054;
   *
   * @test_Strategy: Test MTOM attachments on output.
   */
  public void MTOMOut2Test() throws Fault {
    TestUtil.logMsg("MTOMOut2Test");
    boolean pass = true;

    try {

      Image doc = AttachmentHelper.getImageDoc(docURLBigJpeg);
      String urls = docURLBigJpeg.toString();
      TestUtil.logMsg("urls=" + urls);
      TestUtil.logMsg(
          "Receive 1 document using MTOM via webservice method mtomOut2()");
      TestUtil.logMsg("Document to receive: [" + SDOCBigJpeg + "]");
      DataType3 data = port.mtomOut2(urls);
      TestUtil.logMsg("Verify the content of the received document");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc,
          data.getDoc(), SDOCBigJpeg);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error: " + tmpRes);
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("The received document is as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("MTOMOut2Test failed");
  }
}
