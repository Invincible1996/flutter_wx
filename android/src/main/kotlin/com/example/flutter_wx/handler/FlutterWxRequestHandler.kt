package com.example.flutter_wx.handler

import io.flutter.plugin.common.PluginRegistry


object FlutterWxRequestHandler {

    private var registrar: PluginRegistry.Registrar? = null

    fun setRegistrar(reg: PluginRegistry.Registrar) {
        registrar = reg
    }

    fun getRegistrar(): PluginRegistry.Registrar? {
        return registrar;
    }
}