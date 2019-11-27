package com.kernel.jobify;

import java.io.Serializable;

public class JobData implements Serializable
{

    public String jobTitle;
    public String jobDisc;
    public String jobLink;

    public String getJobApplyLink() {
        return jobApplyLink;
    }

    public void setJobApplyLink(String jobApplyLink) {
        this.jobApplyLink = jobApplyLink;
    }

    public String jobApplyLink;

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
