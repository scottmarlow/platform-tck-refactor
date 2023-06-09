/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: URLClient.java 73856 2014-06-23 22:39:25Z sdimilla $
 */

package com.sun.ts.tests.servlet.api.jakarta_servlet.servletcontext40;

import java.net.InetAddress;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.WebUtil;

public class Client extends EETest {

  private static final String CONTEXT_ROOT = "/servlet_js_servletcontext40_web";

  private static final String PROTOCOL = "http";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private String hostname;

  private int portnum;

  private WebUtil.Response response = null;

  private String request = null;

  private TSURL tsurl = new TSURL();

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties p) throws Exception {
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
        pass = false;
      }
    } catch (Exception e) {
      throw new Exception("setup failed:", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Exception("setup failed:");
    }

    System.out.println(hostname);
    System.out.println(portnum);
    logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: addJspTest
   * 
   * @assertion_ids: NA
   * 
   * @test_Strategy:
   */
  public void addJspTest() throws Exception {
    try {
      request = CONTEXT_ROOT + "/servlet/addJspFile";
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Exception("addJspTest failed.");
      }

      if (response.content.indexOf("addJspFile is accessed") < 0) {
        TestUtil.logErr("The test jsp is accessed incorrectly: " + request);
        throw new Exception("addJspTest failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addJspTest failed: ", e);
    }
  }

  /*
   * @testName: changeSessionTimeoutTest
   * 
   * @assertion_ids: servlet40:changeSessionTimeoutTest;
   * 
   * @test_Strategy:
   */
  public void changeSessionTimeoutTest() throws Exception {
    try {
      request = CONTEXT_ROOT + "/TestServlet";
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Exception("changeSessionTimeoutTest failed.");
      }

      if (response.content.indexOf("changeSessionTimeout_correctly") < 0) {
        throw new Exception("changeSessionTimeoutTest failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("changeSessionTimeoutTest failed: ", e);
    }
  }

  /*
   * @testName: addJspContextInitialized
   * 
   * @assertion_ids: NA
   * 
   * @test_Strategy: IllegalStateException - if this ServletContext has already
   * been initialized
   */
  public void addJspContextInitialized() throws Exception {
    try {
      request = CONTEXT_ROOT + "/TestServlet2";
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Exception("addJspContextInitialized failed.");
      }

      if (response.content.indexOf(
          "IllegalStateException_when_addJsp__ContextInitialized") < 0) {
        TestUtil.logErr(
            "IllegalStateException should be thrown if this ServletContext has already been initialized");
        throw new Exception("addJspContextInitialized failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addJspContextInitialized failed: ", e);
    }
  }

  /*
   * @testName: setSessionTimeoutContextInitialized
   * 
   * @assertion_ids: NA
   * 
   * @test_Strategy: IllegalStateException - if this ServletContext has already
   * been initialized
   */
  public void setSessionTimeoutContextInitialized() throws Exception {
    try {
      request = CONTEXT_ROOT + "/TestServlet2";
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Exception("setSessionTimeoutContextInitialized failed.");
      }

      if (response.content.indexOf(
          "IllegalStateException_when_setSessionTimeout_ContextInitialized") < 0) {
        TestUtil.logErr(
            "IllegalStateException should be thrown if this ServletContext has already been initialized");
        throw new Exception("setSessionTimeoutContextInitialized failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setSessionTimeoutContextInitialized failed: ", e);
    }
  }

  /*
   * @testName: addJspContextListenerInTLD
   * 
   * @assertion_ids: NA
   * 
   * @test_Strategy: UnsupportedOperationException - if this ServletContext was
   * passed to the ServletContextListener.contextInitialized(jakarta.servlet.
   * ServletContextEvent) method of a ServletContextListener that was neither
   * declared in web.xml or web-fragment.xml, nor annotated with WebListener
   */
  public void addJspContextListenerInTLD() throws Exception {
    try {
      request = CONTEXT_ROOT + "/TestServlet";
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Exception("addJspContextListenerInTLD failed.");
      }

      if (response.content.indexOf(
          "UnsupportedOperationException_when_addJsp_addedListener") < 0) {
        TestUtil.logErr("UnsupportedOperationException should be thrown.");
        throw new Exception("addJspContextListenerInTLD failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addJspContextListenerInTLD failed: ", e);
    }
  }

  /*
   * @testName: setSessionTimeoutContextListenerInTLD
   * 
   * @assertion_ids: NA
   * 
   * @test_Strategy: UnsupportedOperationException - if this ServletContext was
   * passed to the ServletContextListener.contextInitialized(jakarta.servlet.
   * ServletContextEvent) method of a ServletContextListener that was neither
   * declared in web.xml or web-fragment.xml, nor annotated with WebListener
   */
  public void setSessionTimeoutContextListenerInTLD() throws Exception {
    try {
      request = CONTEXT_ROOT + "/TestServlet";
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Exception("setSessionTimeoutContextListenerInTLD failed.");
      }

      if (response.content.indexOf(
          "UnsupportedOperationException_when_setSessionTimeout_addedListener") < 0) {
        TestUtil.logErr("UnsupportedOperationException should be thrown.");
        throw new Exception("setSessionTimeoutContextListenerInTLD failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setSessionTimeoutContextListenerInTLD failed: ", e);
    }
  }

  /*
   * @testName: addJspEmptyAndNullName
   * 
   * @assertion_ids: NA
   * 
   * @test_Strategy: IllegalArgumentException - if servletName is null or an
   * empty String
   */
  public void addJspEmptyAndNullName() throws Exception {
    try {
      request = CONTEXT_ROOT + "/TestServlet";
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Exception("addJspEmptyAndNullName failed.");
      }

      if (response.content
          .indexOf("IllegalArgumentException_when_empty_name") < 0) {
        TestUtil.logErr(
            "IllegalArgumentException should be thrown if servletName is an empty String");
        throw new Exception("addJspEmptyAndNullName failed.");
      }

      if (response.content
          .indexOf("IllegalArgumentException_when_null_name") < 0) {
        TestUtil.logErr(
            "IllegalArgumentException should be thrown if servletName is null");
        throw new Exception("addJspEmptyAndNullName failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addJspEmptyAndNullName failed: ", e);
    }
  }

  /*
   * @testName: getAttributeWithNullName
   * 
   * @assertion_ids: NA
   * 
   * @test_Strategy: NullPointerException - if name is null
   */
  public void getAttributeWithNullName() throws Exception {
    try {
      request = CONTEXT_ROOT + "/TestServlet2";
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Exception("getAttributeWithNullName failed.");
      }

      if (response.content
          .indexOf("NullPointerException_when_getAttribute_with_null") < 0) {
        TestUtil.logErr(
            "NullPointerException should be thrown if getAttribute with a null name.");
        throw new Exception("getAttributeWithNullName failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getAttributeWithNullName failed: ", e);
    }
  }

  /*
   * @testName: getInitParameterWithNullName
   * 
   * @assertion_ids: NA
   * 
   * @test_Strategy: NullPointerException - if name is null
   */
  public void getInitParameterWithNullName() throws Exception {
    try {
      request = CONTEXT_ROOT + "/TestServlet2";
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Exception("getInitParameterWithNullName failed.");
      }

      if (response.content.indexOf(
          "NullPointerException_when_getInitParameter_with_null") < 0) {
        TestUtil.logErr(
            "NullPointerException should be thrown if getInitParameter with a null name.");
        throw new Exception("getAttributeWithNullName failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getInitParameterWithNullName failed: ", e);
    }
  }

  /*
   * @testName: setAttributeWithNullName
   * 
   * @assertion_ids: NA
   * 
   * @test_Strategy: NullPointerException - if name is null
   */
  public void setAttributeWithNullName() throws Exception {
    try {
      request = CONTEXT_ROOT + "/TestServlet2";
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Exception("setAttributeWithNullName failed.");
      }

      if (response.content
          .indexOf("NullPointerException_when_setAttribute_with_null") < 0) {
        TestUtil.logErr(
            "NullPointerException should be thrown if setAttribute with a null name.");
        throw new Exception("setAttributeWithNullName failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setAttributeWithNullName failed: ", e);
    }
  }

  /*
   * @testName: setInitParameterWithNullName
   * 
   * @assertion_ids: NA
   * 
   * @test_Strategy: NullPointerException - if name is null
   */
  public void setInitParameterWithNullName() throws Exception {
    try {
      request = CONTEXT_ROOT + "/TestServlet2";
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Exception("setInitParameterWithNullName failed.");
      }

      if (response.content.indexOf(
          "NullPointerException_when_setInitParameter_with_null") < 0) {
        TestUtil.logErr(
            "NullPointerException should be thrown if setInitParameter with a null name.");
        throw new Exception("setAttributeWithNullName failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setInitParameterWithNullName failed: ", e);
    }
  }

  /*
   * @testName: getEffectiveSessionTrackingModes
   * 
   * @assertion_ids: NA
   * 
   * @test_Strategy: with no setEffectiveSesssionTrackingModes, default is in
   * effective
   */
  public void getEffectiveSessionTrackingModes() throws Exception {
    try {
      request = CONTEXT_ROOT + "/TestServlet2";
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Exception("getEffectiveSessionTrackingModes failed.");
      }

      if (response.content
          .indexOf("getEffectiveSessionTrackingModes_test_passed") < 0) {
        TestUtil.logErr("getEffectiveSessionTrackingModes return a wrong set.");
        throw new Exception("getEffectiveSessionTrackingModes failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getEffectiveSessionTrackingModes failed: ", e);
    }
  }
}
