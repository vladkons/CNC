package cnc.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.File;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.file.Files;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import twitter4j.User;

import twitter4j.auth.OAuthAuthorization;

import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.codehaus.jettison.json.JSONObject;

public class CNCTwitter {
    public CNCTwitter() {
        super();
    }


    public static void main(String[] args) {

        System.out.println(twitterUserExists());
    }


    public void tweet() {
        ConfigurationBuilder build = new ConfigurationBuilder();

        build.setOAuthAccessToken("902248303571591168-2Bl5uRMo2xjjNlQjDqL7M45agWeMPro");
        build.setOAuthAccessTokenSecret("w1vT69wszzNbNhkzQJlTSUcOpnzRUxw14rOFB2qSpGPwZ");
        build.setOAuthConsumerKey("zdKLMxNi1Lw5k1Rhs2od3cTB6");
        build.setOAuthConsumerSecret("iXVMUTpuPucbtKqXyd8qppPMSb92DiAxG25EK1WYnbwAwTkUAp");
        OAuthAuthorization auth = new OAuthAuthorization(build.build());
        Twitter twitter = new TwitterFactory().getInstance(auth);


        try {
            User user = twitter.verifyCredentials();
            System.out.println(user.getScreenName());

            StatusUpdate status = new StatusUpdate("@Frldr Posted a picture.");


            ByteArrayInputStream bis =
                new ByteArrayInputStream(Files.readAllBytes(new File("D:\\CNCDemo\\Images\\bridge.jpg").toPath()));

            status.setMedia("Picture", bis);
            twitter.updateStatus(status);

        } catch (Exception e) {

            System.out.println(ExceptionString.toString(e));

        }
    }

    public static boolean twitterUserExists() {
        try {

            URL url = new URL("https://twitter.com/users/username_available?username=Frldr");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = null;
            String nextLine = null;
            System.out.println("Output from Server .... \n");
            
            while ((nextLine = br.readLine()) != null) {
                output = nextLine;
                System.out.println(output);
            }

            conn.disconnect();
            
            JSONObject user = new JSONObject(output);   
            
            // Twitter should return the following JSON if user exists: 
            // {"valid":true,"reason":"available","msg":"Available!","desc":"Available!"}
            return   user.has("valid") && (Boolean) user.get("valid");

        } catch (Exception e) {

            e.printStackTrace();

        }
        
        return false;


    }
}
