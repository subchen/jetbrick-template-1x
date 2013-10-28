package testcase.model;

import java.util.*;

public class SimpleModel {
    private boolean booleanVal;
    private byte byteVal;
    private short shortVal;
    private char charVal;
    private int intVal;
    private long longVal;
    private float floatVal;
    private double doubleVal;
    private Boolean booleanObj;
    private Byte byteObj;
    private Short shortObj;
    private Character charObj;
    private Integer intObj;
    private Long longObj;
    private Float floatObj;
    private Double doubleObj;
    private String str;
    private Date date;
    private int[] intArray;
    private String[] strArray;
    private List<String> strList;
    private List<SimpleModel> list;
    private Map<String, SimpleModel> map;
    private Map<String, List<SimpleModel>> listmap;
    private List<Map<String, SimpleModel>> maplist;
    private SimpleModel me;
    private SimpleModel[] ours;

    @SuppressWarnings("unchecked")
    public static SimpleModel newInstance() {
        SimpleModel s = new SimpleModel();
        s.booleanVal = true;
        s.byteVal = 1;
        s.shortVal = 121;
        s.charVal = 'a';
        s.intVal = 1000;
        s.longVal = 10000000L;
        s.floatVal = 9.9F;
        s.doubleVal = 999.99D;
        s.booleanObj = true;
        s.byteObj = 1;
        s.shortObj = 121;
        s.charObj = 'a';
        s.intObj = 1000;
        s.longObj = 10000000L;
        s.floatObj = 9.9F;
        s.doubleObj = 999.99D;
        s.str = "Hello";
        s.date = new Date();
        s.intArray = new int[] { 1, 2, 3, 4, 5 };
        s.strArray = new String[] { "aaa", "bbb", "ccc" };
        s.strList = Arrays.asList("Mon", "Tue", "Wed");
        s.list = Arrays.asList(s);
        s.map = new HashMap();
        s.listmap = new HashMap();
        s.maplist = Arrays.asList(s.map);
        s.me = s;
        s.ours = new SimpleModel[] { s, s };
        s.map.put("apple", s);
        s.map.put("google", s);
        s.listmap.put("one", s.list);
        s.listmap.put("two", s.list);
        return s;
    }

    public boolean isBooleanVal() {
        return booleanVal;
    }

    public void setBooleanVal(boolean booleanVal) {
        this.booleanVal = booleanVal;
    }

    public byte getByteVal() {
        return byteVal;
    }

    public void setByteVal(byte byteVal) {
        this.byteVal = byteVal;
    }

    public short getShortVal() {
        return shortVal;
    }

    public void setShortVal(short shortVal) {
        this.shortVal = shortVal;
    }

    public char getCharVal() {
        return charVal;
    }

    public void setCharVal(char charVal) {
        this.charVal = charVal;
    }

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    public long getLongVal() {
        return longVal;
    }

    public void setLongVal(long longVal) {
        this.longVal = longVal;
    }

    public float getFloatVal() {
        return floatVal;
    }

    public void setFloatVal(float floatVal) {
        this.floatVal = floatVal;
    }

    public double getDoubleVal() {
        return doubleVal;
    }

    public void setDoubleVal(double doubleVal) {
        this.doubleVal = doubleVal;
    }

    public Boolean getBooleanObj() {
        return booleanObj;
    }

    public void setBooleanObj(Boolean booleanObj) {
        this.booleanObj = booleanObj;
    }

    public Byte getByteObj() {
        return byteObj;
    }

    public void setByteObj(Byte byteObj) {
        this.byteObj = byteObj;
    }

    public Short getShortObj() {
        return shortObj;
    }

    public void setShortObj(Short shortObj) {
        this.shortObj = shortObj;
    }

    public Character getCharObj() {
        return charObj;
    }

    public void setCharObj(Character charObj) {
        this.charObj = charObj;
    }

    public Integer getIntObj() {
        return intObj;
    }

    public void setIntObj(Integer intObj) {
        this.intObj = intObj;
    }

    public Long getLongObj() {
        return longObj;
    }

    public void setLongObj(Long longObj) {
        this.longObj = longObj;
    }

    public Float getFloatObj() {
        return floatObj;
    }

    public void setFloatObj(Float floatObj) {
        this.floatObj = floatObj;
    }

    public Double getDoubleObj() {
        return doubleObj;
    }

    public void setDoubleObj(Double doubleObj) {
        this.doubleObj = doubleObj;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int[] getIntArray() {
        return intArray;
    }

    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }

    public String[] getStrArray() {
        return strArray;
    }

    public void setStrArray(String[] strArray) {
        this.strArray = strArray;
    }

    public List<String> getStrList() {
        return strList;
    }

    public void setStrList(List<String> strList) {
        this.strList = strList;
    }

    public Map<String, SimpleModel> getMap() {
        return map;
    }

    public void setMap(Map<String, SimpleModel> map) {
        this.map = map;
    }

    public Map<String, List<SimpleModel>> getListmap() {
        return listmap;
    }

    public void setListmap(Map<String, List<SimpleModel>> listmap) {
        this.listmap = listmap;
    }

    public List<Map<String, SimpleModel>> getMaplist() {
        return maplist;
    }

    public void setMaplist(List<Map<String, SimpleModel>> maplist) {
        this.maplist = maplist;
    }

    public SimpleModel getMe() {
        return me;
    }

    public void setMe(SimpleModel me) {
        this.me = me;
    }

    public SimpleModel[] getOurs() {
        return ours;
    }

    public void setOurs(SimpleModel[] ours) {
        this.ours = ours;
    }
}
