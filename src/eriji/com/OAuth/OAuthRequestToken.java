package eriji.com.OAuth;

public class OAuthRequestToken extends OAuthToken {

    public OAuthRequestToken(String token, String tokenSecret) {
        super(token, tokenSecret, OAuthToken.REQUEST_TOKEN);
    }
    

}
