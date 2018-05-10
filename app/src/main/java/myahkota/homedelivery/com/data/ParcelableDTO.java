package myahkota.homedelivery.com.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

public class ParcelableDTO implements Parcelable {

    private String category;
    private String city;
    private String place;
    private String work;
    private String delivery;
    private String menu;
    private String phone;
    private String icon;
    private String condition;

    public ParcelableDTO(ParseObject parseObject, String _category) {
        category = _category;
        city = parseObject.getString(Const.P_COLUMN_CITY);
        place = parseObject.getString(Const.P_COLUMN_TITLE);
        work = parseObject.getString(Const.P_COLUMN_WORK);
        delivery = parseObject.getString(Const.P_COLUMN_DELIVERY);
        menu = parseObject.getString(Const.P_COLUMN_MENU);
        phone = parseObject.getString(Const.P_COLUMN_PHONE);
        icon = parseObject.getParseFile(Const.P_COLUMN_IMAGE).getUrl();
        condition = parseObject.getString(Const.P_COLUMN_CONDITION);
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
        dest.writeString(this.icon);
        dest.writeString(this.condition);
    }

    protected ParcelableDTO(Parcel in) {
        this.category = in.readString();
        this.city = in.readString();
        this.place = in.readString();
        this.work = in.readString();
        this.delivery = in.readString();
        this.menu = in.readString();
        this.phone = in.readString();
        this.icon = in.readString();
        this.condition = in.readString();
    }

    public static final Creator<ParcelableDTO> CREATOR = new Creator<ParcelableDTO>() {
        @Override
        public ParcelableDTO createFromParcel(Parcel source) {
            return new ParcelableDTO(source);
        }

        @Override
        public ParcelableDTO[] newArray(int size) {
            return new ParcelableDTO[size];
        }
    };
}
