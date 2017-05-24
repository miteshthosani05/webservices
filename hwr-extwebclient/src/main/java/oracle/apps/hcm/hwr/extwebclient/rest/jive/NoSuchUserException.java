/*******************************************************************************
 * Copyright 2013, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/

package oracle.apps.hcm.hwr.extwebclient.rest.jive;

public class NoSuchUserException extends RuntimeException {

	private static final long serialVersionUID = -6122202447905303363L;

	public NoSuchUserException(String pUserName) {
		super("The user: '" + pUserName + "' does not exist in this Jive server.");
	}

}
