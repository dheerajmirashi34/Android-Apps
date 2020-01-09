package com.example.emailappnew.Utils;

/**
 * Author: Jatin N Gupte, Dheeraj Mirashi
 * Group No: 50
 */
public interface AppConstants {
    String AUTHORIZATION_HEADER = "Authorization";
    String FORM_PARAM_EMAIL = "email";
    String FORM_PARAM_PASSWORD = "password";
    String FORM_PARAM_FNAME = "fname";
    String FORM_PARAM_LNAME = "lname";
    String AUTH_TOKEN_KEY = "token";
    String USER_NAME_KEY = "userName";
    String RECEIVER_ID_KEY = "receiver_id";
    String SUBJECT_KEY = "subject";
    String MESSAGE_KEY = "message";

    //URL's
    String SIGNUP_URL = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup";
    String LOGIN_URL = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login";
    String INBOX_GET_URL = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox";
    String USERS_GET_URL = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/users";
    String INBOX_ADD_URL = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/add";
    String INBOX_DELETE_URL = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/delete";
}
