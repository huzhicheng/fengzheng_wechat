开发工具为IntelliJ IDEA 14,部署时请将web.xml中
```xml
<servlet>
        <servlet-name>initAccessTokenServlet</servlet-name>
        <servlet-class>
            org.fengzheng.wechat.accesstoken.AccessTokenServlet
        </servlet-class>
        <init-param>
            <param-name>appid</param-name>
            <param-value>your appid</param-value>
        </init-param>
        <init-param>
            <param-name>appsecret</param-name>
            <param-value>your appsecret</param-value>
        </init-param>
        <load-on-startup>0</load-on-startup>
  </servlet>
  
  中的“your appid”替换为自己的微信公众号的appid,将“your appsecret”替换为自己的微信公众号的appsecret
