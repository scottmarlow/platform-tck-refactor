/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:$
 */
package com.sun.ts.tests.servlet.api.jakarta_servlet_http.asynccontext;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

  String CONTEXT_ROOT = "/servlet_jsh_asynccontext_web";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  @Override
  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setServletName("AsyncTestServlet");
    setContextRoot(CONTEXT_ROOT);

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * Run test
   */
  /*
   * Test set up:
   *
   * Define two servets AsyncTestServlet and AsyncTests that supports async in
   * web.xml; Define three AsyncListeners using annotation: ACListener,
   * ACListener1, ACListenerBad; - ACListenerBad does not instantiate; Define a
   * ServletRequestWrapper that wraps the original request; Define a
   * ServletResponseWrapper that wraps the original response;
   *
   */

  /*
   * @testName: dispatchZeroArgTest
   * 
   * @assertion_ids: Servlet:JAVADOC:639; Servlet:JAVADOC:639.2;
   * Servlet:JAVADOC:639.4; Servlet:JAVADOC:703; Servlet:JAVADOC:707;
   * Servlet:JAVADOC:708; Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which supports async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(); call ac.dispatch(); call
   * ServletRequest.isAsyncSupported() call ServletRequest.isAsyncStarted() call
   * ServletRequest.getDispatcherType() verifies all work accordingly.
   */
  public void dispatchZeroArgTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "dispatchZeroArgTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "ASYNC_NOT_STARTED_dispatchZeroArgTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=REQUEST|"
            + "ASYNC_STARTED_dispatchZeroArgTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=ASYNC");
    invoke();
  }

  /*
   * @testName: dispatchZeroArgTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:639; Servlet:JAVADOC:639.2;
   * Servlet:JAVADOC:639.4; Servlet:JAVADOC:703; Servlet:JAVADOC:707;
   * Servlet:JAVADOC:708; Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which supports async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(); call ac.dispatch(); call
   * ServletRequest.isAsyncSupported() call ServletRequest.isAsyncStarted() call
   * ServletRequest.getDispatcherType() verifies all work accordingly.
   */
  public void dispatchZeroArgTest1() throws Exception {
    TEST_PROPS.setProperty(APITEST, "dispatchZeroArgTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "ASYNC_NOT_STARTED_dispatchZeroArgTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=REQUEST|"
            + "ASYNC_STARTED_dispatchZeroArgTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=ASYNC");
    invoke();
  }

  /*
   * @testName: dispatchContextPathTest @assertion_ids: Servlet:JAVADOC:641;
   * Servlet:JAVADOC:703; Servlet:JAVADOC:707; Servlet:JAVADOC:708;
   * Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create two Servlets AsyncTestServlet and AsynTest both
   * support async; Client send a request to AsyncTestServlet; StartAsync in
   * AsyncTestServlet ServletRequest.startAsync(); call
   * ac.dispatch(ServltContext, path to AsynTest); call
   * ServletRequest.isAsyncSupported() call ServletRequest.isAsyncStarted() call
   * ServletRequest.getDispatcherType() verifies all work accordingly.
   */
  public void dispatchContextPathTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "dispatchContextPathTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "ASYNC_NOT_STARTED_dispatchContextPathTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=REQUEST|"
            + "ASYNC_STARTED_asyncTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=ASYNC");
    invoke();
  }

  /*
   * @testName: getRequestTest @assertion_ids: Servlet:JAVADOC:642;
   * Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(); call ac.getRequest() verifies it works.
   */
  public void getRequestTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getRequestTest");
    invoke();
  }

  /*
   * @testName: asyncListenerTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:649; Servlet:JAVADOC:710;
   * Servlet:JAVADOC:846; Servlet:JAVADOC:866; Servlet:JAVADOC:873;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Create an AsyncListenerBad; Client send a request to AsyncTestServlet;
   * StartAsync in AsyncTestServlet; AsyncContext.createistener(clazz) verifies
   * ServletException is thrown when clazz fails to be instantiated.
   */
  public void asyncListenerTest1() throws Exception {
    TEST_PROPS.setProperty(APITEST, "asyncListenerTest1");
    invoke();
  }

  /*
   * @testName: timeOutTest
   * 
   * @assertion_ids: Servlet:JAVADOC:710; Servlet:JAVADOC:846;
   * Servlet:JAVADOC:868; Servlet:JAVADOC:869;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet;
   * AsyncContext.setTimeout(L) verifies it works using getTimeout.
   */
  public void timeOutTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "timeOutTest");
    invoke();
  }

  /*
   * @testName: timeOutTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:651; Servlet:JAVADOC:710;
   * Servlet:JAVADOC:846; Servlet:JAVADOC:868; Servlet:JAVADOC:869;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet;
   * AsyncContext.setTimeout(L) verifies it works by letting it timeout.
   */
  public void timeOutTest1() throws Exception {
    TEST_PROPS.setProperty(APITEST, "timeOutTest1");
    TEST_PROPS.setProperty(SEARCH_STRING, "in onTimeout method of ACListener2");
    TEST_PROPS.setProperty(STATUS_CODE, "-1");
    invoke();
  }

  /*
   * @testName: originalRequestTest
   * 
   * @assertion_ids: Servlet:JAVADOC:638; Servlet:JAVADOC:644;
   * Servlet:JAVADOC:710; Servlet:JAVADOC:710.2;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; Call ServletRequest.startAsync()
   * in AsyncTestServlet; verifies AsyncContext.hasOriginalRequestAndResponse
   * works.
   */
  public void originalRequestTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "originalRequestTest");
    invoke();
  }

  /*
   * @testName: originalRequestTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:638; Servlet:JAVADOC:644;
   * Servlet:JAVADOC:712; Servlet:JAVADOC:712.1; Servlet:JAVADOC:712.3;
   * * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; Call
   * ServletRequest.startAsync(ServletRequest, ServletResponse); verifies
   * AsyncContext.hasOriginalRequestAndResponse works.
   */
  public void originalRequestTest1() throws Exception {
    TEST_PROPS.setProperty(APITEST, "originalRequestTest1");
    invoke();
  }

  /*
   * @testName: originalRequestTest2
   * 
   * @assertion_ids: Servlet:JAVADOC:638; Servlet:JAVADOC:644;
   * Servlet:JAVADOC:712; Servlet:JAVADOC:712.1; Servlet:JAVADOC:712.3;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; Call
   * ServletRequest.startAsync(ServletRequestWrapper, ServletResponseWrapper);
   * verifies AsyncContext.hasOriginalRequestAndResponse works.
   */
  public void originalRequestTest2() throws Exception {
    TEST_PROPS.setProperty(APITEST, "originalRequestTest2");
    invoke();
  }

  /*
   * @testName: originalRequestTest3
   * 
   * @assertion_ids: Servlet:JAVADOC:638; Servlet:JAVADOC:644;
   * Servlet:JAVADOC:712; Servlet:JAVADOC:712.1; Servlet:JAVADOC:712.3;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; Call
   * ServletRequest.startAsync(ServletRequestWrapper, ServletResponse); verifies
   * AsyncContext.hasOriginalRequestAndResponse works.
   */
  public void originalRequestTest3() throws Exception {
    TEST_PROPS.setProperty(APITEST, "originalRequestTest3");
    invoke();
  }

  /*
   * @testName: originalRequestTest4
   * 
   * @assertion_ids: Servlet:JAVADOC:638; Servlet:JAVADOC:644;
   * Servlet:JAVADOC:712; Servlet:JAVADOC:712.1; Servlet:JAVADOC:712.3;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; Call
   * ServletRequest.startAsync(ServletRequest, ServletResponseWrapper); verifies
   * AsyncContext.hasOriginalRequestAndResponse works.
   */
  public void originalRequestTest4() throws Exception {
    TEST_PROPS.setProperty(APITEST, "originalRequestTest4");
    invoke();
  }
}
