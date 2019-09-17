package com.forecaster_app.Utility;

import android.util.Patterns;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.regex.Pattern;


public class Validation {

    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "^[0-9, ,+]{10}$";

    // Error Messages
    private static final String REQUIRED_MSG = "required field";
    private static final String EMAIL_MSG = "invalid email";
    private static final String PHONE_MSG = "invalid number";
    private static String usernametext;


    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }

    public static boolean isRadioButton(RadioButton radioButton){
        if (!radioButton.getText().toString().equalsIgnoreCase("Male") && !radioButton.getText().toString().equalsIgnoreCase("Female") && !radioButton.getText().toString().equalsIgnoreCase("Other")) {
            return false;
        }
        return true;
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        usernametext = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (usernametext.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

    public static boolean password(EditText editText) {
        Boolean ret=true;
        String text=editText.getText().toString().trim();
        if(text.length()<6 || usernametext.equals(text) || text.length()>8)
        {
            if(text.length()<6) {

                editText.setError("Your password must be at least of six digits");
                editText.setFocusable(true);
                ret = false;
            }
            else if(text.length()>8)
            {
                editText.setError("Your password need not be maximum of eight digits");
                editText.setFocusable(true);
                ret=false;

            }
            else if(usernametext.equals(text))
            {
                editText.setError("Your password need not be same as username");
                editText.setFocusable(true);
                ret=false;

            }




        }
        else if(text.length()==0)
        {
            editText.setError("Required");
            editText.setFocusable(true);
            ret=false;
        }


        return ret;
    }

    public static boolean email(EditText editText) {
        boolean ret=true;
        String text=editText.getText().toString().trim();
        if(text.length()==0 || !Patterns.EMAIL_ADDRESS.matcher(text).matches())
        {

            if(text.length()==0) {
                editText.setError(REQUIRED_MSG);
                editText.setFocusable(true);
                ret = false;
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(text).matches())
            {
                editText.setError("Not a valid Email format\neg: abc@domain.com");
                editText.setFocusable(true);
                ret = false;
            }
        }

        return ret;
    }
}
