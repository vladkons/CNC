package cnc.client.rest;

import cnc.model.Attendee;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.math.BigDecimal;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.codec.binary.Base64;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class WSDAO {
    public WSDAO() {
        super();
    }

    private final static String serverIP = "replace_server_ip";

    static {
        System.setProperty("https.protocols", "TLSv1.1");
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
           
                if (hostname.equals(serverIP))
                    return true;
                return false;
            }
        });
    }

    public static boolean twitterUserExists(String handle) {
        try {

            URL url = new URL("https://twitter.com/users/username_available?username=" + handle);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = null;
            String nextLine = null;

            //Note - expect a single line
            while ((nextLine = br.readLine()) != null) {
                output = nextLine;
            }

            conn.disconnect();

            JSONObject user = new JSONObject(output);

            // Twitter should return the following JSON if user exists:
            // {"valid":true,"reason":"available","msg":"Available!","desc":"Available!"}

            // Note - we want "valid" to be "false", it means that the userid is taken(user exists)
            return user.has("valid") && !(Boolean) user.get("valid");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    //Return all attendees PARTIALLY matching "name"
    public static ArrayList<Attendee> getAttendeesByName(String name) {

        ArrayList<Attendee> attendeeList = new ArrayList<Attendee>();

        try {


            URL url = new URL("https://" + serverIP + "/cnc/resources/service/getAttendee/" + name);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Accept", "application/json");


            URLConnection uc = url.openConnection();
            String userpass = "CNC" + ":" + "replace_password";
            String basicAuth = "Basic " + new String((new Base64()).encode(userpass.getBytes()));
            conn.setRequestProperty("Authorization", basicAuth);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = null;
            String nextLine = null;

            //Note - expect a single line
            while ((nextLine = br.readLine()) != null) {
                output = nextLine;
                //            System.out.println(output);
            }

            conn.disconnect();
            
            // No results
            if (output.startsWith("No results found")){
                return null;
            }
            
            // Should return array of attendees
            JSONObject ret = new JSONObject(output);

            if (ret.has("attendees")) {
                JSONArray attendees = ret.getJSONArray("attendees");

                for (int i = 0; i < attendees.length(); i++) {
                    JSONObject att = attendees.getJSONObject(i);


                    Attendee attendee =
                        new Attendee(att.has("pk") ? new BigDecimal(att.getString("pk")) : new BigDecimal(-1),
                                     att.has("name") ? att.getString("name") : "",
                                     att.has("image") ? att.getString("image") : "",
                                     att.has("imageThumb") ? att.getString("imageThumb") : "",
                                     att.has("imageMorph") ? att.getString("imageMorph") : "",
                                     att.has("imageAssoc") ? att.getString("imageAssoc") : "",
                                     att.has("cncPath") ? att.getString("cncPath") : "");

                    attendeeList.add(attendee);


                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return attendeeList;

    }


    //Return a page of the most recently(creation date descending order) created attendees  -
    //page is defined as between 'first' and 'last'
    //Service enforces a limit of 20 (last - first < 20) per page
    public static ArrayList<Attendee> getAttendeesByPage(int first, int last) {

        ArrayList<Attendee> attendeeList = new ArrayList<Attendee>();

        try {


            URL url = new URL("https://" + serverIP + "/cnc/resources/service/getAttendeePaged/" + first + ":" + last);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Accept", "application/json");


            URLConnection uc = url.openConnection();
            String userpass = "CNC" + ":" + "replace_password";
            String basicAuth = "Basic " + new String((new Base64()).encode(userpass.getBytes()));
            conn.setRequestProperty("Authorization", basicAuth);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = null;
            String nextLine = null;

            //Note - expect a single line
            while ((nextLine = br.readLine()) != null) {
                output = nextLine;
                //            System.out.println(output);
            }

            conn.disconnect();

            // Should return array of attendees
            JSONObject ret = new JSONObject(output);

            if (ret.has("attendees")) {
                JSONArray attendees = ret.getJSONArray("attendees");

                for (int i = 0; i < attendees.length(); i++) {
                    JSONObject att = attendees.getJSONObject(i);


                    Attendee attendee =
                        new Attendee(att.has("pk") ? new BigDecimal(att.getString("pk")) : new BigDecimal(-1),
                                     att.has("name") ? att.getString("name") : "",
                                     att.has("image") ? att.getString("image") : "",
                                     att.has("imageThumb") ? att.getString("imageThumb") : "",
                                     att.has("imageMorph") ? att.getString("imageMorph") : "",
                                     att.has("imageAssoc") ? att.getString("imageAssoc") : "",
                                     att.has("cncPath") ? att.getString("cncPath") : "");

                    attendeeList.add(attendee);


                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return attendeeList;

    }


    // Get attendee by ID ( ID coulumn is a PK in the table)
    public static Attendee getAttendeeByID(int id) {

        Attendee attendee = null;

        try {


            URL url = new URL("https://" + serverIP + "/cnc/resources/service/getAttendeeByID/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Accept", "application/json");


            URLConnection uc = url.openConnection();
            String userpass = "CNC" + ":" + "replace_password";
            String basicAuth = "Basic " + new String((new Base64()).encode(userpass.getBytes()));
            conn.setRequestProperty("Authorization", basicAuth);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = null;
            String nextLine = null;

            //Note - expect a single line
            while ((nextLine = br.readLine()) != null) {
                output = nextLine;
                //            System.out.println(output);
            }

            conn.disconnect();

            // Should return array of attendees
            JSONObject att = new JSONObject(output);

            if (att != null) {

                attendee =
                    new Attendee(att.has("pk") ? new BigDecimal(att.getString("pk")) : new BigDecimal(-1),
                                 att.has("name") ? att.getString("name") : "",
                                 att.has("image") ? att.getString("image") : "",
                                 att.has("imageThumb") ? att.getString("imageThumb") : "",
                                 att.has("imageMorph") ? att.getString("imageMorph") : "",
                                 att.has("imageAssoc") ? att.getString("imageAssoc") : "",
                                 att.has("cncPath") ? att.getString("cncPath") : "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return attendee;

    }
 



}
