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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.customization.external;

import com.sun.ts.tests.jaxws.ee.w2j.document.literal.customization.external.custom.pkg.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.common.*;

import java.net.*;
import java.util.*;

import jakarta.xml.ws.*;
import javax.xml.namespace.QName;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  // need to create jaxbContext
  private static final ObjectFactory of = new ObjectFactory();

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.customization.external.";

  // service and port information
  private static final String NAMESPACEURI = "http://customizationexternaltest.org/wsdl";

  private static final String SERVICE_NAME = "myService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "w2jcustomizationexternaltest.endpoint.1";

  private static final String WSDLLOC_URL = "w2jcustomizationexternaltest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  Hello port = null;

  static CustomizationExternalTestService service = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPortStandalone() throws Exception {
    port = (Hello) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        CustomizationExternalTestService.class, PORT_QNAME, Hello.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (Hello) JAXWS_Util.getPort(service, PORT_QNAME, Hello.class);
    // port = (Hello) service.getMyHelloPort();
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
        service = (CustomizationExternalTestService) getSharedObject();
        getTestURLs();
        getPortJavaEE();
      }
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
   * @testName: CustomizationExternalTest
   *
   * @assertion_ids: JAXWS:SPEC:8000; JAXWS:SPEC:8001; JAXWS:SPEC:8003;
   * JAXWS:SPEC:8004; JAXWS:SPEC:8005; JAXWS:SPEC:8006; JAXWS:SPEC:8007;
   * JAXWS:SPEC:8008; JAXWS:SPEC:8010; JAXWS:SPEC:8012; JAXWS:SPEC:8013;
   * JAXWS:SPEC:2064; JAXWS:SPEC:7000; JAXWS:SPEC:8009;
   *
   * @test_Strategy: An external customization file is used to change aspects of
   * the wsdl file. If the endpoint is reachable then the customization worked.
   *
   */
  public void CustomizationExternalTest() throws Fault {
    TestUtil.logTrace("CustomizationExternalTest");
    boolean pass = true;
    String reqStr = "Hello";
    String resStr = "Hello, World!";
    try {
      HelloElement helloReq = of.createHelloElement();
      helloReq.setArgument(reqStr);
      Holder<HelloElement> holder = new Holder<HelloElement>();
      holder.value = helloReq;
      port.myHello(holder);
      String result = holder.value.getArgument();
      TestUtil.logMsg("result=" + result);
      if (!result.equals(resStr)) {
        TestUtil.logErr("expected: " + resStr + ", received: " + result);
        pass = false;
      }
      TestUtil.logMsg("Testing Fault Exception Case ...");
      helloReq.setArgument("Exception Case");
      holder.value = helloReq;
      try {
        port.myHello(holder);
        TestUtil.logErr(
            "CustomizationExternalTestException expected but not thrown");
        pass = false;
      } catch (CustomizationExternalTestException e) {
        TestUtil.logMsg("Got expected CustomizationExternalTestException");
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }

    if (!pass)
      throw new Fault("CustomizationExternalTest failed");
  }

}
