package net.ddns.weissenterprises.mview;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CFData {
    private float val;

    private long currentTimeMillis;

    private String dateS;

    private int category;


    public CFData(float val, long currentTimeMillis, int category){
        this.val = val;
        this.currentTimeMillis = currentTimeMillis;
        setDateS(currentTimeMillis);

        this.category = category;
    }
    public CFData(float val){
        this.val = val;
        this.currentTimeMillis = System.currentTimeMillis();
        setDateS(this.currentTimeMillis);

        this.category = 0;
    }

    private void setDateS(long currentTimeMillis){
        this.dateS = (new SimpleDateFormat("MM/dd/YYYY")).format(new Date(currentTimeMillis));
    }

    public String getDateS() {

        return dateS;
    }

    public float getVal() {
        return val;
    }

    public long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public int getCategory() {
        return category;
    }
}
