package myahkota.homedelivery.com.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

public class ParcelableParseObject implements Parcelable {

    private String category;
    private String city;
    private String place;
    private String work;
    private String delivery;
    private String menu;
    private String phone;

    public ParcelableParseObject(ParseObject parseObject, String _category) {
        category = _category;
        city = parseObject.getString(Const.P_COLUMN_CITY);
        place = parseObject.getString(Const.P_COLUMN_TITLE);
        work = parseObject.getString(Const.P_COLUMN_WORK);
        delivery = parseObject.getString(Const.P_COLUMN_DELIVERY);
        menu = parseObject.getString(Const.P_COLUMN_MENU);
        phone = parseObject.getString(Const.P_COLUMN_PHONE);

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
        dest.writeString(this.city);
        dest.writeString(this.place);
        dest.writeString(this.work);
        dest.writeString(this.delivery);
        dest.writeString(this.menu);
        dest.writeString(this.phone);
    }

    protected ParcelableParseObject(Parcel in) {
        this.category = in.readString();
        this.city = in.readString();
        this.place = in.readString();
        this.work = in.readString();
        this.delivery = in.readString();
        this.menu = in.readString();
        this.phone = in.readString();
    }

    public static final Parcelable.Creator<ParcelableParseObject> CREATOR = new Parcelable.Creator<ParcelableParseObject>() {
        @Override
        public ParcelableParseObject createFromParcel(Parcel source) {
            return new ParcelableParseObject(source);
        }

        @Override
        public ParcelableParseObject[] newArray(int size) {
            return new ParcelableParseObject[size];
        }
    };
}
