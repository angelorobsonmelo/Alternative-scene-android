package br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview

import android.content.Context
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.Module
import dagger.Provides
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import javax.inject.Named
import javax.inject.Singleton

@Module
class RecyclerViewAnimatedWithDividerModule(
    private val recyclerView: RecyclerView,
    private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
) {

    @Provides
    @Singleton
    fun provideLayoutManager(@Named("ApplicationContext") context: Context) =
        LinearLayoutManager(context)

    @Provides
    @Singleton
    fun provideScaleInAnimationAdapter(): ScaleInAnimationAdapter {
        return ScaleInAnimationAdapter(adapter).apply {
            setFirstOnly(false)
            setDuration(500)
            setInterpolator(OvershootInterpolator(.5f))
        }
    }

    @Provides
    @Singleton
    fun provideRecyclerView(
        layoutManager: LinearLayoutManager,
        scaleInAnimationAdapter: ScaleInAnimationAdapter
    ): RecyclerView {
        val divider = DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation
        )

        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(divider)
        recyclerView.adapter = scaleInAnimationAdapter
        return recyclerView
    }


}