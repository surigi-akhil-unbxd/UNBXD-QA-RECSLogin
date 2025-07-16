package lib;

public enum UrlMapper {

    LOGIN("/login"),
    SIGN_UP("/signup"),

    WELCOME_PAGE("/ss/newuserlanding"),
    CREATE_SITE_PAGE("/ss/onboarding"),

    SYNONYMS("/ss/search/algorithms/%s/content/synonyms"),
    PHRASES("/ss/search/algorithms/%s/content/phrases"),
    STEMMING("/ss/search/algorithms/%s/content/stemming"),
    CONCEPTS("/ss/search/algorithms/%s/content/concepts"),
    STOP_WORD("/ss/search/algorithms/%s/content/stopwords"),
    RELEVANCY_PAGE("/ss/onboarding/%s"),

    // RECS URL:
    EXPERIENCES("/recs-v2/sites/%s/experience");

    private final String urlPath;
    UrlMapper(String url) {
        this.urlPath=url;
    }

    public String getUrlPath(String... args)
    {
        String result=String.format(this.urlPath,args);
        return EnvironmentConfig.getApplicationUrl()+result;
    }

   /* public String getBaseUrlPath(String... args)
    {
        String result=String.format(this.urlPath,args);
        return EnvironmentConfig.getBaseUrl()+result;
    }*/

    public String getUrlPathFromAppUrl(String applicationUrl, String... args){
        String result=String.format(this.urlPath,args);
        return applicationUrl+result;
    }

    public String getBaseUrl(String... args)
    {
        String result=String.format(this.urlPath,args);
        return EnvironmentConfig.getBaseUrl()+result;
    }

    public String getUrlPathFromBaseUrl(String... args) {
        String result = String.format(this.urlPath, args);
        return EnvironmentConfig.getBaseUrl() + result;
    }

}
