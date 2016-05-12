package com.iaskdata.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 * @author ruijie jiangzhx@gmail.com
 * @date 2015年3月3日
 */
public class LogUtil {

	private static final Logger rawdataLogger = LoggerFactory.getLogger("rawdata");
	
	private static final Logger segmentsLogger = LoggerFactory.getLogger("segments");
	
	private static final Logger clickLogger = LoggerFactory.getLogger("click");
	
	private static final Logger track2Logger = LoggerFactory.getLogger("track2");
	
	private static final Logger rawdataTrashLogger = LoggerFactory.getLogger("rawdataTrash");
	
	private static final Logger segmentsTrashLogger = LoggerFactory.getLogger("segmentsTrash");
	
	private static final Logger h5rawdataLogger = LoggerFactory.getLogger("h5rawdata");
	
	private static final Logger h5segmentsLogger = LoggerFactory.getLogger("h5segments");
	
	private static final Logger marketLogger = LoggerFactory.getLogger("market");
	
	private static final Logger pkginfoLogger = LoggerFactory.getLogger("pkginfo");
	
	public static final void rawdata(String value) {
		rawdataLogger.info(value);
	}
	
	public static final void segments(String value) {
		segmentsLogger.info(value);
	}
	
	public static final void click(String value) {
		clickLogger.info(value);
	}
	
	public static final void track2(String value) {
		track2Logger.info(value);
	}
	
	public static final void rawdataTrash(String value) {
		rawdataTrashLogger.info(value);
	}
	
	public static final void segmentsTrash(String value) {
		segmentsTrashLogger.info(value);
	}
	
	public static final void h5rawdata(String value) {
		h5rawdataLogger.info(value);
	}
	
	public static final void h5segments(String value) {
		h5segmentsLogger.info(value);
	}
	
	public static final void market(String value) {
	    marketLogger.info(value);
    }
	
	public static final void pkginfo(String value) {
	    pkginfoLogger.info(value);
    }
}
