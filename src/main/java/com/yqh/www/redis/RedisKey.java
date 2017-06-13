package com.yqh.www.redis;

public enum  RedisKey {
 
	//实时行情
	REALTIME_QUOTATION ("neeq:realtime:quotation"),	
	
	//分时图    分时均线图
	REALTIME_SHARINGCHAT ("neeq:realtime:sharingchat"),
	REALTIME_SHARINGCHAT_AVARAGE ("neeq:realtime:sharingchat_avarage"),
	
	//K线图
	REALTIME_K_LINE_DAY ("neeq:realtime:k_line_day"),
	REALTIME_K_LINE_WEEK ("neeq:realtime:k_line_week"),	
	REALTIME_K_LINE_MONTH ("neeq:realtime:k_line_month"),	  
	REALTIME_K_LINE_QUARTER ("neeq:realtime:k_line_quarter"),
	
	//k线均线图	
	REALTIME_AVARAGE_LINE_5_DAY ("neeq:realtime:avarage_line_5_day"),
	REALTIME_AVARAGE_LINE_10_DAY ("neeq:realtime:avarage_line_10_day"),
	REALTIME_AVARAGE_LINE_20_DAY ("neeq:realtime:avarage_line_20_day"),
	REALTIME_AVARAGE_LINE_60_DAY ("neeq:realtime:avarage_line_60_day"),
	REALTIME_AVARAGE_LINE_5_WEEK ("neeq:realtime:avarage_line_5_week"),
	REALTIME_AVARAGE_LINE_10_WEEK ("neeq:realtime:avarage_line_10_week"),
	REALTIME_AVARAGE_LINE_20_WEEK ("neeq:realtime:avarage_line_20_week"),
	REALTIME_AVARAGE_LINE_60_WEEK ("neeq:realtime:avarage_line_60_week"),
	REALTIME_AVARAGE_LINE_5_MONTH ("neeq:realtime:avarage_line_5_month"),
	REALTIME_AVARAGE_LINE_10_MONTH ("neeq:realtime:avarage_line_10_month"),
	REALTIME_AVARAGE_LINE_20_MONTH ("neeq:realtime:avarage_line_20_month"),
	REALTIME_AVARAGE_LINE_60_MONTH ("neeq:realtime:avarage_line_60_month"),
	REALTIME_AVARAGE_LINE_5_QUARTER ("neeq:realtime:avarage_line_5_quarter"),
	REALTIME_AVARAGE_LINE_10_QUARTER ("neeq:realtime:avarage_line_10_quarter"),
	REALTIME_AVARAGE_LINE_20_QUARTER ("neeq:realtime:avarage_line_20_quarter"),
	REALTIME_AVARAGE_LINE_60_QUARTER ("neeq:realtime:avarage_line_60_quarter"),
	
	//协议申报信息
	REALTIME_AGREE_TRANS_REAL ("neeq:realtime:agree_trans_real"),
	
	//协议申报统计
	REALTIME_AGREE_TRANS_STAT ("neeq:realtime:agree_trans_stat"),
	
	//实时行情总览
	REALTIME_QUOTATION_TOTALSUMER ("stats:listcompany:realtime:quotation:stat"),
	
	//实时行情按价格排序及实时行情按成交金额排序
	REALTIME_QUOTATION_RANK ("stats:listcompany:realtime:quotation:rank"),
	
	//实时行情异动
	REALTIME_QUOTATION_ABNORMAL ("stats:listcompany:realtime:quotation:abnormal");
	
	
	 // 定义私有变量
    private String key ;
	
	 // 构造函数，枚举类型只能为私有
    private RedisKey( String _key) {
        this.key = _key;
    }
	
    public String val() {
        return String.valueOf (this.key);
    }
	
	
	
}

