package com.pay2all.aeps.aepsnew;

import android.content.Context;

import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class UtilsXmlToJson {
    Context mContext;
    public UtilsXmlToJson(Context context)
    {
        mContext=context;
    }

    public String mGetJson(String xmlData)
    {
        JSONObject json = new XmlToJson.Builder(xmlData).build().toJson();

        return json.toString();
    }

}
