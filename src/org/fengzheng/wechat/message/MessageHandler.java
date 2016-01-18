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
            System.out.print(e.getName() + "|" + e.getText());
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
        System.out.println("MsgType:" + msgType);
        MessageType messageEnumType = MessageType.valueOf(MessageType.class,
                msgType.toUpperCase());
        switch (messageEnumType) {
            case TEXT:
                result = handleTextMessage(map);
                break;
            case IMAGE:
                result = handleImageMessage(map);
                break;
            case VOICE:
                result = handleVoiceMessage(map);
                break;
            case VIDEO:
                result = handleVideoMessage(map);
                break;
            case SHORTVIDEO:
                result = handleSmallVideoMessage(map);
                break;
            case LOCATION:
                result = handleLocationMessage(map);
                break;
            case LINK:
                result = handleLinkMessage(map);
                break;
            default:
                break;
        }
        return result;
    }

    private static String handleTextMessage(Map map) {
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
        switch (content) {
            case "文本":
                xml = buildTextMessage(map, "这是一条文本消息");
                break;
            case "图片":
                xml = buildPicture(map);
                break;
            case "音乐":
                xml = buildMusic(map);
                break;
            case "图文":
                xml = buildNewsMessage(map);
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

    /**
     * 构造文本消息
     *
     * @param map
     * @param content
     * @return
     */
    private static String buildTextMessage(Map map, String content) {
        String fromUserName = map.get("FromUserName").toString();
        // 开发者微信号
        String toUserName = map.get("ToUserName").toString();

        return String.format("<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[%s]]></Content></xml>",
                fromUserName, toUserName, getUtcTime(), content);
    }

    /**
     * 构造图片消息
     *
     * @param map
     * @return
     */
    private static String buildPicture(Map map) {
        PictureMessage msg = new PictureMessage();
        String fromUserName = map.get("FromUserName").toString();
        // 公众号
        String toUserName = map.get("ToUserName").toString();
        String tempMediaId = "58fFYMRjJv0V94qZJ1EeGSKRQB4CbboVBC7FAILlxxZ2AzMnVVUeiOOenxgkzv9g";
        return String.format("<xml>\n" +
                "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                "<CreateTime>%s</CreateTime>\n" +
                "<MsgType><![CDATA[image]]></MsgType>\n" +
                "<Image>\n" +
                "<MediaId><![CDATA[%s]]></MediaId>\n" +
                "</Image>\n" +
                "</xml>", fromUserName, toUserName, getUtcTime(), tempMediaId);
    }


    /**
     * 构造音乐消息
     *
     * @param map
     * @return
     */
    private static String buildMusic(Map map) {
        MusicMessage msg = new MusicMessage();
        String fromUserName = map.get("FromUserName").toString();
        // 开发者微信号
        String toUserName = map.get("ToUserName").toString();


        msg.Sender = toUserName;
        msg.Receiver = fromUserName;
        msg.CreateTime = getUtcTime();
        msg.Title = "蓝莲花";
        msg.Description = "蓝莲花-许巍";
        msg.MusicURL = "http://7te94m.com1.z0.glb.clouddn.com/lanlianhua.mp3";
        msg.HQMusicUrl = "http://7te94m.com1.z0.glb.clouddn.com/lanlianhua.mp3";
        msg.ThumbMediaId = "kpNqTAk7tZ6NHhlHlEBhgUhfxjPBADbK79EfwF1RlOKAFcuKYC0eenD-ja-nTHg9";

        return String
                .format("<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[music]]></MsgType><Music><Title><![CDATA[%s]]></Title><Description><![CDATA[%s]]></Description><MusicUrl><![CDATA[%s]]></MusicUrl><HQMusicUrl><![CDATA[%s]]></HQMusicUrl><ThumbMediaId><![CDATA[%s]]></ThumbMediaId></Music></xml>",
                        msg.Receiver, msg.Sender, getUtcTime(), msg.Title,
                        msg.Description, msg.MusicURL, msg.HQMusicUrl,
                        msg.ThumbMediaId);
    }


    /**
     * 构造图文消息
     *
     * @param map
     * @return
     */
    private static String buildNewsMessage(Map map) {
        String fromUserName = map.get("FromUserName").toString();
        // 开发者微信号
        String toUserName = map.get("ToUserName").toString();

        NewsItem item = new NewsItem();
        item.Title = "iOS 9.1现2大故障 iPhone基本功能遭殃";
        item.Description = "凤凰科技讯 11月9日消息，据中关村在线报道,iOS 9.1 是个颇为稳定的版本，至少初初推出时的印象是这样，现在推出接近两星期，一些颇为严重的问题开始浮现。其中两大问题，正在困扰为数不少的用户。";
        item.PicUrl = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";
        item.Url = "http://tech.ifeng.com/a/20151109/41503466_0.shtml";
        String itemContent1 = buildSingleItem(item);

        NewsItem item2 = new NewsItem();
        item2.Title = "刚毕业就月薪过万的程序员 创业大街上的得意与迷茫";
        item2.Description = "DoNews11月9日消息（编辑 陈艳曲）第一次见到刚毕业的程序员小李时，正逢他在创业大街上谋到了一份工作，五位数的薪资让他更加坚信，毕业前在技术学校报一个ios开发培训班是一个多么正确的决定";
        item2.PicUrl = "http://photocdn.sohu.com/20151108/mp40396008_1446997429715_4_th_fv23.jpeg";
        item2.Url = "http://tech.hexun.com/2015-11-09/180434346.html";
        String itemContent2 = buildSingleItem(item2);


        String content = String.format("<xml>\n" +
                "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                "<CreateTime>%s</CreateTime>\n" +
                "<MsgType><![CDATA[news]]></MsgType>\n" +
                "<ArticleCount>%s</ArticleCount>\n" +
                "<Articles>\n" + "%s" +
                "</Articles>\n" +
                "</xml> ", fromUserName, toUserName, getUtcTime(), 2, itemContent1 + itemContent2);
        return content;

    }

    /**
     * 生成图文消息的一条记录
     *
     * @param item
     * @return
     */
    private static String buildSingleItem(NewsItem item) {
        String itemContent = String.format("<item>\n" +
                "<Title><![CDATA[%s]]></Title> \n" +
                "<Description><![CDATA[%s]]></Description>\n" +
                "<PicUrl><![CDATA[%s]]></PicUrl>\n" +
                "<Url><![CDATA[%s]]></Url>\n" +
                "</item>", item.Title, item.Description, item.PicUrl, item.Url);
        return itemContent;
    }

    /**
     * 接收到图片消息后处理
     *
     * @param map
     * @return
     */
    private static String handleImageMessage(Map map) {
        String picUrl = map.get("PicUrl").toString();
        String mediaId = map.get("MediaId").toString();
        System.out.print("picUrl:"+picUrl);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的图片，图片Url为：%s\n图片素材Id为：%s",picUrl,mediaId);
        return buildTextMessage(map,result);
    }

    private static String handleVoiceMessage(Map map){
        String format = map.get("Format").toString();
        String mediaId = map.get("MediaId").toString();
        System.out.print("format:"+format);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的语音，语音格式为：%s\n语音素材Id为：%s",format,mediaId);
        return buildTextMessage(map,result);
    }
    private static String handleVideoMessage(Map map){
        String thumbMediaId = map.get("ThumbMediaId").toString();
        String mediaId = map.get("MediaId").toString();
        System.out.print("thumbMediaId:"+thumbMediaId);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的视频，视频中的素材ID为：%s\n视频Id为：%s",thumbMediaId,mediaId);
        return buildTextMessage(map,result);
    }
    private static String handleSmallVideoMessage(Map map){
        String thumbMediaId = map.get("ThumbMediaId").toString();
        String mediaId = map.get("MediaId").toString();
        System.out.print("thumbMediaId:"+thumbMediaId);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的小视频，小视频中素材ID为：%s,\n小视频Id为：%s",thumbMediaId,mediaId);
        return buildTextMessage(map,result);
    }
    private static String handleLocationMessage(Map map){
        String latitude = map.get("Location_X").toString();  //纬度
        String longitude = map.get("Location_Y").toString();  //经度
        String label = map.get("Label").toString();  //地理位置精度
        String result = String.format("纬度：%s\n经度：%s\n地理位置：%s",latitude,longitude,label);
        return buildTextMessage(map,result);
    }
    private static String handleLinkMessage(Map map){
        String title = map.get("Title").toString();
        String description = map.get("Description").toString();
        String url = map.get("Url").toString();
        String result = String.format("已收到您发来的链接，链接标题为：%s,\n描述为：%s\n,链接地址为：%s",title,description,url);
        return buildTextMessage(map,result);
    }

}
