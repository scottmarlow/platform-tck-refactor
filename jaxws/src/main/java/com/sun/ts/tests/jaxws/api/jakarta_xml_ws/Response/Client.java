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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.Response;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.tests.jaxws.common.*;

import com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.*;

import java.net.*;
import java.util.*;

import jakarta.xml.ws.*;

import javax.xml.namespace.QName;

import jakarta.xml.bind.JAXBContext;

import javax.xml.transform.Source;

public class Client extends ServiceEETest {
  // need to create jaxbContext
  private static final ObjectFactory of = new ObjectFactory();

  private String helloReq = "<HelloRequest xmlns=\"http://helloservice.org/types\"><argument>foo</argument></HelloRequest>";

  private String helloResp = "<HelloResponse xmlns=\"http://helloservice.org/types\"><argument>foo</argument></HelloResponse>";

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.api.jakarta_xml_ws.Response.";

  private static final String SHARED_CLIENT_PKG = "com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.";

  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME;

  private QName PORT_QNAME;

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "dlhelloservice.endpoint.1";

  private static final String WSDLLOC_URL = "dlhelloservice.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private Dispatch<Object> dispatchJaxb = null;

  private Dispatch<Source> dispatchSrc = null;

  private static final Class JAXB_OBJECT_FACTORY = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.ObjectFactory.class;

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.HelloService.class;

  static HelloService service = null;

  private JAXBContext createJAXBContext() {
    try {
      return JAXBContext.newInstance(JAXB_OBJECT_FACTORY);
    } catch (jakarta.xml.bind.JAXBException e) {
      throw new WebServiceException(e.getMessage(), e);
    }
  }

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
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

    // Initialize QNAMES used in the test
    SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);
    PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

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
        TestUtil.logMsg("Create Service object");
        getTestURLs();
        service = (HelloService) JAXWS_Util.getService(wsdlurl, SERVICE_QNAME,
            SERVICE_CLASS);
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (HelloService) getSharedObject();
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

  private Dispatch<Object> createDispatchJAXB() throws Exception {
    return service.createDispatch(PORT_QNAME, createJAXBContext(),
        jakarta.xml.ws.Service.Mode.PAYLOAD);
  }

  private Dispatch<Source> createDispatchSource() throws Exception {
    return service.createDispatch(PORT_QNAME, Source.class,
        jakarta.xml.ws.Service.Mode.PAYLOAD);
  }

  /*
   * @testName: getXMLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:9; WS4EE:SPEC:4005; WS4EE:SPEC:4006;
   * WS4EE:SPEC:4007;
   *
   * @test_Strategy: Get a Response<Object> using the invokeAsync method passing
   * an stream that contains xml and verify the response returned via "get()" is
   * correct
   */
  public void getXMLTest() throws Fault {
    TestUtil.logTrace("getXMLTest");
    boolean pass = true;
    Collection<Source> requestList = new ArrayList();
    requestList.add(JAXWS_Util.makeSource(helloReq, "DOMSource"));
    requestList.add(JAXWS_Util.makeSource(helloReq, "StreamSource"));
    requestList.add(JAXWS_Util.makeSource(helloReq, "SAXSource"));
    Collection<Source> responseList = new ArrayList();
    responseList.add(JAXWS_Util.makeSource(helloResp, "DOMSource"));
    responseList.add(JAXWS_Util.makeSource(helloResp, "StreamSource"));
    responseList.add(JAXWS_Util.makeSource(helloResp, "SAXSource"));
    Collection<String> typeList = new ArrayList();
    typeList.add("DOMSource");
    typeList.add("StreamSource");
    typeList.add("SAXSource");
    int i = 0;
    for (Iterator<Source> iter = requestList.iterator(); iter.hasNext();) {
      try {
        Source requestObject = iter.next();

        Source sourceResponse = (Source) ((List) responseList).get(i);
        String sSrcResponse = JAXWS_Util.getDOMResultAsString(
            JAXWS_Util.getSourceAsDOMResult(sourceResponse));
        dispatchSrc = createDispatchSource();
        TestUtil.logMsg("Send: " + sSrcResponse);
        Response<Source> roResponse = dispatchSrc.invokeAsync(requestObject);

        String dataType = (String) ((List) typeList).get(i);
        TestUtil.logMsg("Testing " + dataType + " data");
        TestUtil.logMsg("Polling and waiting for data ...");
        Object lock = new Object();
        while (!roResponse.isDone()) {
          synchronized (lock) {
            try {
              lock.wait(50);
            } catch (InterruptedException e) {
              // ignore
            }
          }
        }
        Source srcResponse = roResponse.get();
        String sResponse = JAXWS_Util
            .getDOMResultAsString(JAXWS_Util.getSourceAsDOMResult(srcResponse));
        TestUtil.logMsg("Recv: " + sResponse);
        if (sResponse.indexOf("HelloResponse") == -1
            || sResponse.indexOf("foo") == -1) {
          TestUtil.logErr("unexpected Response results");
          pass = false;
        }
      } catch (Exception e) {
        pass = false;
        e.printStackTrace();
      }
      i++;
    }
    if (!pass)
      throw new Fault("getXMLTest failed");
  }

  /*
   * @testName: getContextXMLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:9; JAXWS:JAVADOC:42; WS4EE:SPEC:4005;
   * WS4EE:SPEC:4006; WS4EE:SPEC:4007;
   *
   * @test_Strategy: Get a Response<Object> using the invokeAsync method passing
   * an stream that contains xml and get the JAXWSontext from that object
   */
  public void getContextXMLTest() throws Fault {
    TestUtil.logTrace("getContextXMLTest");
    boolean pass = true;
    Source sRequest = JAXWS_Util.makeSource(helloReq, "StreamSource");
    try {
      dispatchSrc = createDispatchSource();
      Response<Source> roResponse = dispatchSrc.invokeAsync(sRequest);
      TestUtil.logMsg("Polling and waiting for data ...");
      Object lock = new Object();
      while (!roResponse.isDone()) {
        synchronized (lock) {
          try {
            lock.wait(50);
          } catch (InterruptedException e) {
            // ignore
          }
        }
      }
      java.util.Map<String, Object> jrc = roResponse.getContext();
      if (jrc != null) {
        TestUtil.logMsg("Properties/Keys from java.util.Map<String,Object>:");
        int i = 1;
        for (Iterator iter = jrc.keySet().iterator(); iter.hasNext();) {
          TestUtil.logMsg("Property[" + i + "]=" + (String) iter.next());
          i++;
        }
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("getContextXMLTest failed");
  }

  /*
   * @testName: getJAXBTest
   *
   * @assertion_ids: JAXWS:JAVADOC:9; WS4EE:SPEC:4005; WS4EE:SPEC:4006;
   * WS4EE:SPEC:4007;
   *
   * @test_Strategy: Get a Response<Object> using the invokeAsync method passing
   * a JAXB Object and verify the response returned via "get()" is correct
   */
  public void getJAXBTest() throws Fault {
    TestUtil.logTrace("getJAXBTest");
    boolean pass = true;
    HelloRequest helloReq = null;
    String param = "foo";
    try {
      helloReq = of.createHelloRequest();
      helloReq.setArgument(param);
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr(
          "The follow exception was generated while creating the request object.");
      e.printStackTrace();
    }
    if (pass) {
      HelloResponse hResponse = null;
      try {
        dispatchJaxb = createDispatchJAXB();
        java.util.Map<String, Object> reqContext = dispatchJaxb
            .getRequestContext();
        Response<Object> res = dispatchJaxb.invokeAsync(helloReq);
        TestUtil.logMsg("Polling and waiting for data ...");
        Object lock = new Object();
        while (!res.isDone()) {
          synchronized (lock) {
            try {
              lock.wait(50);
            } catch (InterruptedException e) {
              // ignore
            }
          }
        }
        hResponse = (HelloResponse) res.get();
        String response = hResponse.getArgument();
        if (!helloReq.getArgument().equals(param)) {
          pass = false;
          TestUtil.logErr("The result return was in error:");
          TestUtil.logErr("     Expected result:" + param);
          TestUtil.logErr("     Actual result:" + response);
        } else {
          TestUtil.logMsg("Actual result:" + response);
        }
      } catch (Exception e) {
        pass = false;
        e.printStackTrace();
      }
    }
    if (!pass)
      throw new Fault("getJAXBTest failed");
  }

  /*
   * @testName: getContextJAXBTest
   *
   * @assertion_ids: JAXWS:JAVADOC:9; JAXWS:JAVADOC:42; WS4EE:SPEC:4005;
   * WS4EE:SPEC:4006; WS4EE:SPEC:4007;
   *
   * @test_Strategy: Get a Response<Object> using the invokeAsync method passing
   * an stream that contains xml and get the JAXWSontext from that object
   */
  public void getContextJAXBTest() throws Fault {
    TestUtil.logTrace("getContextJAXBTest");
    boolean pass = true;
    HelloRequest helloReq = null;
    String param = "foo";
    try {
      helloReq = of.createHelloRequest();
      helloReq.setArgument(param);
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr(
          "The follow exception was generated while creating the request object.");
      e.printStackTrace();
    }
    if (pass) {
      HelloResponse hResponse = null;
      try {
        dispatchJaxb = createDispatchJAXB();
        java.util.Map<String, Object> reqContext = dispatchJaxb
            .getRequestContext();
        Response<Object> res = dispatchJaxb.invokeAsync(helloReq);
        TestUtil.logMsg("Polling and waiting for data ...");
        Object lock = new Object();
        while (!res.isDone()) {
          synchronized (lock) {
            try {
              lock.wait(50);
            } catch (InterruptedException e) {
              // ignore
            }
          }
        }
        java.util.Map<String, Object> jrc = res.getContext();
        if (jrc != null) {
          TestUtil.logMsg("Properties/Keys from java.util.Map<String,Object>:");
          int i = 1;
          for (Iterator iter = jrc.keySet().iterator(); iter.hasNext();) {
            TestUtil.logMsg("Property[" + i + "]=" + (String) iter.next());
            i++;
          }
        }
      } catch (Exception e) {
        pass = false;
        e.printStackTrace();
      }
    }
    if (!pass)
      throw new Fault("getContextJAXBTest failed");
  }
}
