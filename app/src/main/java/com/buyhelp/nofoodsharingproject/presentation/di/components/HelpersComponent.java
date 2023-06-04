/**
 * Класс {@code HelpersComponent} позволяет передавать класс DefineUser, пробрасывая в него контекст через AppModule
 * Субкомпонент у AppComponent
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.di.components;

import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.PermissionHandler;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.AppModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.DefineUserModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.PermissionHandlerModule;
import com.buyhelp.nofoodsharingproject.presentation.di.scopes.HelpersScope;

import dagger.Subcomponent;

@HelpersScope
@Subcomponent(modules = { DefineUserModule.class, AppModule.class })
public interface HelpersComponent {

    @Subcomponent.Builder
    interface Builder {
        Builder appModule(AppModule appModule);
        Builder defineUserModule(DefineUserModule defineUserModule);

        HelpersComponent create();
    }

    DefineUser getDefineUser();
}
