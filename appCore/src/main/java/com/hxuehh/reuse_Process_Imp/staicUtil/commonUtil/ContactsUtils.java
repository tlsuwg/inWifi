package com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.utils.Tao800Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tianyanlei
 * Date: 2014 14-3-20
 * Time: 下午1:28
 * To change this template use File | Settings | File Templates
 */
public class ContactsUtils {
    public static List<String> getContactFromUri(Activity activity, Uri uri) {
        try {
            if (uri != null) {
                Cursor cursor = activity.managedQuery(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int count = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    if (count > 0) {
                        String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        return getPhoneFromContacts(activity, contactId, contactName);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.w(e);
        }
        return null;
    }

    private static List<String> getPhoneFromContacts(Activity activity, String contactId, String contactName) throws Exception {
        Cursor phones = activity.managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);
        phones.moveToFirst();
        List<String> phoneList = new ArrayList<String>();

        if (phones.getCount() > 0) {
            int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            do {
                String phoneNumber = phones.getString(index);
                if (Tao800Util.isMobilePhone(phoneNumber)) {
                    phoneList.add(phoneNumber + "(" + contactName + ")");
                }
            }
            while (phones.moveToNext());
        }

        return  phoneList;
    }
}
