package tech.skot.view

import android.Manifest
import tech.skot.core.view.SKPermission

data class SKPermissionAndroid(val name: String) : SKPermission

class SKPermissionsRequestResultAndroid(
    val requestCode:Int,
    val grantedPermissions: List<SKPermissionAndroid>,
)

val  notificationsSkPermission = SKPermissionAndroid(Manifest.permission.POST_NOTIFICATIONS)