/**
 * Класс {@code PermissionComponent} позволяет передавать класс PermissionHandler
 * В планах улучшить переброс активности в модуль PermissionHandlerModule
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.di.components;

import com.buyhelp.nofoodsharingproject.domain.helpers.PermissionHandler;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ActivityModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.PermissionHandlerModule;
import com.buyhelp.nofoodsharingproject.presentation.di.scopes.PermissionsScope;

import dagger.Component;
import dagger.android.AndroidInjector;

@PermissionsScope
@Component(modules = {ActivityModule.class, PermissionHandlerModule.class })
public interface PermissionComponent extends AndroidInjector<ApplicationCore> {
    @Component.Builder
    interface Builder {
        Builder defineActivity(ActivityModule activityModule);

        Builder definePermissions(PermissionHandlerModule permissionHandlerModule);

        PermissionComponent build();
    }

    PermissionHandler getPermissionHandler();

}
