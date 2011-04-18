package eriji.com.SDK;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ch_linghu.fanfoudroid.http.HttpClient;
import com.ch_linghu.fanfoudroid.http.HttpException;
import com.ch_linghu.fanfoudroid.http.Response;

import eriji.com.OAuth.OAuthClient;


public class API extends APISupport {
    private static final String TAG = "Eriji_API";
    private static Logger logger = Logger.getLogger(API.class);

    public static final String CONSUMER_KEY = "3c2d81228736786e5e846fa51900067404daaaa25";
    public static final String CONSUMER_SECRET = "f6996b1591ef009dcea225629a77abf4";
    
    private String baseURL = "http://yiriji.com/api/";
    private String OAuthUrl = "http://yiriji.com/oauth/index/";
    
    public API() {
        super(); // In case that the user is not logged in
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    
    public API(String userId, String password) {
        super(userId, password);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public API(String userId, String password, String baseURL) {
        this(userId, password);
        this.baseURL = baseURL;
    }
    
    public OAuthClient getOAuthClient() {
        return http.getOAuthClient();
    }
    
    /**
     * 设置HttpClient的Auth，为请求做准备
     * @param username
     * @param password
     */
    public void setCredentials(String username, String password) {
        http.setCredentials(username, password);
    }
    
    /**
     * 仅判断是否为空
     * @param username
     * @param password
     * @return
     */
    public static boolean isValidCredentials(String username, String password) {
        return (!username.isEmpty() && !password.isEmpty());
    }
    
    /**
     * 在服务器上验证用户名/密码是否正确，成功则返回该用户信息，失败则抛出异常。
     * @param username
     * @param password
     * @return Verified User
     * @throws HttpException 验证失败及其他非200响应均抛出异常
     */
    public User login(String username, String password) throws HttpException {
        logger.info("Login attempt for " + username);
        http.setCredentials(username, password);
        
        // Verify userName and password on the server. 
        User user = verifyCredentials();
        
        if (null != user && user.getId().length() > 0) {
        }
        
        return user;
    }
    
    /**
     * Reset HttpClient's Credentials
     */
    public void reset() {
        http.reset(); 
    }

    /**
     * Whether Logged-in
     * @return 
     */
    public boolean isLoggedIn() {
        // HttpClient的userName&password是由TwitterApplication#onCreate
        // 从SharedPreferences中取出的，他们为空则表示尚未登录，因为他们只在验证
        // 账户成功后才会被储存，且注销时被清空。
       return isValidCredentials(http.getUserId(), http.getPassword());
    }

    /**
     * Sets the base URL
     *
     * @param baseURL String the base URL
     */
    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * Returns the base URL
     *
     * @return the base URL
     */
    public String getBaseURL() {
        return this.baseURL;
    }

    /**
     * Returns authenticating userid
     * 注意：此userId不一定等同与饭否用户的user_id参数
     * 它可能是任意一种当前用户所使用的ID类型（如邮箱，用户名等），
     *
     * @return userid
     */
    public String getUserId() {
        return http.getUserId();
    }
    
    /**
     * Returns authenticating password
     *
     * @return password
     */
    public String getPassword() {
        return http.getPassword();
    }
    
    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws HttpException 
     */

    protected Response get(String url, boolean authenticate) throws HttpException {
        return get(url, null, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @param name1        the name of the first parameter
     * @param value1       the value of the first parameter
     * @return the response
     * @throws HttpException 
     */

    protected Response get(String url, String name1, String value1, boolean authenticate) throws HttpException {
        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add( new BasicNameValuePair(name1, HttpClient.encode(value1) ) );
        return get(url, params, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param name1        the name of the first parameter
     * @param value1       the value of the first parameter
     * @param name2        the name of the second parameter
     * @param value2       the value of the second parameter
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws HttpException 
     */

    protected Response get(String url, String name1, String value1, String name2, String value2, boolean authenticate) throws HttpException {
        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair(name1, HttpClient.encode(value1)));
        params.add(new BasicNameValuePair(name2, HttpClient.encode(value2)));
        return get(url, params, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws HttpException 
     */
    protected Response get(String url, ArrayList<BasicNameValuePair> params, boolean authenticated) throws HttpException {
        if (url.indexOf("?") == -1) {
            url += "?source=" + CONSUMER_KEY;
        } else if (url.indexOf("source") == -1) {
            url += "&source=" + CONSUMER_KEY;
        }
        
        //以HTML格式获得数据，以便进一步处理
        url += "&format=html";

        if (null != params && params.size() > 0) {
            url += "&" + HttpClient.encodeParameters(params);
        }
        
        return http.get(url, authenticated);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param paging controls pagination
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws HttpException 
     */
    protected Response get(String url, ArrayList<BasicNameValuePair> params, Paging paging, boolean authenticate) throws HttpException {
        if (null == params) {
            params = new ArrayList<BasicNameValuePair>();
        }
        
        if (null != paging) {
            if ("" != paging.getMaxId()) {
                params.add(new BasicNameValuePair("max_id", String.valueOf(paging.getMaxId())));
            }
            if ("" != paging.getSinceId()) {
                params.add(new BasicNameValuePair("since_id", String.valueOf(paging.getSinceId())));
            }
            if (-1 != paging.getPage()) {
                params.add(new BasicNameValuePair("page", String.valueOf(paging.getPage())));
            }
            if (-1 != paging.getCount()) {
                params.add(new BasicNameValuePair("count", String.valueOf(paging.getCount())));
            }
            
            return get(url, params, authenticate);
        } else {
            return get(url, params, authenticate);
        }
    }
    
    /**
     * 生成POST Parameters助手
     * @param nameValuePair 参数(一个或多个)
     * @return post parameters
     */
    public ArrayList<BasicNameValuePair> createParams(BasicNameValuePair... nameValuePair ) {
        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        for (BasicNameValuePair param : nameValuePair) {
            params.add(param);
        }
        return params;
    }
    
    /***************** API METHOD START *******************/
    
    
    public Response test() throws HttpException {
        return get(getBaseURL() + "test", true);
    }
    
    /* 日记方法 */
    
    /**
     * 更新一篇日记
     * 
     * @param title 日记标题
     * @param content 日记内容
     * @return
     * @throws HttpException
     */
    public Response updateDiary(String title, String content) throws HttpException {
        return http.post(getBaseURL() + "diarys", createParams(
                    new BasicNameValuePair("title", title),
                    new BasicNameValuePair("content", content)
                    ), true);
    }
    
    /**
     * 获取一篇日记
     * 
     * @param diary_id 日记ID
     * @return
     * @throws HttpException
     */
    public Response getDiary(String diary_id) throws HttpException {
        return get(getBaseURL() + "diarys", createParams(
                new BasicNameValuePair("id", diary_id)
                ), true);
    }
    
    /* 账户方法 */
    
    /**
     * Returns an HTTP 200 OK response code and a representation of the requesting user if authentication was successful; returns a 401 status code and an error message if not.  Use this method to test if supplied user credentials are valid.
     * 注意： 如果使用 错误的用户名/密码 多次登录后，饭否会锁IP
     * 返回提示为“尝试次数过多，请去 http://fandou.com 登录“,且需输入验证码
     * 
     * 登录成功返回 200 code
     * 登录失败返回 401 code
     * 使用HttpException的getStatusCode取得code
     *
     * @return user
     * @since androidroid 0.5.0
     * @throws HttpException when Weibo service or network is unavailable
     * @see <a href="http://code.google.com/p/fanfou-api/wiki/ApiDocumentation"</a>
     */
    public User verifyCredentials() throws HttpException {
        return new User(get(getBaseURL() + "account/verify_credentials.json"
                , true).asJSONObject());
    }
    
}
