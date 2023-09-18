package id.emergence.wher.screen.settings

import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceGroup
import androidx.preference.PreferenceScreen

@DslMarker
@Target(AnnotationTarget.TYPE)
annotation class SettingsDsl

inline fun PreferenceGroup.preference(block: (@SettingsDsl Preference).() -> Unit): Preference {
    return initThenAdd(Preference(context), block)
}

inline fun PreferenceScreen.preferenceCategory(block: (@SettingsDsl PreferenceCategory).() -> Unit): PreferenceCategory {
    return addThenInit(
        PreferenceCategory(context).apply {
            isIconSpaceReserved = false
        },
        block,
    )
}

inline fun <P : Preference> PreferenceGroup.initThenAdd(
    p: P,
    block: P.() -> Unit,
): P {
    return p.apply {
        block()
        this.isIconSpaceReserved = false
        addPreference(this)
    }
}

inline fun <P : Preference> PreferenceGroup.addThenInit(
    p: P,
    block: P.() -> Unit,
): P {
    return p.apply {
        this.isIconSpaceReserved = false
        addPreference(this)
        block()
    }
}

inline fun Preference.onClick(crossinline block: () -> Unit) {
    setOnPreferenceClickListener {
        block()
        true
    }
}

inline fun Preference.onChange(crossinline block: (Any?) -> Boolean) {
    setOnPreferenceChangeListener { _, newValue -> block(newValue) }
}

var Preference.defaultValue: Any?
    get() = null // set only
    set(value) {
        setDefaultValue(value)
    }

var Preference.titleRes: Int
    get() = 0 // set only
    set(value) {
        setTitle(value)
    }
