package com.buyhelp.nofoodsharingproject.presentation.di.components;

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
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ActivityModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.AppModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ChatListModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ChatModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.MapModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.NotifyModule;
import com.buyhelp.nofoodsharingproject.presentation.di.scopes.AppScope;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;


@AppScope
@Component(modules = { AppModule.class, RetrofitService.class,
        ActivityModule.class, MapModule.class, NotifyModule.class, ChatListModule.class, ChatModule.class })
@Singleton
public interface AppComponent {
    MainActivity inject(MainActivity mainActivity);
    MainAuthActivity inject(MainAuthActivity mainAuthActivity);
    GetterActivity inject(GetterActivity getterActivity);
    SetterActivity inject(SetterActivity setterActivity);

    @Component.Builder
    interface Builder {
        Builder appModule(AppModule appModule);

        Builder retrofitModule(RetrofitService retrofitService);

        AppComponent create();
    }

    HelpersComponent.Builder helpersComponentBuilder();

    AuthRepository getAuthRepository();
    MapRepository getMapRepository();
    GetterRepository getGetterRepository();
    SetterRepository getSetterRepository();
    AdvertsRepository getAdvertsRepository();
    NotificationRepository getNotificationRepository();

}
