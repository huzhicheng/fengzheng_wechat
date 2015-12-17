package org.fengzheng.wechat.message;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huzhicheng on 2015/12/17.
 */
public class MessageHandler {
    public static Map parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map map = new HashMap();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        System.out.println("获取输入流");
        /*
         * 读取request的body内容 此方法会导致流读取问题 Premature end of file. Nested exception:
		 * Premature end of file String requestBody =
		 * inputStream2String(inputStream); System.out.println(requestBody);
		 */
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            System.out.print(e.getName()+ "|"+ e.getText());
            map.put(e.getName(), e.getText());
        }

        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }

    private static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    // 根据消息类型 构造返回消息
    public static String buildXml(Map map, HttpServletResponse response) {
        String result = "";
        String msgType = map.get("MsgType").toString();
        System.out.println("MsgType:"+msgType);
        MessageType messageEnumType = MessageType.valueOf(MessageType.class,
                msgType.toUpperCase());
        switch (messageEnumType) {
            case TEXT:
                result = handleTextMessage(map);
                break;
        }
        return result;
    }

    private static String handleTextMessage(Map map){
        String xml = "";
        String fromUserName = map.get("FromUserName").toString();
        // 开发者微信号
        String toUserName = map.get("ToUserName").toString();
        // 消息内容
        String content = map.get("Content").toString();
        GeneralMessage msg = new GeneralMessage();
        msg.Sender = toUserName;
        msg.Receiver = fromUserName;
        msg.CreateTime = getUtcTime();
        switch (content){
            case "文本":
                xml = String
                        .format("<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[%s]]></Content></xml>",
                                msg.Receiver, msg.Sender, msg.CreateTime,
                                "这是一条文本消息");
                break;
            default:
                xml = String
                        .format("<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[%s]]></Content></xml>",
                                msg.Receiver, msg.Sender, msg.CreateTime,
                                "请回复如下关键词：\n文本\n图片\n语音\n视频\n音乐\n图文");
                break;
        }
        return xml;
    }

    private static String getUtcTime() {
        Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");// 设置显示格式
        String nowTime = "";
        nowTime = df.format(dt);
        long dd = (long) 0;
        try {
            dd = df.parse(nowTime).getTime();
        } catch (Exception e) {

        }
        return String.valueOf(dd);
    }
}
