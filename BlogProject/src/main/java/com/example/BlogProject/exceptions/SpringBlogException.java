package com.example.BlogProject.exceptions;

public class SpringBlogException extends Throwable{

    //when exeptions occur, we do not want to expose the nature of them to the user
    //crate custom exception to pass in our own exception messages for a better user experience


    public SpringBlogException(String exMessage) {
        super(exMessage);
    }

    public SpringBlogException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }
}
