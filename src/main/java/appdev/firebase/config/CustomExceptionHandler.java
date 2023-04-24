package appdev.firebase.config;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import com.google.cloud.firestore.FirestoreException;
import com.google.firebase.FirebaseException;

public class CustomExceptionHandler {

    @ExceptionHandler(value = {FirestoreException.class})
    public ResponseEntity<String> handleFirestoreException(FirestoreException ex) {
        return new ResponseEntity<>("Firestore exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {FirebaseException.class})
    public ResponseEntity<String> handleFirebaseException(FirebaseException ex) {
        return new ResponseEntity<>("Firestore exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {HttpClientErrorException.class})
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException ex) {
        return new ResponseEntity<>("HTTP client error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // @ExceptionHandler(value = {IllegalArgumentException.class})
    // public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    //     return new ResponseEntity<>("Illegal argument exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    // }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        return new ResponseEntity<>("Null pointer exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ConnectException.class})
    public ResponseEntity<String> handleConnectException(ConnectException ex) {
        return new ResponseEntity<>("Connection exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {SocketTimeoutException.class})
    public ResponseEntity<String> handleSocketTimeoutException(SocketTimeoutException ex) {
        return new ResponseEntity<>("Socket timeout exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {TimeoutException.class})
    public ResponseEntity<String> handleTimeoutException(TimeoutException ex) {
        return new ResponseEntity<>("Timeout exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Add more exception handler methods here for other possible exceptions

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>("An exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public ResponseEntity<String> customExceptionHandler(Exception ex) {
        if (ex instanceof FirestoreException) {
            return new ResponseEntity<>("Firestore exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof FirebaseException) {
            return new ResponseEntity<>("Firebase exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof HttpClientErrorException) {
            return new ResponseEntity<>("HTTP client error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        // } else if (ex instanceof IllegalArgumentException) {
        //     return new ResponseEntity<>("Illegal argument exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof NullPointerException) {
            return new ResponseEntity<>("Null pointer exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof ConnectException) {
            return new ResponseEntity<>("Connection exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof SocketTimeoutException) {
            return new ResponseEntity<>("Socket timeout exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof TimeoutException) {
            return new ResponseEntity<>("Timeout exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>("An exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}

