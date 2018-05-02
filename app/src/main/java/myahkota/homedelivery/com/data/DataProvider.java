package myahkota.homedelivery.com.data;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class DataProvider {

    public DataProvider() {

    }
    //region online data
    public void getCitiesOn(FindCallback<ParseObject> findCallback) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Const.CITY_KEY);
        query.findInBackground(findCallback);
    }

    public void getCategoriesOn(FindCallback<ParseObject> findCallback) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Const.CATEGORY_KEY);
        query.findInBackground(findCallback);
    }

    public void getPlacesOn(FindCallback<ParseObject> findCallback) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Const.PLACE_KEY);
        query.findInBackground(findCallback);
    }
    //endregion

    //region offline data
    public void getCitiesOff(FindCallback<ParseObject> findCallback) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Const.CITY_KEY);
        query.fromLocalDatastore();
        query.findInBackground(findCallback);
    }

    public void getCategoriesOff(FindCallback<ParseObject> findCallback) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Const.CATEGORY_KEY);
        query.orderByAscending(Const.P_COLUMN_ORDER);
        query.fromLocalDatastore();
        query.findInBackground(findCallback);
    }

    public void getPlacesOff(FindCallback<ParseObject> findCallback, String city, String category) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Const.PLACE_KEY);
        query.whereEqualTo(Const.P_COLUMN_CITY, city);
//        query.whereContains(Const.P_COLUMN_CATEGORY, category);
        query.fromLocalDatastore();
        query.findInBackground(findCallback);
    }
    //endregion

    public void getSearchPlaces(FindCallback<ParseObject> findCallback, String city, String word) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Const.PLACE_KEY);
        query.whereEqualTo(Const.P_COLUMN_CITY, city);
        query.whereContains(Const.P_COLUMN_MENU, word);
        query.fromLocalDatastore();
        query.findInBackground(findCallback);
    }

    public  void clearParse() {
         ParseObject.unpinAllInBackground();
    }

}
