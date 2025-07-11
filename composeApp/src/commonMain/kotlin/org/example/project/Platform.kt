package org.example.project

enum class AppPlatform { ANDROID, IOS, DESKTOP }

expect fun getPlatform(): AppPlatform