package com.zfxf.douniu.internet;

import android.content.Context;

import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

import static com.zfxf.douniu.utils.CommonUtils.isNetworkAvailable;

/**
 * @author IMXU
 * @time 2017/5/11 16:42
 * @des ${TODO}
 * 邮箱：butterfly_xu@sina.com
 */

public class BaseInternetRequest {

    private HttpUtilsListener mHttpUtilsListener;
    private Context mContext;
    private int index;

    public BaseInternetRequest(Context context,HttpUtilsListener httpUtilsListener) {
        mHttpUtilsListener = httpUtilsListener;
        mContext = context;
    }

    public interface HttpUtilsListener{
        void onError(Call call, Exception e, int id);
        void
        onResponse(String response, int id);
    }

    public void post(String url , Boolean isPostBaseData ,Map<String, String> params){
        if(!isNetworkAvailable(mContext)){
           CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        PostFormBuilder builder = OkHttpUtils.post().url(mContext.getResources().getString(R.string.service_host_address).concat(url));
        CommonUtils.logMes("---------url----"+mContext.getResources().getString(R.string.service_host_address).concat(url));
        if(isPostBaseData){
            builder.addParams("sid", "");
            builder.addParams("index", (index++) + "");
            builder.addParams("ub_id", SpTools.getString(mContext, Constants.userId, "0"));
            builder.addParams("uo_long","");
            builder.addParams("uo_lat","");
            builder.addParams("uo_high","");
        }
        if (params != null)
        {
            for (String key : params.keySet())
            {
                builder.addParams(key, params.get(key));
            }
        }
        builder.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mHttpUtilsListener.onError(call,e,id);
                CommonUtils.dismissProgressDialog();
            }
            @Override
            public void onResponse(String response, int id) {
                mHttpUtilsListener.onResponse(response, id);
                CommonUtils.dismissProgressDialog();
            }
        });
    }
}
