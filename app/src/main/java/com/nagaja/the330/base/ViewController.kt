package com.nagaja.the330.base

import android.content.Context
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.nagaja.the330.R

class ViewController(
    var flContainer: Int,
    var fragmentManager: FragmentManager?,
    var context: Context
) {
    var currentFragment: Fragment? = null

    fun pushFragment(
        screenId: String,
        fragment: Fragment,
        underTopAnimation: Boolean? = true
    ) {
        if (underTopAnimation == true) {
            try {
                currentFragment?.let {
                    val animationUnderTopExit = AnimationUtils.loadAnimation(context, R.anim.exit)
                    it.requireView().animation = animationUnderTopExit
                }
            } catch (e: Exception) {

            }
        }
        fragmentManager?.beginTransaction()
            ?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            ?.add(flContainer, fragment, screenId)
            ?.addToBackStack(screenId)
            ?.commitAllowingStateLoss()
        currentFragment = fragment
    }

    fun pushFragmentCustomAnimation(
        screenId: String,
        fragment: Fragment,
        animPush: Int,
        animPop: Int
    ) {
        fragmentManager?.beginTransaction()
            ?.setCustomAnimations(animPush, R.anim.exit, R.anim.pop_enter, animPop)
            ?.add(flContainer, fragment, screenId)
            ?.addToBackStack(screenId)
            ?.commitAllowingStateLoss()
        currentFragment = fragment
    }

    fun pushFragmentNoAnimation(
        screenId: String,
        fragment: Fragment,
    ) {
        fragmentManager?.beginTransaction()
            ?.add(flContainer, fragment, screenId)
            ?.addToBackStack(screenId)
            ?.commitAllowingStateLoss()
        currentFragment = fragment
    }

//    fun replaceByFragment(
//        screenId: String,
//        fragment: Fragment,
//    ) {
//        fragmentManager?.beginTransaction()
////            ?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_exit, R.anim.pop_enter)
//            ?.replace(flContainer, fragment)
//            ?.addToBackStack(screenId)
//            ?.commitAllowingStateLoss()
//        currentFragment = fragment
//    }

    fun popFragment(underTopAnimation: Boolean? = true) {
        val backstackEntryCount = fragmentManager?.backStackEntryCount?:0
        var underTopFragment: Fragment? = null
        if (backstackEntryCount >= 2) {
            underTopFragment = fragmentManager?.fragments?.get(backstackEntryCount - 2)
            try {
                if (underTopAnimation == true) {
                    val animationUnderTopComeback =
                        AnimationUtils.loadAnimation(currentFragment?.context, R.anim.pop_enter)
                    underTopFragment?.view?.animation = animationUnderTopComeback
                }
            } catch (e: Exception) {

            }
        }
        fragmentManager?.popBackStack()
        currentFragment = underTopFragment
//        currentFragment = if (fragmentManager?.backStackEntryCount!! > 1) {
//            val fragmentTag =
//                fragmentManager?.getBackStackEntryAt(fragmentManager?.backStackEntryCount!! - 2)?.name
//            fragmentManager?.findFragmentByTag(fragmentTag)
//        } else {
//            null
//        }
    }

    fun popAllFragment() {
        fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        currentFragment = null
    }

    fun popNFragment(n: Int) {
        for (i in 0 until n) {
            fragmentManager?.popBackStack()
        }
        if (fragmentManager?.backStackEntryCount!! > 0) {
            val fragmentTag =
                fragmentManager?.getBackStackEntryAt(fragmentManager?.backStackEntryCount!! - 1)?.name
            currentFragment = fragmentManager?.findFragmentByTag(fragmentTag)
        }
    }

    fun popToFragment(tag: String) {
        fragmentManager?.popBackStackImmediate(tag, 0)
        currentFragment = fragmentManager?.findFragmentByTag(tag)
    }
}