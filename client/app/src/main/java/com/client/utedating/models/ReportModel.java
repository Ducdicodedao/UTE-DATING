package com.client.utedating.models;

public class ReportModel {
    private Boolean success;
    private Report report;
    private Boolean isExist;
    private String message;

    public ReportModel() {

    }

    public ReportModel(Boolean success, Report report, Boolean isExist, String message) {
        this.success = success;
        this.report = report;
        this.isExist = isExist;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
