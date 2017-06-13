package com.yqh.www.socketio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.yqh.www.redis.RedisKey;
import com.yqh.www.response.Point;
import com.yqh.www.response.ResponseData;

/** 
 * Author:杨庆辉
 * Time:2017年6月7日 上午9:47:41 
 * Desp: 配置未分离
 */
public class Server_old {
	
//	private static final String HOST_NAME = "101.201.116.200";
	private static final String HOST_NAME = "localhost";
	private static final Integer SERVER_PORT = 9999;
	private static final Integer DELAY_TIME = 5000;//毫秒
	private static final Integer SPACE_TIME = 5000;//毫秒
	
	private static ObjectMapper mapper = new ObjectMapper(); 
	private static HashOperations<String,String,String> hashOps = null;
    private static List<SocketIOClient> clients = new ArrayList<SocketIOClient>();//用于保存所有客户端  
    
    private static Map<String,String> quotationMap = new HashMap<String,String>();
    private static Map<SocketIOClient,String> klinedayMap = new HashMap<SocketIOClient,String>();
    private static Map<SocketIOClient,String> klineWeekMap = new HashMap<SocketIOClient,String>();
    
    public static void main(String[] args) throws Exception {
    	
    	ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
    	
    	hashOps = context.getBean(RedisTemplate.class).opsForHash();
    	
//        Configuration configuration = new Configuration();  
//        configuration.setHostname(HOST_NAME);//设置主机名  
//        configuration.setPort(SERVER_PORT);//设置监听的端口号  
//        SocketIOServer server = new SocketIOServer(configuration);//根据配置创建服务器对象  
  
		SocketIOServer server = context.getBean(SocketIOServer.class);
        server.addConnectListener(new ConnectListener() {//添加客户端连接监听器  
            @Override
            public void onConnect(SocketIOClient client) {  
                System.out.println("connected:SessionId=" + client.getSessionId());   
                clients.add(client);//保存客户端
            }  
        });  
        
        server.addEventListener("test", String.class, new DataListener<String>() {
			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("接收到客户端的信息为：" + data);
				// 向客户端发送消息
				
				System.out.println("----------test：准备返回参数");
				// 第一个参数必须与eventName一致，第二个参数data必须与eventClass一致
				client.sendEvent("test", "this is test's response......");
			}
		});
        
        //行情订阅
        server.addEventListener("quotation_order", String.class, new DataListener<String>() {
        	@Override
        	public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
        		System.out.println("---->行情订阅代码>>>存放的key："+client.getSessionId().toString()+"；value："+code);
        		quotationMap.put(client.getSessionId().toString(),code);
        		client.sendEvent("quotation_order", getResponseDataJson("quotation_order", code, RedisKey.REALTIME_QUOTATION.val()));
        	}
        });
        
        //K线图（天）订阅
        server.addEventListener("klineday_order", String.class, new DataListener<String>() {
			@Override
			public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
				System.out.println("---->K线图（天）订阅代码>>>存放的key："+client.getSessionId().toString()+"；value："+code);
				klinedayMap.put(client,code);
				client.sendEvent("klineday_order", getResponseDataJson("klineweek_order", code, RedisKey.REALTIME_K_LINE_DAY.val()));
			}
		});
        
        //K线图（周）订阅
        server.addEventListener("klineweek_order", String.class, new DataListener<String>() {
        	@Override
        	public void onData(SocketIOClient client, String code, AckRequest ackSender) throws Exception {
        		System.out.println("---->K线图（周）订阅代码>>>存放的key："+client.getSessionId().toString()+"；value："+code);
        		klineWeekMap.put(client,code);
        		client.sendEvent("klineweek_order", getResponseDataJson("klineweek_order", code, RedisKey.REALTIME_K_LINE_WEEK.val()));
        	}
        });
        
/*********************************************************************************/









/*********************************************************************************/
        
        //启动socketio
        server.start();  
        System.out.println("-------------->>启动socketIo 服务器<<---------------");  
        
        Timer timer = new Timer();
        
        timer.schedule(new TimerTask() {  
            @Override  
            public void run() {  
                Random random = new Random();  
                for(SocketIOClient client : clients) {
                    client.sendEvent("pushpoint", new Point(random.nextInt(100), random.nextInt(100)));//每隔一秒推送一次  
                }  
            }  
        }, DELAY_TIME, SPACE_TIME);
        
        //行情推送
        timer.schedule(new TimerTask() {  
        	@Override  
        	public void run() {  
        		for(SocketIOClient client : clients) {
        			String code = quotationMap.get(client.getSessionId().toString());
        			if(StringUtils.isNotBlank(code)){
	        			System.out.println("~~~服务器的推送{ 行情  }数据的code："+code);
	            		client.sendEvent("quotation_prop", getResponseDataJson("quotation_prop", code, RedisKey.REALTIME_QUOTATION.val()));
        			}
        		}  
        	}  
        }, DELAY_TIME, SPACE_TIME);
        //K线图（天）推送
        timer.schedule(new TimerTask() {  
        	@Override  
        	public void run() {  
        		for(SocketIOClient client : klinedayMap.keySet()) {
        			String code = klinedayMap.get(client);
    				System.out.println("~~~服务器的推送{ K线图（天） }数据的code："+code);
    				client.sendEvent("klineday_prop", getResponseDataJson("klineday_prop", code, RedisKey.REALTIME_K_LINE_DAY.val()));
        		}
        	}  
        }, DELAY_TIME, SPACE_TIME);
        
        //K线图（周）推送
        timer.schedule(new TimerTask() {  
        	@Override  
        	public void run() {
        		for(SocketIOClient client : klineWeekMap.keySet()) {
    				String code = klineWeekMap.get(client);
					System.out.println("~~~服务器的推送{ K线图（周） }数据的code："+code);
					client.sendEvent("klineweek_prop", getResponseDataJson("klineweek_prop", code, RedisKey.REALTIME_K_LINE_WEEK.val()));
        		}  
        	}  
        }, DELAY_TIME, SPACE_TIME);
        
/*********************************************************************************/
        
        
        
        
        
        
        
        
        
/*********************************************************************************/
          
//        Object object = new Object();  
//        synchronized (object) {  
//            object.wait();  
//        }  
    }
    
    /**
     * 从redis中获取数据
     * @param eventName
     * @param redisKey
     * @param code
     * @return
     */
    private static String getResponseDataJson(String eventName,String code,String redisKey){
    	String json = null;
    	ResponseData responseData;
		try {
			System.out.println("~~~服务器的推送数据的code："+code);
			String result = hashOps.get(redisKey, code);
			responseData = new ResponseData(eventName, code, result);
			json = mapper.writeValueAsString(responseData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
    }
  
}  
