package com.buyhelp.nofoodsharingproject.presentation.di.components;

import android.app.Application;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.data.api.getter.GetterRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.data.api.setter.SetterRepository;
import com.buyhelp.nofoodsharingproject.presentation.activities.GetterActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainAuthActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.SetterActivity;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.AppModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.MapModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.NotifyModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.getter.GetterAdvrsModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.getter.GetterAuthModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.getter.GetterNewAdvertModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.getter.GetterProfileModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.setter.SetterAdvertModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.setter.SetterAdvrsModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.setter.SetterAuthLoginModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.setter.SetterAuthModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.setter.SetterProfileModule;


import dagger.BindsInstance;
import dagger.Component;

@Component(modules = { AppModule.class, RetrofitService.class,
        GetterAuthModule.class, SetterAuthLoginModule.class, SetterAuthModule.class,
        GetterAdvrsModule.class, GetterNewAdvertModule.class, GetterProfileModule.class,
        MapModule.class, NotifyModule.class, SetterAdvrsModule.class, SetterAdvertModule.class,
        SetterProfileModule.class })
public interface AppComponent {
    MainActivity inject(MainActivity mainActivity);
    MainAuthActivity inject(MainAuthActivity mainAuthActivity);
    GetterActivity inject(GetterActivity getterActivity);
    SetterActivity inject(SetterActivity setterActivity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder appModule(AppModule appModule);

        Builder retrofitModule(RetrofitService retrofitService);

        AppComponent create();
    }

    AuthRepository getAuthRepository();
    MapRepository getMapRepository();
    GetterRepository getGetterRepository();
    SetterRepository getSetterRepository();
    AdvertsRepository getAdvertsRepository();
    NotificationRepository getNotificationRepository();

}
