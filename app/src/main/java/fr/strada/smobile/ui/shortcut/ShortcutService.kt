package fr.strada.smobile.ui.shortcut

import android.app.Service
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import dagger.android.AndroidInjection
import fr.strada.smobile.R
import fr.strada.smobile.data.models.access_functionalities.Functionality
import fr.strada.smobile.data.repositories.UserRepository
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.pointeuse.OpenPointeuseActivity
import fr.strada.smobile.utils.OPEN_MES_ACTIVITES
import fr.strada.smobile.utils.OPEN_POINTEUSE
import fr.strada.smobile.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShortcutService : Service() {

    @Inject
    internal lateinit var userRepository : UserRepository

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        GlobalScope.launch {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val isUserLoggedIn = userRepository.isUserLoggedIn()
                val shortcutManager = getSystemService(ShortcutManager::class.java)
                if(isUserLoggedIn) {
                    val shortcuts = ArrayList<ShortcutInfo>()
                    val res = userRepository.getUserFunctionalities()
                    if(res.status == Status.SUCCESS){
                        val userFunctionalities = res.data!!
                        userFunctionalities.forEach {
                            when(it.code){
                                Functionality.POINTEUSE.code -> {
                                    val intent = Intent(this@ShortcutService, OpenPointeuseActivity::class.java)
                                    intent.action = OPEN_POINTEUSE
                                    val builder = ShortcutInfo.Builder(this@ShortcutService, "pointeuse")
                                        .setShortLabel(this@ShortcutService.getString(R.string.pointeuse))
                                        .setIcon(Icon.createWithResource(this@ShortcutService, R.drawable.ic_time_clock_menu))
                                        .setIntent(intent)
                                    val pointeuseShortcut = builder.build()
                                    shortcuts.add(pointeuseShortcut)
                                }
                                Functionality.MES_ACTIVITES.code -> {
                                    val intent = Intent(this@ShortcutService, MainActivity::class.java)
                                    intent.action = OPEN_MES_ACTIVITES
                                    val builder = ShortcutInfo.Builder(this@ShortcutService, "activitie")
                                        .setShortLabel(this@ShortcutService.getString(R.string.mes_activites))
                                        .setIcon(Icon.createWithResource(this@ShortcutService, R.drawable.ic_mes_activites))
                                        .setIntent(intent)
                                    val mesActivititesShortcut = builder.build()
                                    shortcuts.add(mesActivititesShortcut)
                                }
                            }
                        }
                        shortcutManager.addDynamicShortcuts(shortcuts)
                    }
                }else {
                    shortcutManager.removeAllDynamicShortcuts()
                }
                stopSelf()
            }else{
                stopSelf()
            }
        }
        return START_STICKY
    }
}