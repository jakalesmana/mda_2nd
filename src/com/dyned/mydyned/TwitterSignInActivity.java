package com.dyned.mydyned;
//package com.pistarlabs.android.mydyned;
//
//import java.util.StringTokenizer;
//
//import twitter4j.Twitter;
//import twitter4j.TwitterException;
//import twitter4j.TwitterFactory;
//import twitter4j.auth.AccessToken;
//import twitter4j.auth.RequestToken;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.pistarlabs.android.mydyned.constant.Constant;
//import com.pistarlabs.android.mydyned.model.User;
//
///**
//author: jakalesmana
// */
//
//public class TwitterSignInActivity extends BaseActivity {
//
//	private RequestToken twitterReqToken;
//	private Twitter twitter;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_loading);
//		
//		twitter = new TwitterFactory().getInstance();
//        twitter.setOAuthConsumer(Constant.TWITTER_CONSUMER_KEY, Constant.TWITTER_CONSUMER_SECRET);
//        
//        new Thread(new Runnable() {
//			public void run() {
//				loginTwitter();
//			}
//		}).start();
//	}
//	
//	//handle Twitter Login
//		private void loginTwitter() {
//	        try {
//	                twitterReqToken = twitter.getOAuthRequestToken(Constant.TWITTER_CALLBACK_URL);
//
////	                WebView twitterSite = new WebView(this);
////	                twitterSite.requestFocus(View.FOCUS_DOWN);
////	                twitterSite.setOnTouchListener(new View.OnTouchListener() {
////	                    @Override
////	                    public boolean onTouch(View v, MotionEvent event) {
////	                        switch (event.getAction()) {
////	                            case MotionEvent.ACTION_DOWN:
////	                            case MotionEvent.ACTION_UP:
////	                                if (!v.hasFocus()) {
////	                                    v.requestFocus();
////	                                }
////	                                break;
////	                        }
////	                        return false;
////	                    }
////	                });
////	                
////	                twitterSite.loadUrl(twitterReqToken.getAuthenticationURL());
////	                setContentView(twitterSite);
//	                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterReqToken.getAuthenticationURL()));
//					startActivity(browserIntent);
//
//	        } catch (TwitterException e) {
//	                Toast.makeText(this, "Twitter Login error, try again later", Toast.LENGTH_SHORT).show();
//	        }
//		}
//		
//		//handle twitter login callback
//		protected void onNewIntent(Intent intent) {
//	        super.onNewIntent(intent);
//	        dealWithTwitterResponse(intent);
//		}
//		
//		private void dealWithTwitterResponse(Intent intent) {
//			Uri uri = intent.getData();
//	        if (uri != null && uri.toString().startsWith(Constant.TWITTER_CALLBACK_URL)) { // If the user has just logged in
//	                String oauthVerifier = uri.getQueryParameter("oauth_verifier");
//	                authoriseNewUser(oauthVerifier);
//	        }
//		}
//		
//		private void authoriseNewUser(final String oauthVerifier) {
//			System.out.println("handle twitter login");
//			new Thread(new Runnable() {
//				public void run() {
//					try {
//		                AccessToken at = twitter.getOAuthAccessToken(twitterReqToken, oauthVerifier);
//		                twitter.setOAuthAccessToken(at);
//		                	                
//		                twitter4j.User user = twitter.showUser(at.getScreenName());
//		                User dynedUser = new User();
//		                dynedUser.setTwitterId("" + user.getId());
//		                StringTokenizer tokens = new StringTokenizer(user.getName(), " ");
//		                dynedUser.setFirstName(tokens.nextToken());
//		                dynedUser.setLastName(tokens.nextToken());
//		                dynedUser.setImageUrl("https://api.twitter.com/1/users/profile_image?screen_name=" + at.getScreenName() + "&size=bigger");
//		                System.out.println("user image: " + user.getProfileImageURL());
//		                
//		                // go to register
//						Intent i = new Intent(TwitterSignInActivity.this, RegisterActivity.class);
//						i.putExtra("user", dynedUser);
//						i.putExtra("social", true);
//						startActivity(i);
//						finish();
//			        } catch (TwitterException e) {
//		                Toast.makeText(TwitterSignInActivity.this, "Twitter auth error x01, try again later", Toast.LENGTH_SHORT).show();
//			        }
//				}
//			}).start();
//	        
//		}
//}
