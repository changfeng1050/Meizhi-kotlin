package me.zjl.meizhi.ui.base

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import me.zjl.meizhi.MeizhiFactory
import me.zjl.meizhi.R
import me.zjl.meizhi.ui.AboutActivity
import me.zjl.meizhi.util.L
import me.zjl.meizhi.util.Once
import org.jetbrains.anko.browse
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by chang on 2017-08-14.
 */
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        val TAG: String = L.makeLogTag(BaseActivity::class.java)

        val sGankIo = MeizhiFactory.gank
    }

    private val compositeSubscription by lazy {
        CompositeSubscription()
    }

    public fun addSubscription(s: Subscription) {
        compositeSubscription.add(s)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId ?: return super.onOptionsItemSelected(item)
        when (id) {
            R.id.action_about -> {
                startActivity(intentFor<AboutActivity>())
                return true
            }
            R.id.action_login -> {
                loginGitHub()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loginGitHub() {
        Once(this).show(R.string.action_github_login) {
            toast(R.string.tip_login_github)
        }
        val url = getString(R.string.url_login_github)
        browse(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription.unsubscribe()
    }


}