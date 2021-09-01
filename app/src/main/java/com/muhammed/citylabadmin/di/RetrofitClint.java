package com.muhammed.citylabadmin.di;

import com.muhammed.citylabadmin.data.model.AllOffer;
import com.muhammed.citylabadmin.data.model.Datum;
import com.muhammed.citylabadmin.data.model.general.SimpleResponse;
import com.muhammed.citylabadmin.service.RetrofitService;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClint {
    private static final String BASE_URL  = "http://citylab123-001-site1.htempurl.com/";
    private static RetrofitClint Instance;
    private RetrofitService apiApi;

    public RetrofitClint() {


        Retrofit retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
        apiApi=retrofit.create(RetrofitService.class);
    }

    public static RetrofitClint getInstance() {
        if(null==Instance)
        {
            Instance=new RetrofitClint();

        }
        return Instance;
    }


    public Call<SimpleResponse> delete_reservation(String token, int id)
    {
        return apiApi.delete("Bearer "+token,id);
    }
    public  Call<SimpleResponse> delete_offer(String token, int id)
    {
        return  apiApi.delete_offer("Bearer "+token,id);
    }
    public  Call<SimpleResponse>updateOffer(String token, int id,String image,String title,String desc, String sDate, String eDate ,Double oldPrice , Double newPrice )
    {
        return apiApi.updateOffer(image,title,desc,sDate,eDate,oldPrice,newPrice,"Bearer "+token,id);

    }


}
