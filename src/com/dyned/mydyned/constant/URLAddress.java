package com.dyned.mydyned.constant;

public class URLAddress {

	
//	public static final String DEVELOPMENT_DOMAIN = "http://idbuild.id.dyned.com:8080/mdc/index.php";
	public static final String DYNED_DOMAIN = "https://mobile.dyned.com/index.php"; 
//	public static final String DYNED_DOMAIN = DEVELOPMENT_DOMAIN;
	public static final String CMS_DOMAIN = "http://cmsmda.dyned.com/index.php";
	
	public static final String VERSION_CHECK = CMS_DOMAIN + "/api/application_temp/detail?id=5";
	
	public static final String MEDIA_MENU = CMS_DOMAIN + "/api/media_categories/list_categories";
	public static final String MEDIA_URL = CMS_DOMAIN + "/api/media_files/by_category";
	public static final String MEDIA_DETAIL_URL = CMS_DOMAIN + "/api/media_files/detail";
	public static final String DYNED_APP_URL = CMS_DOMAIN + "/api/applications/os?id=2";
	
	public static final String NEWS_URL = DYNED_DOMAIN + "/api/news";
	public static final String NEWS_DETAIL_URL = DYNED_DOMAIN + "/api/news/detail";
	public static final String EVENT_URL = DYNED_DOMAIN + "/api/events";
	public static final String ASSESSMENT_URL = DYNED_DOMAIN + "/api/assessment";
	public static final String ASSESSMENT_SUBMIT_URL = DYNED_DOMAIN + "/api/assessment/submit";
	public static final String EVENT_DETAIL_URL = DYNED_DOMAIN + "/api/events/detail";
	
	public static final String SUPPORT_URL = CMS_DOMAIN + "/api/support";
	public static final String SOCIAL_MEDIA_URL = DYNED_DOMAIN + "/api/socmed";
	
	public static final String API_KEY = "c31b32364ce19ca8fcd150a417ecce58";
	
	public static final String LOGIN_URL = DYNED_DOMAIN + "/api/oauth/login";
	public static final String FORGOT_PASSWORD_URL = DYNED_DOMAIN + "/account/oauth";
	public static final String SCHOOLS_URL = DYNED_DOMAIN + "/api/2/school";
//	public static final String SCHOOLS_DETAIL_URL = "http://mdc.pistarlabs.net/0.1.0/index.php/api/2/school/detail";
	public static final String SCHOOLS_DETAIL_URL = DYNED_DOMAIN + "/api/2/school/detail";
	
	public static final String COUNTRY_URL = DYNED_DOMAIN + "/api/helper/country_list";
	public static final String LANGUAGE_URL = DYNED_DOMAIN + "/api/helper/language_list";
	public static final String PROFILE_URL = DYNED_DOMAIN + "/api/account/profile";
	public static final String UPDATE_PROFILE_URL = DYNED_DOMAIN + "/api/account/update_profile";
	public static final String UPDATE_PROFILE_AVATAR = DYNED_DOMAIN + "/api/account/update_picture";
	public static final String REGISTER_URL = DYNED_DOMAIN + "/api/oauth/register";

	public static final String SET_DEVICE_ID = DYNED_DOMAIN + "/api/device/add";
//	public static final String RECORD_URL = DYNED_DOMAIN + "/account";
	
	public static final String RECORD_URL = DYNED_DOMAIN + "/account";
	
	public static final String GEO_IP = "https://freegeoip.net/json/";
	
	
}