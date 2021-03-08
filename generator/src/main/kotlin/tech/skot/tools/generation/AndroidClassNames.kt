package tech.skot.tools.generation

import com.squareup.kotlinpoet.ClassName
import tech.skot.tools.generation.viewlegacy.coreComponentsPackage

object AndroidClassNames {
    val skActivity = ClassName(coreComponentsPackage, "SKActivity")
    val fragment = ClassName("androidx.fragment.app", "Fragment")
    val view = ClassName("android.view", "View")
    val context = ClassName("android.content", "Context")
    val layoutInflater = ClassName("android.view", "LayoutInflater")
    val viewGroup = ClassName("android.view", "ViewGroup")

}