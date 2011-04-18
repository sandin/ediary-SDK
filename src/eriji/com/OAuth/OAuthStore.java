package eriji.com.OAuth;

public interface OAuthStore {
    
    void store(String key, OAuthToken token) throws OAuthStoreException;
    
    OAuthToken get(String key, String TokenType) throws OAuthStoreException;
    
    boolean isExists(String key);
}
