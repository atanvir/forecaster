package com.forecaster.Utility;

import com.forecaster.Modal.ForcasterSetupProfile;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UpdateForcasterBody {

    private MediaType mediaType = MediaType.parse("text/plain");

    private Map<String, RequestBody> requestBodyMap = new HashMap<>();


    public UpdateForcasterBody(ForcasterSetupProfile profile)
    {

        requestBodyMap.put("forecasterId",RequestBody.create(mediaType,profile.getForecasterId()));
        requestBodyMap.put("gender",RequestBody.create(mediaType,profile.getGender()));
        requestBodyMap.put("name",RequestBody.create(mediaType,profile.getName()));
        requestBodyMap.put("email",RequestBody.create(mediaType,profile.getEmail()));
        requestBodyMap.put("dob",RequestBody.create(mediaType,profile.getDob()));
        requestBodyMap.put("categoryName",RequestBody.create(mediaType,profile.getCategoryName()));
        requestBodyMap.put("aboutUs",RequestBody.create(mediaType,profile.getAboutUs()));
        requestBodyMap.put("pricePerQues",RequestBody.create(mediaType, String.valueOf(profile.getPricePerQues())));
        requestBodyMap.put("langCode",RequestBody.create(mediaType, profile.getLangCode()));

//        requestBodyMap.put("bankName",RequestBody.create(mediaType,profile.getBankName()));
//        requestBodyMap.put("accountHolderName",RequestBody.create(mediaType,profile.getAccountHolderName()));
//        requestBodyMap.put("accountNumber",RequestBody.create(mediaType,profile.getAccountNumber()));


    }

    public Map<String,RequestBody> getBody()
    {
        return requestBodyMap;
    }

}
