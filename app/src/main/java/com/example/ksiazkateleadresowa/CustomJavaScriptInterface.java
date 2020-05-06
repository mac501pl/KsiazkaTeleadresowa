package com.example.ksiazkateleadresowa;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.webkit.JavascriptInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomJavaScriptInterface {

    private Context context;

    CustomJavaScriptInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public String getContacts() throws JSONException {
        JSONArray contacts = new JSONArray();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        assert cursor != null;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                JSONObject contactInfo = new JSONObject();

                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                String firstName = name.substring(0, name.indexOf(" "));
                String lastName = name.substring(name.indexOf(" ") + 1);

                contactInfo.put("firstName", firstName);
                contactInfo.put("lastName", lastName);

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor phone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                assert phone != null;
                while (phone.moveToNext()) {
                    contactInfo.put("phoneNumber", phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[()\\s-]+", ""));
                    break;
                }
                phone.close();

                Cursor emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                assert emails != null;
                while (emails.moveToNext()) {
                    contactInfo.put("email", emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
                    break;
                }
                emails.close();

                Cursor notes = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = ?", new String[]{id}, null);
                assert notes != null;
                if (notes.moveToFirst()) {
                    contactInfo.put("notes", notes.getString(notes.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)));
                }
                notes.close();

                Cursor organizations = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = ?", new String[]{id}, null);
                assert organizations != null;
                if (organizations.moveToFirst()) {
                    contactInfo.put("company", organizations.getString(organizations.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)));
                    contactInfo.put("title", organizations.getString(organizations.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)));
                }
                organizations.close();

                contacts.put(contactInfo);
            }
        }
        cursor.close();
        return contacts.toString();
    }
}

