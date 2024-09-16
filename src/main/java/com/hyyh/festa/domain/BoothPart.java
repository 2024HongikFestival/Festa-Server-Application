package com.hyyh.festa.domain;

public enum BoothPart {
    ARCH("architecture"),
    BE("businessEconomic"),
    ENG("engineering"),
    CONV("convergence"),
    LIAR("liberalArts"),
    ART("art"),
    EDU("education"),
    ATN("autonomous"),
    CSC("clubScholarship"),
    CSP("clubSports"),
    CP("clubPerformance"),
    CEL("clubExhibitionLeisure"),
    CS("clueSociety"),
    ;

    String value;

    BoothPart(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
