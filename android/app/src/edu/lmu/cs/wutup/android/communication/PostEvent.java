package edu.lmu.cs.wutup.android.communication;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.lmu.cs.wutup.android.manager.LogTags;

public class PostEvent extends HttpWutup {
    
    public static final String DEFAULT_ERROR_MESSAGE = "Failed to post event!";

    
    public static final int INDEX_OF_NAME_IN_PARAMETERS = 0;
    public static final int INDEX_OF_DESCRIPTION_IN_PARAMETERS = 1;
    
    @Override
    protected Object doInBackground(Object... parameters) {
        
        int idOfPostedEvent = -1;
        
        if (parameters[INDEX_OF_NAME_IN_PARAMETERS] instanceof String &&
            parameters[INDEX_OF_DESCRIPTION_IN_PARAMETERS] instanceof String) {
            
            String name = (String) parameters[INDEX_OF_NAME_IN_PARAMETERS];
            String description = (String) parameters[INDEX_OF_DESCRIPTION_IN_PARAMETERS];
            
            try {  
                idOfPostedEvent = postEvent(name, description);
                
            } catch (ClientProtocolException clientProtocolException) {
                Log.e(LogTags.POST, DEFAULT_ERROR_MESSAGE, clientProtocolException);
                
            } catch (IOException ioException) {
                Log.e(LogTags.POST, DEFAULT_ERROR_MESSAGE, ioException);
            }
            
        } else {
            throw new IllegalArgumentException();
        }
        
        return idOfPostedEvent;
        
    }
    
    private int postEvent(String name, String description) throws ClientProtocolException, IOException {
        
        HttpPost postEvent = new HttpPost(ADDRESS_OF_EVENTS);
        String jsonForPostingEvent = generateJsonForPostingEvent(name, description);
        StringEntity entityForPostingEvent = new StringEntity(jsonForPostingEvent);
        
        postEvent.setEntity(entityForPostingEvent);
        postEvent.addHeader("Content-type", "application/json");
        
        HttpResponse responceToPostingEvent = client.execute(postEvent);
        int idOfPostedEvent = extractEventId(responceToPostingEvent);
                
        Log.i(LogTags.POST, "Executed HTTP call to post event with the following JSON. " + jsonForPostingEvent + 
                      " Posted event assigned ID " + idOfPostedEvent + ".");
        
        return idOfPostedEvent;
        
    }
    
    private String generateJsonForPostingEvent(String name, String description) throws JsonProcessingException {
        
        String jsonFormat = "{\"name\":\"%s\",\"description\":\"%s\",\"creator\":{\"email\":\"40mpg@gmail.com\"}}";
        String json = String.format(jsonFormat, name, description);
        
        return json;
                
    }
    
    private int extractEventId(HttpResponse responceToPostingEvent) {
        
        String locationOfPostedEvent = responceToPostingEvent.getFirstHeader("Location").getValue();        
        int indexOfForwardSlashPrecedingId = -1;
        
        for (int index = 0; index < locationOfPostedEvent.length(); index++) {
            
            if (locationOfPostedEvent.charAt(index) == '/') {
                indexOfForwardSlashPrecedingId = index;
            }
            
        }
        
        int eventId = Integer.parseInt(locationOfPostedEvent.substring(indexOfForwardSlashPrecedingId + 1));
        
        return eventId;
        
    }

}