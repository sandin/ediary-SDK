package eriji.com.OAuth;

public class OAuthAccessToken extends OAuthToken {

    public OAuthAccessToken(String token, String tokenSecret) {
        super(token, tokenSecret, OAuthToken.ACCESSS_TOKEN);
    }
    

}
