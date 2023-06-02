package com.buyhelp.nofoodsharingproject.presentation.di.components;

import android.app.Application;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.data.api.getter.GetterRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.data.api.setter.SetterRepository;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.activities.GetterActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainAuthActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.SetterActivity;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ActivityModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.AppModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ChatListModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ChatModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.MapModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.NotifyModule;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;

@Component(modules = { AppModule.class, RetrofitService.class, DefineUser.class,
        ActivityModule.class, MapModule.class, NotifyModule.class, ChatListModule.class, ChatModule.class })
public interface AppComponent extends AndroidInjector<ApplicationCore> {
    MainActivity inject(MainActivity mainActivity);
    MainAuthActivity inject(MainAuthActivity mainAuthActivity);
    GetterActivity inject(GetterActivity getterActivity);
    SetterActivity inject(SetterActivity setterActivity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder appModule(AppModule appModule);
        Builder defineUser(DefineUser defineUser);

        Builder retrofitModule(RetrofitService retrofitService);

        AppComponent create();
    }

    DefineUser getDefineUser();

    AuthRepository getAuthRepository();
    MapRepository getMapRepository();
    GetterRepository getGetterRepository();
    SetterRepository getSetterRepository();
    AdvertsRepository getAdvertsRepository();
    NotificationRepository getNotificationRepository();

}
