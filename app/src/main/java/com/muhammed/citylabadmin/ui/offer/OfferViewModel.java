package com.muhammed.citylabadmin.ui.offer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.muhammed.citylabadmin.data.model.AllOffer;
import com.muhammed.citylabadmin.data.model.general.SimpleResponse;
import com.muhammed.citylabadmin.helper.NetworkState;
import com.muhammed.citylabadmin.service.RetrofitService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class OfferViewModel extends ViewModel {

    private final RetrofitService retrofitService;

    @Inject
    public OfferViewModel(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<NetworkState> _addOfferLiveData = new MutableLiveData<NetworkState>();
    public LiveData<NetworkState> addOfferLiveData = _addOfferLiveData;

    private final MutableLiveData<NetworkState> _getOfferLiveData = new MutableLiveData<NetworkState>();
    public LiveData<NetworkState> getOfferLiveData = _getOfferLiveData;


    public void addOffer(String image, String title, String desc, String startDate, String  endDate,
                         Double oldPrice, Double newPrice) {

        retrofitService.uploadOffer(image, title, desc, startDate, endDate, oldPrice, newPrice)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SimpleResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        _addOfferLiveData.postValue(NetworkState.LOADING);
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull SimpleResponse offerResponse) {

                        if (!offerResponse.isStatus())
                            _addOfferLiveData.postValue(NetworkState.getErrorMessage(offerResponse.getMessage()));
                        else
                            _addOfferLiveData.postValue(NetworkState.getLoaded(offerResponse.getMessage()));

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        _addOfferLiveData.postValue(NetworkState.getErrorMessage(e));


                    }
                });


    }

    MutableLiveData<AllOffer> resultmutbel=new MediatorLiveData<>();

    public  MutableLiveData<AllOffer>  getallpffer()
    {
        retrofitService.offers().enqueue(new Callback<AllOffer>() {
            @Override
            public void onResponse(Call<AllOffer> call, Response<AllOffer> response) {
                resultmutbel.postValue(response.body());
            }

            @Override
            public void onFailure(Call<AllOffer> call, Throwable t) {

            }
        });
return resultmutbel;

    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
