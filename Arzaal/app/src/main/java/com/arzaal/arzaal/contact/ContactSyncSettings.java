package com.arzaal.arzaal.contact;

/**
 * Created by henry on 4/2/16.
 */
public class ContactSyncSettings {
    private boolean facebook;
    private boolean phoneContactInfo;
    private boolean google;
    public ContactSyncSettings() {

    }

    public boolean isFacebook() {
        return facebook;
    }

    public void setFacebook(boolean facebook) {
        this.facebook = facebook;
    }

    public boolean isPhoneContactInfo() {
        return phoneContactInfo;
    }

    public void setPhoneContactInfo(boolean phoneContactInfo) {
        this.phoneContactInfo = phoneContactInfo;
    }

    public boolean isGoogle() {
        return google;
    }

    public void setGoogle(boolean google) {
        this.google = google;
    }
}
