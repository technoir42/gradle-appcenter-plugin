package com.betomorrow.gradle.appcenter.extensions

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import java.lang.Exception

open class AppCenterExtension(val project: Project) {

    private var _apiToken: String? = null
    private var _ownerName: String? = null
    private var _distributionGroups: List<String> = emptyList()
    private var _releaseNotes: Any? = null
    private var _notifyTesters: Boolean? = null
    private var _symbols: List<Any> = emptyList()

    var uploadMappingFiles: Boolean = true

    var apps: NamedDomainObjectContainer<AppCenterAppExtension> = project.container(AppCenterAppExtension::class.java) {
        AppCenterAppExtension(it, this)
    }

    var apiToken: String
        get() {
            return _apiToken ?: getGlobalConfig("APPCENTER_API_TOKEN", "")
        }
        set(value) {
            this._apiToken = value
        }

    var ownerName: String
        get() {
            return _ownerName ?: getGlobalConfig("APPCENTER_OWNER_NAME", "")
        }
        set(value) {
            this._ownerName = value
        }

    var releaseNotes: Any
        get() {
            return _releaseNotes ?: getGlobalConfig("APPCENTER_RELEASE_NOTES", "")
        }
        set(value) {
            _releaseNotes = value
        }

    var distributionGroups: List<String>
        get() {
            return if (!_distributionGroups.isEmpty())
                _distributionGroups
            else
                getGlobalConfig("APPCENTER_DISTRIBUTION_GROUPS", "").split(",")
        }
        set(value) {
            _distributionGroups = value
        }

    var notifyTesters: Boolean
        get() {
            return _notifyTesters ?: getGlobalConfig("APPCENTER_NOTIFY_TESTERS", "false").toBoolean()
        }
        set(value) {
            _notifyTesters = value
        }

    var symbols: List<Any>
        get() {
            return if (!_symbols.isEmpty()) {
                return _symbols
            } else {
                getGlobalConfig("APPCENTER_SYMBOLS", "").split(",")
            }
        }
        set(value) {
            _symbols = value
        }

    fun apps(action: Action<NamedDomainObjectContainer<AppCenterAppExtension>>) {
        action.execute(apps)
    }

    fun addApp(name: String, configureAction: Action<AppCenterAppExtension>) {
        val app = AppCenterAppExtension(name, this)
        configureAction.execute(app)
        apps.add(app)
    }

    private fun getGlobalConfig(name: String, defaultValue: String): String {
        return try {
            System.getProperty(name)
        } catch (e: Exception) {
            try {
                System.getenv(name)
            } catch (e: Exception) {
                defaultValue
            }
        }

    }

}
