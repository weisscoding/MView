package net.ddns.weissenterprises.mview;

import android.content.Context;

import java.util.Date;

public class DCCFA{
    DCCFA(Context context, Date date, MainActivity.CashFlowAdapter cfa){
        this.date = date;
        this.context = context;
        this.cfa = cfa;
    }
    public Date date;
    public Context context;
    public MainActivity.CashFlowAdapter cfa;

}
