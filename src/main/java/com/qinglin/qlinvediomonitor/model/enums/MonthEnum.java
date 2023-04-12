package com.qinglin.qlinvediomonitor.model.enums;

/**
 * 月份的枚举
 * @author tlm1051051
 * @version $Id: MonthEnum.java, v 0.1 2019年02月11日 15:51 tlm1051051 Exp $
 */
public enum MonthEnum {

    JAN(1, "JAN"),
    FEB(2, "FEB"),
    MAR(3, "MAR"),
    APR(4, "APR"),
    MAY(5, "MAY"),
    JUN(6, "JUN"),
    JUL(7, "JUL"),
    AUG(8, "AUG"),
    SEP(9, "SEP"),
    OCT(10, "OCT"),
    NOV(11, "NOV"),
    DEC(12, "DEC")
    ;

    private Integer code;
    private String desc;

    MonthEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MonthEnum getEnumByCode(Integer code){
        if (null == code){
            return null;
        }
        for(MonthEnum monthEnum : values()){
            if(monthEnum.getCode().equals(code)){
                return monthEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
