/**
 * Класс {@code AppComponent} позволяет передавать класс Retrofit, пробрасывая в него контекст через AppModule
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.di.components;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.data.api.giver.GiverRepository;
import com.buyhelp.nofoodsharingproject.data.api.needy.NeedyRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.presentation.activities.GiverActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.NeedyActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainAuthActivity;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ActivityListModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.AppModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ChatListModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ChatModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.MapModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.NotifyModule;
import com.buyhelp.nofoodsharingproject.presentation.di.scopes.AppScope;

import javax.inject.Singleton;

import dagger.Component;


@AppScope
@Component(modules = { AppModule.class, RetrofitService.class,
        ActivityListModule.class, MapModule.class, NotifyModule.class, ChatListModule.class, ChatModule.class })
@Singleton
public interface AppComponent {
    MainActivity inject(MainActivity mainActivity);
    MainAuthActivity inject(MainAuthActivity mainAuthActivity);
    NeedyActivity inject(NeedyActivity needyActivity);
    GiverActivity inject(GiverActivity giverActivity);

    @Component.Builder
    interface Builder {
        Builder appModule(AppModule appModule);

        Builder retrofitModule(RetrofitService retrofitService);

        AppComponent create();
    }

    HelpersComponent.Builder helpersComponentBuilder();

    AuthRepository getAuthRepository();
    MapRepository getMapRepository();
    NeedyRepository getNeedyRepository();
    GiverRepository getGiverRepository();
    AdvertsRepository getAdvertsRepository();
    NotificationRepository getNotificationRepository();

}
