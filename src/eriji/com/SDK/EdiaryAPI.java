package eriji.com.SDK;

import java.util.Scanner;

import org.apache.log4j.Logger;

import eriji.com.OAuth.OAuthClient;
import eriji.com.OAuth.OAuthFileStore;

public class EdiaryAPI extends API {
    private Logger log = Logger.getLogger(EdiaryAPI.class);

    public EdiaryAPI() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public EdiaryAPI setup() {
        OAuthClient oauth = getHttpClient().getOAuthClient();
        oauth.setStore(new OAuthFileStore(System.getProperty("user.dir") + "/data/"));

        try {
            // 只运行一次
            if (! oauth.hasAccessToken()) {
                log.warn("Only run once");
                // 获取request token, 并储存起来
                String authUrl = oauth.retrieveRequestToken();
                // 询问用户pinCode
                System.out.println("尚未授权本程序访问权, 请前去服务器授权, 并记下PinCode : " + authUrl);
                System.out.print("请输入PinCode:");
                Scanner in = new Scanner(System.in);
                // 利用储存起来的request token和PinCode去与服务器交换access Token, 并储存起来
                oauth.retrieveAccessToken(in.nextLine());
            }
        
            // 测试API服务器是否工作正常
            //Response res = api.test();
            //String body = res.asString();
            //logger.info(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return this;
    }


}
