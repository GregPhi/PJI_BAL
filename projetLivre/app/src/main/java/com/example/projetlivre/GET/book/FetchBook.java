package com.example.projetlivre.GET.book;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class FetchBook extends AsyncTask<String, Void, String> {
    private WeakReference<TextView> mCDText;
    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mPubliText;
    private WeakReference<TextView> mDescriptText;

    public FetchBook(TextView cdText, TextView titleText, TextView publiText, TextView descriptText) {
        this.mCDText = new WeakReference<>(cdText);
        this.mTitleText = new WeakReference<>(titleText);
        this.mPubliText = new WeakReference<>(publiText);
        this.mDescriptText = new WeakReference<>(descriptText);
    }

    @Override
    protected String doInBackground(String... strings) {
        this.mCDText.get().setText(strings[0]);
        return BookNetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;
            String edit = null;
            String descipt = null;

            // Look for results in the items array, exiting
            // when both the title and author
            // are found or when all items have been checked.
            while (i < itemsArray.length() && (descipt == null && edit == null && title == null)) {
                // Get the current item information.
                JSONObject book = ((JSONArray) itemsArray).getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    title = volumeInfo.getString("title");
                    edit = volumeInfo.getString("publisher");
                    descipt = volumeInfo.getString("description");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If both are found, display the result.
            if (title != null && edit != null && descipt != null) {
                mTitleText.get().setText(title);
                mPubliText.get().setText(edit);
                mDescriptText.get().setText(descipt);
            } else {
                // If none are found, update the UI to
                // show failed results.
                mTitleText.get().setText("NO RESULTS");
                mPubliText.get().setText("NO RESULTS");
                mDescriptText.get().setText("NO RESULTS");
            }

        } catch (Exception e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            mTitleText.get().setText("NO RESULTS");
            mPubliText.get().setText("NO RESULTS");
            mDescriptText.get().setText("NO RESULTS");
        }
    }
}

