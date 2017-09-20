package cnc.server.rest;

import cnc.jdbc.AttendeeDAO;

import cnc.model.Attendee;

import cnc.util.CNCResult;
import cnc.util.ExceptionString;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;

import twitter4j.auth.OAuthAuthorization;

import twitter4j.conf.ConfigurationBuilder;


@Path("/service")
//@RolesAllowed({"CNCRole"})
public class AttendeeService {
    
    private  Attendee attendee;
    private  List<Attendee> listAttendee;

    public AttendeeService() {
        super();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAttendeeCount")
//    @RolesAllowed({"CNCRole"})
    public Response getAttendeeCount(){
               
         
        CNCResult res = AttendeeDAO.getAttendeeCount();
        //If error - pass to client
        if (res.getErr().length()>0){
            return Response.status(Status.OK).entity(res.getErr()).build();
        }
        
        String count = (String) res.getObj();
        JSONObject ret = new JSONObject();
        try {
            ret.put("count", count);
        } catch (JSONException e) {
            return Response.status(Status.OK).entity(ExceptionString.toString(e)).build();
        }
        return Response.status(Status.OK).entity(ret.toString()).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAttendeePaged/{range}")
    public Response getAttendeePaged(@PathParam("range") String range){
        
        JSONObject ret = null;
        JSONArray jarr = null;
        try {
            //Range parameter is expected in format "n:m"
            //defining the first and last record to be retrieved 
            String[] firstLast= range.split(":");
            
            int first = Integer.parseInt(firstLast[0]);
            int last =  Integer.parseInt(firstLast[1]);
            
            //Check if page request is too large, limit to 20.
            if ((first + 19) < (last)){
                last = first + 19;
            }
            CNCResult res = AttendeeDAO.getAttendeePaged(first, last);
            //If error - pass to client
            if (res.getErr().length()>0){
                return Response.status(Status.OK).entity(res.getErr()).build();
            }
            
            ArrayList<Attendee> list = (ArrayList<Attendee>) res.getObj();
            
            //Nothing found
            if (list.size() == 0){
                return Response.status(Status.OK).entity("No results found for range parameter '" + firstLast[0] + ":" + firstLast[1] + "'").build(); 
            }
            
            //Return attendees
            jarr = new JSONArray();
            for (Attendee att : list){
                JSONObject obj = new JSONObject();
                obj.put("pk", att.getPk()); 
                obj.put("name", att.getName());
                obj.put("image", att.getImage());
                obj.put("imageThumb", att.getImageThumb());
                obj.put("imageMorph", att.getImageMorph());
                obj.put("imageAssoc", att.getImageAssoc());
                obj.put("cncPath", att.getCncPath());
                
                jarr.put(obj);
            }
                
           ret = new JSONObject();
           ret.put("attendees", jarr);
        } catch (JSONException e) {
            return Response.status(Status.OK).entity(ExceptionString.toString(e)).build();
        }
        return Response.status(Status.OK).entity(ret.toString()).build();
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAttendee/{search}")
    public Response getAttendeeByName(@PathParam("search") String searchName){
        
        JSONObject ret = null;
        JSONArray jarr = null;
        try {
         
            
            CNCResult res = AttendeeDAO.getAttendee(searchName);
            //If error - pass to client
            if (res.getErr().length()>0){
                return Response.status(Status.OK).entity(res.getErr()).build();
            }
            
            ArrayList<Attendee> list = (ArrayList<Attendee>) res.getObj();
            
            //Nothing found
            if (list.size() == 0){
                return Response.status(Status.OK).entity("No results found for search parameter '" + searchName + "'").build(); 
            }
            
            //Return attendees
            jarr = new JSONArray();
            for (Attendee att : list){
                JSONObject obj = new JSONObject();
                obj.put("pk", att.getPk()); 
                obj.put("name", att.getName());
                obj.put("image", att.getImage());
                obj.put("imageThumb", att.getImageThumb());
                obj.put("imageMorph", att.getImageMorph());
                obj.put("imageAssoc", att.getImageAssoc());
                obj.put("cncPath", att.getCncPath());
                
                jarr.put(obj);
            }
               
           ret = new JSONObject();
           ret.put("attendees", jarr);
        } catch (JSONException e) {
            return Response.status(Status.OK).entity(ExceptionString.toString(e)).build();
        }
        return Response.status(Status.OK).entity(ret.toString()).build();
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAttendeeByID/{id}")
    public Response getAttendeeById(@PathParam("id") String id){
        
        JSONObject ret = null;
        JSONArray jarr = null;
        try {
         
            
            CNCResult res = AttendeeDAO.getAttendeeByID(Integer.parseInt(id));
            //If error - pass to client
            if (res.getErr().length()>0){
                return Response.status(Status.OK).entity(res.getErr()).build();
            }
            
            Attendee att = (Attendee) res.getObj();
            
            //Nothing found
            if (att == null){
                return Response.status(Status.OK).entity("No results found for ID  '" + id + "'").build(); 
            }
            
            //Return attendees
           
            ret = new JSONObject();
            ret.put("pk", att.getPk()); 
            ret.put("name", att.getName());
            ret.put("image", att.getImage());
            ret.put("imageThumb", att.getImageThumb());
            ret.put("imageMorph", att.getImageMorph());
            ret.put("imageAssoc", att.getImageAssoc());
            ret.put("cncPath", att.getCncPath());
                
        } catch (JSONException e) {
            return Response.status(Status.OK).entity(ExceptionString.toString(e)).build();
        }
        return Response.status(Status.OK).entity(ret.toString()).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("setAttendee")
    public String setAttendee(@Context Request request, String json) {
        JSONObject jobj = null;
        String ret = "OK";
        
        try {
            jobj = new JSONObject(json);            
            Attendee att =  new Attendee(
                                            jobj.has("name") ? jobj.getString("name") : "",
                                            jobj.has("company") ? jobj.getString("company") : "", 
                                            jobj.has("email") ? jobj.getString("email") : "",
                                            jobj.has("twitter") ? jobj.getString("twitter") : "",
                                            jobj.has("image") ? jobj.getString("image") : "",
                                            jobj.has("imageThumb") ? jobj.getString("imageThumb") : "",
                                            jobj.has("imageMorph") ? jobj.getString("imageMorph") : "",
                                            jobj.has("imageAssoc") ? jobj.getString("imageAssoc") : "",
                                            jobj.has("cncPath") ? jobj.getString("cncPath") : ""
                                        );
            // Insert in DB
            AttendeeDAO.insertAttendee(att);
            
            String handle = att.getTwitter();
            // Tweet, if the user actually exists 
            if (handle!=null && handle.length()>0 && twitterUserExists(att.getTwitter())){
                tweet(att.getTwitter(), att.getImageAssoc()); 
            }
                                        
        } catch (Exception e) {
            ret = ExceptionString.toString(e);
        }
        
        return ret;     
    }
    

//    @GET
//    @Produces(value = { "application/json", "multipart/form-data" })
//    @Path("getAttendee")
    public Attendee getAttendee() {
        return attendee;
    }

    public void setListAttendee(List<Attendee> listAttendee) {
        this.listAttendee = listAttendee;
    }


//    @POST
//    @Consumes(value = { "application/json", "multipart/form-data" })
//    @Produces(value = { "application/json", "multipart/form-data" })
//    @Path("getListAttendee")
    public List<Attendee> getListAttendee() {
        return listAttendee;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("verifyHandle/{handle}")
    public Response verifyHandle(@PathParam("handle") String handle){
        
        JSONObject ret = null;
        
        try{
            ret = new JSONObject();
            ret.put("exists", twitterUserExists(handle)); 
        } catch (Exception e) {
            return Response.status(Status.OK).entity(ExceptionString.toString(e)).build();
        }
        
        return Response.status(Status.OK).entity(ret.toString()).build();        
    }

// Using @odevcommunity twitter account
    private void tweet(String handle, String encodedImage) throws Exception {
        
        ConfigurationBuilder build = new ConfigurationBuilder();
        //As passed for @odevcommunity
        build.setOAuthAccessToken("902248303571591168-2Bl5uRMo2xjjNlQjDqL7M45agWeMPro");
        build.setOAuthAccessTokenSecret("w1vT69wszzNbNhkzQJlTSUcOpnzRUxw14rOFB2qSpGPwZ");
        build.setOAuthConsumerKey("zdKLMxNi1Lw5k1Rhs2od3cTB6");
        build.setOAuthConsumerSecret("iXVMUTpuPucbtKqXyd8qppPMSb92DiAxG25EK1WYnbwAwTkUAp");
        OAuthAuthorization auth = new OAuthAuthorization(build.build());
        Twitter twitter = new TwitterFactory().getInstance(auth);
        
        
            User user = twitter.verifyCredentials();
            System.out.println(user.getScreenName());
            
            StatusUpdate status = new StatusUpdate("@" + handle + " attended OOW2017 in San Francisco.");
            
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(encodedImage));
                 
            status.setMedia("Picture", bis);
            twitter.updateStatus(status);
            
    }
    
    
    private boolean twitterUserExists(String handle){
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
                
                while ((nextLine = br.readLine()) != null) {
                    output = nextLine;
                }

                conn.disconnect();
                
                JSONObject user = new JSONObject(output);   
                
                // Twitter should return the following JSON if user exists: 
                // {"valid":true,"reason":"available","msg":"Available!","desc":"Available!"}
                
                // Note - we want "valid" to be "false", it means that the userid is taken(user exists)
                return user.has("valid") &&  !(Boolean)user.get("valid");
                

            } catch (Exception e) {
                e.printStackTrace();
            }
            
            
            return false;

        }
   
         
}
