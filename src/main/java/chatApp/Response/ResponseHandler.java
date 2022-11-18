package chatApp.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> generateResponse(boolean success, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", success);
        map.put("data", responseObj);

        return new ResponseEntity<Object>(map,status);
    }
    public static ResponseEntity<Object> generateErrorResponse(boolean success,HttpStatus status, Object errors) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", success);
        map.put("errors", errors);

        return new ResponseEntity<Object>(map,status);
    }

}