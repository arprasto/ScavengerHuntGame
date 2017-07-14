/**
 *****************************************************************************
 * Copyright (c) 2017 IBM Corporation and other Contributors.

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Arpit Rastogi - Initial Contribution
 *****************************************************************************
 */
/*
 * Cloudant DB sample template class of JSON document structure stored in DB.
 */

package com.ibm.watson.scavenger.CloudantNoSQLDB;

public class JSonDocumentTemplateClass {
	private String img_base64 = null;
	private String img_id = null;
	private String img_result_html = null;
	  public String getImg_result_html() {
		return img_result_html;
	}
	public void setImg_result_html(String img_result_html) {
		this.img_result_html = img_result_html;
	}
	public String getImg_id() {
		return img_id;
	}
	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}
	public String getImg_base64() {
		return img_base64;
	}
	public void setImg_base64(String img_base64) {
		this.img_base64 = img_base64;
	}
	private String _id = null;
	  public JSonDocumentTemplateClass(String img_id,String img_base64) {
		  this.img_base64 = img_base64;
		  this._id = img_id;
	  }
}
