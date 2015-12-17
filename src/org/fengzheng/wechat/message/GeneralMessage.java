package org.fengzheng.wechat.message;

public class GeneralMessage {
	
	//消息发送者  若是公众好接收消息 则为具体的关注者  若是公众号发送消息则为公共号自身
	public String Sender;
	
	//消息接收者
	public String Receiver;
	
	//消息生成时间
	public String CreateTime;
	
	//消息类型
	public MessageType MsgType;
	
	
}
