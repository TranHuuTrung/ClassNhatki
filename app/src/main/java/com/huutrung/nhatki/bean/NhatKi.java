package com.huutrung.nhatki.bean;

import java.io.Serializable;

/**
 * Created by Admin on 11/20/2017.
 */

public class NhatKi implements Serializable {
    private int nhatKiId;
    private String nhatKiTitle;
    private String nhatKiContent;

    public NhatKi(){

    }

    public NhatKi(String nhatKiTitle, String nhatKiContent) {
        this.nhatKiTitle = nhatKiTitle;
        this.nhatKiContent = nhatKiContent;
    }

    public NhatKi(int nhatKiId, String nhatKiTitle, String nhatKiContent) {
        this.nhatKiId = nhatKiId;
        this.nhatKiTitle = nhatKiTitle;
        this.nhatKiContent = nhatKiContent;
    }

    public int getNhatKiId() {
        return nhatKiId;
    }

    public void setNhatKiId(int nhatKiId) {
        this.nhatKiId = nhatKiId;
    }

    public String getNhatKiTitle() {
        return nhatKiTitle;
    }

    public void setNhatKiTitle(String nhatKiTitle) {
        this.nhatKiTitle = nhatKiTitle;
    }

    public String getNhatKiContent() {
        return nhatKiContent;
    }

    public void setNhatKiContent(String nhatKiContent) {
        this.nhatKiContent = nhatKiContent;
    }

    @Override
    public String toString() {
        return this.nhatKiTitle;
    }
}
