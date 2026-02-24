package torilab.assessment.notes.data.local.preferences

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class AppPreferences @Inject constructor(
    private val prefs: SharedPreferences
) {
    var isDarkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) = prefs.edit { putBoolean(KEY_DARK_MODE, value) }

    val isDarkModeFlow: Flow<Boolean>
        get() = observeKey(KEY_DARK_MODE, false)

    private fun <T> observeKey(key: String, default: T): Flow<T> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == key) {
                @Suppress("UNCHECKED_CAST")
                val value = prefs.all[key] as? T ?: default
                trySend(value)
            }
        }
        @Suppress("UNCHECKED_CAST")
        trySend(prefs.all[key] as? T ?: default)
        prefs.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }.distinctUntilChanged()

    companion object {
        private const val KEY_DARK_MODE = "dark_mode"
        const val PREFS_NAME = "tori_notes_prefs"
    }
}
