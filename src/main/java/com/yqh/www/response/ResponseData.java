package com.yqh.www.response; 
/** 
 * Author:杨庆辉
 * Time:2017年6月7日 上午11:38:50 
 * Desp:
 */
public class ResponseData {

	private String event;
	private String code;
	private String result;
	
	public ResponseData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ResponseData(String event, String code, String result) {
		super();
		this.event = event;
		this.code = code;
		this.result = result;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
