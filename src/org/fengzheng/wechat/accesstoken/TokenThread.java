package org.fengzheng.wechat.accesstoken;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.fengzheng.wechat.common.NetWorkHelper;

/**
 * Created by huzhicheng on 2015/11/5.
 */
public class TokenThread implements Runnable {
    public static String appId = "";

    public static String appSecret= "";

    //设置为静态
    public static AccessToken accessToken = null;

    public void run(){
        while (true){
            try{
                accessToken = this.getAccessToken();
                if(null!=accessToken){
                    System.out.println(accessToken.getAccessToken());
                    Thread.sleep(7000 * 1000); //获取到access_token 休眠7000秒
                    //Thread.sleep(30*1000);//测试时可以设置短一点时间 方便看效果

                }else{
                    Thread.sleep(1000*3); //获取的access_token为空 休眠3秒
                }
            }catch(Exception e){
                System.out.println("发生异常："+e.getMessage());
                e.printStackTrace();
                try{
                    Thread.sleep(1000*10); //发生异常休眠1秒
                }catch (Exception e1){

                }
            }
        }
    }


    /**
     * 获取access_token
     * @return
     */
    private AccessToken getAccessToken(){
        NetWorkHelper netHelper = new NetWorkHelper();
        String Url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",this.appId,this.appSecret);
        String result = netHelper.getHttpsResponse(Url,"");
        System.out.println(result);
        //response.getWriter().println(result);
        JSONObject json = JSON.parseObject(result);
        AccessToken token = new AccessToken();
        token.setAccessToken(json.getString("access_token"));
        token.setExpiresin(json.getInteger("expires_in"));
        return token;
    }
}
