package ir.mrahimy.conceal.ui.slide

import android.app.Application
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ir.mrahimy.conceal.base.BaseAndroidViewModel
import ir.mrahimy.conceal.util.arch.Event
import ir.mrahimy.conceal.util.ktx.loadBitmap
import java.io.File

class SlideShowViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val _imagePath = MutableLiveData<String>()

    val bitmap = _imagePath.map {
        it.loadBitmap()
    }

    fun setImagePath(path: String) {
        _imagePath.postValue(path)
    }

    private val _onShare = MutableLiveData<Event<Uri>>()
    val onShare: LiveData<Event<Uri>>
        get() = _onShare

    fun share() {
        val path = _imagePath.value ?: return
        val content = FileProvider.getUriForFile(
            getApplication(),
            getApplication().applicationContext.packageName + ".provider",
            File(path)
        ) ?: return
        _onShare.postValue(Event(content))
    }
}
