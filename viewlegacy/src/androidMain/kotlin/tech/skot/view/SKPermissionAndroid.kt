package tech.skot.view

import tech.skot.core.view.SKPermission

data class SKPermissionAndroid(val name: String) : SKPermission

class SKPermissionsRequestResultAndroid(
    val requestCode:Int,
    val grantedPermissions: List<SKPermissionAndroid>,
)