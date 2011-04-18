package eriji.com.SKD.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.log4j.Logger;

import com.ch_linghu.fanfoudroid.http.HttpException;
import com.ch_linghu.fanfoudroid.http.Response;

import eriji.com.OAuth.OAuthClient;
import eriji.com.OAuth.OAuthClientException;
import eriji.com.OAuth.OAuthStoreException;
import eriji.com.SDK.API;

public class ClientExample {
    private static Logger logger = Logger.getLogger(ClientExample.class);

    public static void main(String[] args) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException, OAuthStoreException {

        API api = new API();
        
        OAuthClient oauth = api.getHttpClient().getOAuthClient();

        try {
            
            /****************** 只运行一遍 *****************************
            
            // 获取request token, 并储存起来
            oauth.retrieveRequestToken();
            
            // 询问用户pinCode
            Scanner in = new Scanner(System.in);
            
            // 利用储存起来的request token和PinCode去与服务器交换access Token, 并储存起来
            oauth.retrieveAccessToken(in.nextLine());
            
            /****************** 只运行一遍 *******************************/
        
            // 测试API服务器是否工作正常
            Response res = api.test();
            String body = res.asString();
            logger.info(body);
            
            // 创建一篇日记
            Response res2 = api.updateDiary("title-api", "content-api");
            logger.debug( res2.asJSONObject() );
            
            // 获得一篇日记
            Response res3 = api.getDiary("10000086");
            logger.debug( res3.asString() );
        
            logger.info("exit");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
