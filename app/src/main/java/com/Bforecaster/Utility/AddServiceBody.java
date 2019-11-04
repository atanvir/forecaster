package com.Bforecaster.Utility;

import com.Bforecaster.Modal.ForcasterSetupProfile;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddServiceBody
{
    private MediaType mediaType = MediaType.parse("text/plain");

    private Map<String, RequestBody> requestBodyMap = new HashMap<>();


    public AddServiceBody(ForcasterSetupProfile profile)
    {

        requestBodyMap.put("forecasterId",RequestBody.create(mediaType,profile.getForecasterId()));
        requestBodyMap.put("gender",RequestBody.create(mediaType,profile.getGender()));
        requestBodyMap.put("dob",RequestBody.create(mediaType,profile.getDob()));
        requestBodyMap.put("categoryName",RequestBody.create(mediaType,profile.getCategoryName()));
        requestBodyMap.put("aboutUs",RequestBody.create(mediaType,profile.getAboutUs()));
        requestBodyMap.put("pricePerQues",RequestBody.create(mediaType, String.valueOf(profile.getPricePerQues())));
        requestBodyMap.put("bankName",RequestBody.create(mediaType,profile.getBankName()));
        requestBodyMap.put("accountHolderName",RequestBody.create(mediaType,profile.getAccountHolderName()));
        requestBodyMap.put("accountNumber",RequestBody.create(mediaType,profile.getAccountNumber()));
        requestBodyMap.put("documentType",RequestBody.create(mediaType,profile.getDocumentType()));
        requestBodyMap.put("langCode",RequestBody.create(mediaType,profile.getLangCode()));
       // requestBodyMap.put("deviceType",RequestBody.create(mediaType,profile.getDeviceType()));
       // requestBodyMap.put("deviceToken",RequestBody.create(mediaType,profile.getDeviceToken()));



    }

    public Map<String,RequestBody> getBody()
    {
        return requestBodyMap;
    }
}
