package com.forecaster.Utility;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.forecaster.R;

import java.util.regex.Pattern;


public class Validation extends AppCompatActivity {

    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "^[0-9, ,+]{10}$";
    private Context context;
    // Error Messages
    private static final String REQUIRED_MSG = "You ";
    private static final String EMAIL_MSG = "Invalid email is";

    private static String usernametext;



    // call this method when you need to check email validation
    public   boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    // call this method when you need to check phone number validation
    public   boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, context.getString(R.string.invalid_phone_number), required);
    }

    public  boolean isRadioButton(RadioButton radioButton){
        if (!radioButton.getText().toString().equalsIgnoreCase("Male") && !radioButton.getText().toString().equalsIgnoreCase("Female") && !radioButton.getText().toString().equalsIgnoreCase("Other")) {
            return false;
        }
        return true;
    }

    public Validation(Context context)
    {
        this.context=context;
    }

    // return true if the input field is valid, based on the parameter passed
    public  boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText,context.getString(R.string.phone_number))) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public  boolean hasText(EditText editText,String message) {

        usernametext = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (usernametext.length() == 0) {
            editText.setError(message);

            return false;
        }

        return true;
    }
    public static boolean hasText2(EditText editText,String message) {

        usernametext = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (usernametext.length() == 0) {
            editText.setError(message);
            editText.requestFocus();

            return false;
        }

        return true;
    }

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

    public static boolean hasText(EditText editText,Boolean error) {

        usernametext = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (usernametext.length() == 0) {
            if(!error) {
                editText.setError(REQUIRED_MSG);
            }
            return false;
        }

        return true;
    }

    public  boolean password(EditText editText) {
        Boolean ret=true;
        String text=editText.getText().toString().trim();
        if(text.length()<6 || usernametext.equals(text) || text.length()>8)
        {
            if(text.length()<6) {

                editText.setError(context.getString(R.string.six_digits));
                editText.setFocusable(true);
                ret = false;
            }
            else if(text.length()>8)
            {
                editText.setError(context.getString(R.string.max_eight_digits));
                editText.setFocusable(true);
                ret=false;

            }
            else if(usernametext.equals(text))
            {
                editText.setError(context.getString(R.string.password_need_not_be_same_username));
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

    public  boolean email(EditText editText) {
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
                editText.setError(context.getString(R.string.not_valid_email));
                editText.setFocusable(true);
                ret = false;
            }
        }

        return ret;
    }
    public  boolean email(EditText editText,String message) {
        boolean ret=true;
        String text=editText.getText().toString().trim();
        if(text.length()==0 || !Patterns.EMAIL_ADDRESS.matcher(text).matches())
        {

            if(text.length()==0) {
                editText.setError(message);
                editText.setFocusable(true);
                ret = false;
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(text).matches())
            {
                editText.setError(context.getString(R.string.not_valid_email));
                editText.setFocusable(true);
                ret = false;
            }
        }

        return ret;
    }

}
