/*
 * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecyclecdijsf;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@ManagedBean("OverrideWithPostConstructBean")
public class OverrideWithPostConstructBean extends OverrideBeanBase {
  private static final String simpleName = "OverrideWithPostConstructBean";

  @PostConstruct
  @Override
  protected void postConstructInOverrideBeanBase() {
    historySingletonBean.addPostConstructRecordFor(this, simpleName);
  }

  @Override
  @PreDestroy
  protected void preDestroyInOverrideBeanBase() {
    Helper.getLogger().info("In preDestroyInOverrideBeanBase of " + this);
  }
}
