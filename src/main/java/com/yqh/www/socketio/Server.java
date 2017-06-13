package com.yqh.www.socketio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.yqh.www.entity.TimeDefined;
import com.yqh.www.redis.RedisKey;
import com.yqh.www.response.ResponseData;

/** 
 * Author:杨庆辉，尹欣天才!
 * Time: 2017年6月7日 上午9:47:41 
 * Desp: socket服务端
 */
public class Server {
	
	private static HashOperations<String,String,String> hashOps = null;
    private static Set<SocketIOClient> clients = new HashSet<SocketIOClient>();//用于保存所有客户端  
    
    private static Map<SocketIOClient, String> quotationMap = new HashMap<SocketIOClient, String>();
    private static Map<SocketIOClient, String> sharingchatMap = new HashMap<SocketIOClient, String>();
    private static Map<SocketIOClient, String> sharingchatAverageMap = new HashMap<SocketIOClient, String>();
    private static Map<SocketIOClient, String> klinedayMap = new HashMap<SocketIOClient, String>();
    private static Map<SocketIOClient, String> klineWeekMap = new HashMap<SocketIOClient, String>();
    private static Map<SocketIOClient, String> klinemonthMap = new HashMap<SocketIOClient, String>();
    private static Map<SocketIOClient, String> klinequarterMap = new HashMap<SocketIOClient, String>();
   	private static Map<SocketIOClient, String> avarageline5dayMap = new HashMap<SocketIOClient, String>();
   	private static Map<SocketIOClient, String> avarageline10dayMap = new HashMap<SocketIOClient, String>();
   	private static Map<SocketIOClient, String> avarageline20dayMap = new HashMap<SocketIOClient, String>();
   	private static Map<SocketIOClient, String> avarageline60dayMap = new HashMap<SocketIOClient, String>();
   	private static Map<SocketIOClient, String> avarageline5weekMap = new HashMap<SocketIOClient, String>();
   	private static Map<SocketIOClient, String> avarageline10weekMap = new HashMap<SocketIOClient, String>();
    
    
    @SuppressWarnings({ "unchecked", "resource" })
	public static void main(String[] args) throws Exception {
    	
    	ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
    	hashOps = context.getBean(RedisTemplate.class).opsForHash();
    	TimeDefined timeDefined = context.getBean(TimeDefined.class);
		SocketIOServer server = context.getBean(SocketIOServer.class);
		
        server.addConnectListener(new ConnectListener() {//添加客户端连接监听器  
            @Override
            public void onConnect(SocketIOClient client) {  
                System.out.println("链接客户端:SessionId=" + client.getSessionId());   
                clients.add(client);//保存客户端
            }  
        });  
        
        //行情订阅
        server.addEventListener("quotation_order", String.class, new DataListener<String>() {
        	@Override
        	public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
        		System.out.println("---->行情订阅代码>>>存放的key："+client.getSessionId().toString()+"；value："+code);
        		quotationMap.put(client,code);
        		client.sendEvent("quotation_order", 
        				getResponseDataJson("quotation_order", code, RedisKey.REALTIME_QUOTATION.val()));
        	}
        });
        
        //分时图订阅
        server.addEventListener("sharingchat_order", String.class, new DataListener<String>() {
        	@Override
        	public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
        		System.out.println("---->分时图订阅代码>>>存放的key："+client.getSessionId().toString()+"；value："+code);
        		sharingchatMap.put(client,code);
        		client.sendEvent("sharingchat_order", 
        				getResponseDataJson("sharingchat_order", code, RedisKey.REALTIME_SHARINGCHAT.val()));
        	}
        });
        
        //分时均线图订阅
        server.addEventListener("sharingchatavarage_order", String.class, new DataListener<String>() {
        	@Override
        	public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
        		System.out.println("---->分时均线图订阅代码>>>存放的key："+client.getSessionId().toString()+"；value："+code);
        		sharingchatAverageMap.put(client,code);
        		client.sendEvent("sharingchatavarage_order", 
        				getResponseDataJson("sharingchatavarage_order", code, RedisKey.REALTIME_SHARINGCHAT_AVARAGE.val()));
        	}
        });
        
        //K线图（天）订阅
        server.addEventListener("klineday_order", String.class, new DataListener<String>() {
			@Override
			public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
				System.out.println("---->K线图（天）订阅代码>>>存放的key："+client.getSessionId().toString()+"；value："+code);
				klinedayMap.put(client,code);
				client.sendEvent("klineday_order", 
						getResponseDataJson("klineweek_order", code, RedisKey.REALTIME_K_LINE_DAY.val()));
			}
		});
        
        //K线图（周）订阅
        server.addEventListener("klineweek_order", String.class, new DataListener<String>() {
        	@Override
        	public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
        		System.out.println("---->K线图（周）订阅代码>>>存放的key："+client.getSessionId().toString()+"；value："+code);
        		klineWeekMap.put(client,code);
        		client.sendEvent("klineweek_order", 
        				getResponseDataJson("klineweek_order", code, RedisKey.REALTIME_K_LINE_WEEK.val()));
        	}
        });
        
        // K线图（月）订阅
		server.addEventListener("klinemonth_order", String.class, new DataListener<String>() {
			@Override
			public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
				System.out.println("---->K线图（月）订阅代码>>>存放的key：" + client.getSessionId().toString() + "；value：" + code);
				klineWeekMap.put(client, code);
				client.sendEvent("klinemonth_order",
						getResponseDataJson("klinemonth_order", code, RedisKey.REALTIME_K_LINE_MONTH.val()));
			}
		});
        
    	//K线图（季）订阅
 		server.addEventListener("klinequarter_order", String.class, new DataListener<String>() {
 			@Override
 			public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
 				System.out.println("---->K线图（季）订阅代码>>>存放的key：" + client.getSessionId().toString() + "；value：" + code);
 				klinequarterMap.put(client, code);
 				client.sendEvent("klinequarter_order",
 						getResponseDataJson("klinequarter_order", code, RedisKey.REALTIME_K_LINE_QUARTER.val()));
 			}
 		});
        
 		// 均线图（5天）订阅
 		server.addEventListener("avarageline5day_order", String.class, new DataListener<String>() {
 			@Override
 			public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
 				System.out.println("---->均线图（5天）订阅代码>>>存放的key：" + client.getSessionId().toString() + "；value：" + code);
 				avarageline5dayMap.put(client, code);
 				client.sendEvent("avarageline5day_order",
 						getResponseDataJson("avarageline5day_order", code, RedisKey.REALTIME_AVARAGE_LINE_5_DAY.val()));
 			}
 		});
 		
 		// 均线图（10天）订阅
 		server.addEventListener("avarageline10day_order", String.class, new DataListener<String>() {
 			@Override
 			public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
 				System.out.println("---->均线图（10天）订阅代码>>>存放的key：" + client.getSessionId().toString() + "；value：" + code);
 				avarageline10dayMap.put(client, code);
 				client.sendEvent("avarageline10day_order",
 						getResponseDataJson("avarageline10day_order", code, RedisKey.REALTIME_AVARAGE_LINE_10_DAY.val()));
 			}
 		});
 		
 		// 均线图（20天）订阅
 		server.addEventListener("avarageline20day_order", String.class, new DataListener<String>() {
 			@Override
 			public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
 				System.out.println("---->均线图（20天）订阅代码>>>存放的key：" + client.getSessionId().toString() + "；value：" + code);
 				avarageline20dayMap.put(client, code);
 				client.sendEvent("avarageline10day_order",
 						getResponseDataJson("avarageline20day_order", code, RedisKey.REALTIME_AVARAGE_LINE_20_DAY.val()));
 			}
 		});
 		
 		// 均线图（60天）订阅
 		server.addEventListener("avarageline60day_order", String.class, new DataListener<String>() {
 			@Override
 			public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
 				System.out.println("---->均线图（60天）订阅代码>>>存放的key：" + client.getSessionId().toString() + "；value：" + code);
 				avarageline60dayMap.put(client, code);
 				client.sendEvent("avarageline60day_order",
 						getResponseDataJson("avarageline60day_order", code, RedisKey.REALTIME_AVARAGE_LINE_60_DAY.val()));
 			}
 		});
 		
 		//均线图（5周）订阅
 		server.addEventListener("avarageline5week_order", String.class, new DataListener<String>() {
 			@Override
 			public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
 				System.out.println("---->均线图（5周）订阅代码>>>存放的key：" + client.getSessionId().toString() + "；value：" + code);
 				avarageline5weekMap.put(client, code);
 				client.sendEvent("avarageline5week_order",
 						getResponseDataJson("avarageline5week_order", code, RedisKey.REALTIME_AVARAGE_LINE_5_WEEK.val()));
 			}
 		});
 		
 		//均线图（10周）订阅
 		server.addEventListener("avarageline10week_order", String.class, new DataListener<String>() {
 			@Override
 			public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
 				System.out.println("---->均线图（10周）订阅代码>>>存放的key：" + client.getSessionId().toString() + "；value：" + code);
 				avarageline10weekMap.put(client, code);
 				client.sendEvent("avarageline10week_order",
 						getResponseDataJson("avarageline10week_order", code, RedisKey.REALTIME_AVARAGE_LINE_10_WEEK.val()));
 			}
 		});
        
/*********************************************************************************/
        


/*********************************************************************************/
        
        //启动socketio
        server.start();  
        System.out.println("-------------->>启动socketIo 服务器<<---------------");  
        
        Timer timer = new Timer();
        
        //行情推送
		timer.schedule(new TimerTask() {  
			@Override  
			public void run() {  
				for(SocketIOClient client : quotationMap.keySet()) {
					String code = quotationMap.get(client);
					System.out.println("~~~服务器的推送{ 行情  }数据的code："+code);
		    		client.sendEvent("quotation_prop", 
		    				getResponseDataJson("quotation_prop", code, RedisKey.REALTIME_QUOTATION.val()));
				}  
			}  
		}, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
        
		//分时图推送
		timer.schedule(new TimerTask() {  
			@Override  
			public void run() {  
				for(SocketIOClient client : sharingchatMap.keySet()) {
					String code = sharingchatMap.get(client);
					System.out.println("~~~服务器的推送{ 分时图  }数据的code："+code);
					client.sendEvent("sharingchat_prop", 
							getResponseDataJson("sharingchat_prop", code, RedisKey.REALTIME_SHARINGCHAT.val()));
				}
			}  
		}, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
		
		//分时均线图推送
		timer.schedule(new TimerTask() {  
			@Override  
			public void run() {  
				for(SocketIOClient client : sharingchatAverageMap.keySet()) {
					String code = sharingchatAverageMap.get(client);
					System.out.println("~~~服务器的推送{ 分时均线图  }数据的code："+code);
					client.sendEvent("sharingchatavarage_prop", 
							getResponseDataJson("sharingchatavarage_prop", code, RedisKey.REALTIME_SHARINGCHAT_AVARAGE.val()));
				}
			}  
		}, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
		
        //K线图（天）推送
        timer.schedule(new TimerTask() {  
        	@Override  
        	public void run() {  
        		for(SocketIOClient client : klinedayMap.keySet()) {
        			String code = klinedayMap.get(client);
    				System.out.println("~~~服务器的推送{ K线图（天） }数据的code："+code);
    				client.sendEvent("klineday_prop", 
    						getResponseDataJson("klineday_prop", code, RedisKey.REALTIME_K_LINE_DAY.val()));
        		}
        	}  
        }, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
        
        //K线图（周）推送
        timer.schedule(new TimerTask() {
        	@Override  
        	public void run() {
        		for(SocketIOClient client : klineWeekMap.keySet()) {
    				String code = klineWeekMap.get(client);
					System.out.println("~~~服务器的推送{ K线图（周） }数据的code："+code);
					client.sendEvent("klineweek_prop", 
							getResponseDataJson("klineweek_prop", code, RedisKey.REALTIME_K_LINE_WEEK.val()));
        		}  
        	}  
        }, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
        
        // K线图（月）推送
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (SocketIOClient client : klinemonthMap.keySet()) {
					String code = klinemonthMap.get(client.getSessionId().toString());
					System.out.println("~~~服务器的推送{ K线图（月） }数据的code：" + code);
					client.sendEvent("klinemonth_prop",
							getResponseDataJson("klinemonth_prop", code, RedisKey.REALTIME_K_LINE_MONTH.val()));
				}
			}
		}, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
		
		// K线图（季） 推送
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (SocketIOClient client : klinequarterMap.keySet()) {
					String code = klinequarterMap.get(client.getSessionId().toString());
					System.out.println("~~~服务器的推送{ K线图（季） }数据的code：" + code);
					client.sendEvent("klinequarter_prop",
							getResponseDataJson("klinequarter_prop", code, RedisKey.REALTIME_K_LINE_QUARTER.val()));
				}
			}
		}, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
		
		// 均线图（5天）推送
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (SocketIOClient client : avarageline5dayMap.keySet()) {
					String code = avarageline5dayMap.get(client.getSessionId().toString());
					System.out.println("~~~服务器的推送{ 均线图（5天） }数据的code：" + code);
					client.sendEvent("avarageline5day_prop",
							getResponseDataJson("avarageline5day_prop", code, RedisKey.REALTIME_AVARAGE_LINE_5_DAY.val()));
				}
			}
		}, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
		
		// 均线图（10天）推送
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (SocketIOClient client : avarageline10dayMap.keySet()) {
					String code = avarageline10dayMap.get(client.getSessionId().toString());
					System.out.println("~~~服务器的推送{ 均线图（10天） }数据的code：" + code);
					client.sendEvent("avarageline10day_prop",
							getResponseDataJson("avarageline10day_prop", code, RedisKey.REALTIME_AVARAGE_LINE_10_DAY.val()));
				}
			}
		}, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
		
		// 均线图（20天）推送
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (SocketIOClient client : avarageline20dayMap.keySet()) {
					String code = avarageline20dayMap.get(client.getSessionId().toString());
					System.out.println("~~~服务器的推送{ 均线图（20天） }数据的code：" + code);
					client.sendEvent("avarageline20day_prop",
							getResponseDataJson("avarageline20day_prop", code, RedisKey.REALTIME_AVARAGE_LINE_20_DAY.val()));
				}
			}
		}, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
		
		// 均线图（60天）推送
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (SocketIOClient client : avarageline60dayMap.keySet()) {
					String code = avarageline60dayMap.get(client.getSessionId().toString());
					System.out.println("~~~服务器的推送{ 均线图（60天） }数据的code：" + code);
					client.sendEvent("avarageline60day_prop",
							getResponseDataJson("avarageline60day_prop", code, RedisKey.REALTIME_AVARAGE_LINE_60_DAY.val()));
				}
			}
		}, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
		
		// 均线图（5周）推送
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (SocketIOClient client : avarageline5weekMap.keySet()) {
					String code = avarageline5weekMap.get(client.getSessionId().toString());
					System.out.println("~~~服务器的推送{ 均线图（5周） }数据的code：" + code);
					client.sendEvent("avarageline5week_prop",
							getResponseDataJson("avarageline5week_prop", code, RedisKey.REALTIME_AVARAGE_LINE_5_WEEK.val()));
				}
			}
		}, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
		
		// 均线图（10周）推送
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (SocketIOClient client : avarageline10weekMap.keySet()) {
					String code = avarageline10weekMap.get(client.getSessionId().toString());
						System.out.println("~~~服务器的推送{ 均线图（10周） }数据的code：" + code);
						client.sendEvent("avarageline10week_prop",
								getResponseDataJson("avarageline10week_prop", code, RedisKey.REALTIME_AVARAGE_LINE_10_WEEK.val()));
				}
			}
		}, timeDefined.getDelayTime(), timeDefined.getSpaceTime());
        
/*********************************************************************************/
       
        
        
/*********************************************************************************/
          

    }
    
    /**
     * 从redis中获取数据
     * @param eventName
     * @param redisKey
     * @param code
     * @return
     */
    private static String getResponseDataJson(String eventName,String code,String redisKey) {
    	String json = null;
		try {
			System.out.println("~~~服务器的推送数据的code："+code);
			String result = hashOps.get(redisKey, code);
			ResponseData responseData = new ResponseData(eventName, code, result);
			json = new ObjectMapper().writeValueAsString(responseData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
    }
  
}  
