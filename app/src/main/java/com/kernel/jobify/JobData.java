package com.kernel.jobify;

import java.io.Serializable;

public class JobData implements Serializable
{

    public String jobTitle;
    public String jobDisc;
    public String jobLink;
    public String jobKey;
    public String jobCat;

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public String shareLink;

    public Boolean getBookMarked() {
        return isBookMarked;
    }

    public void setBookMarked(Boolean bookMarked) {
        isBookMarked = bookMarked;
    }

    public Boolean isBookMarked;

    public String getJobKey() {
        return jobKey;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
    }

    public String getJobCat() {
        return jobCat;
    }

    public void setJobCat(String jobCat) {
        this.jobCat = jobCat;
    }

    public String getJobPhotoLink() {
        return jobPhotoLink;
    }

    public void setJobPhotoLink(String jobPhotoLink) {
        this.jobPhotoLink = jobPhotoLink;
    }

    public String jobPhotoLink;

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDisc() {
        return jobDisc;
    }

    public void setJobDisc(String jobDisc) {
        this.jobDisc = jobDisc;
    }

    public String getJobLink() {
        return jobLink;
    }

    public void setJobLink(String jobLink) {
        this.jobLink = jobLink;
    }

}
