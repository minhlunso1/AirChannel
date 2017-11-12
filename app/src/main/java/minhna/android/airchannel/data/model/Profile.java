package minhna.android.airchannel.data.model;

/**
 * Created by Minh on 11/12/2017.
 */

public class Profile {
    private String email;
    private @SortType int sortType;//default constructor is sortID

    public Profile() {
    }

    public Profile(String email, int sortType) {
        this.email = email;
        this.sortType = sortType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public @SortType int getSortType() {
        return sortType;
    }

    public void setSortType(@SortType int sortType) {
        this.sortType = sortType;
    }
}
