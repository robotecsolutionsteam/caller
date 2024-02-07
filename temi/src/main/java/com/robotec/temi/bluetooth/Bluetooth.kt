package com.robotec.temi.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.text.SpannableStringBuilder
import android.widget.Toast
import java.util.ArrayDeque

class Bluetooth: ServiceConnection, SerialListener {

    private var globalResponse: SpannableStringBuilder = SpannableStringBuilder()

    private var context: Context? = null

    private val handler = Handler()

    fun setContext(context: Context) {
        this.context = context
    }

    fun response(): String {
        return globalResponse.toString()
    }

    fun startBluetoothService() {
        val intent = Intent(context, SerialService::class.java)
        context?.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    private enum class Connected {
        False, Pending, True
    }

    private var service: SerialService? = null

    private val serialService: SerialService? = null
    private var connected = Connected.False
    private var initialStart = true
    private val hexEnabled = false
    private var pendingNewline = false
    private val newline = TextUtil.newline_crlf

    override fun onServiceConnected(name: ComponentName?, binder: IBinder) {
        service = (binder as SerialService.SerialBinder).service
        service!!.attach(this)
        if (initialStart) {
            initialStart = false
            runOnUiThread { this.connect() }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        service = null
    }

    override fun onSerialConnect() {
        println("connected")
        connected = Connected.True
    }

    override fun onSerialConnectError(e: java.lang.Exception) {
        println("connection failed: " + e.message)
        disconnect()
    }

    override fun onSerialRead(data: ByteArray) {
        val datas = ArrayDeque<ByteArray>()
        datas.add(data)
        receive(datas)
    }

    override fun onSerialRead(datas: ArrayDeque<ByteArray>) {
        receive(datas)
    }

    override fun onSerialIoError(e: java.lang.Exception) {
        println("connection lost: " + e.message)
        disconnect()
    }

    private fun disconnect() {
        connected = Connected.False
        service!!.disconnect()
    }

    private fun connect() {
        try {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val device = bluetoothAdapter.getRemoteDevice("FC:B4:67:50:FD:A6")
            println("connecting...")
            connected = Connected.Pending
            val socket = SerialSocket(context?.applicationContext, device)
            service!!.connect(socket)
        } catch (e: java.lang.Exception) {
            onSerialConnectError(e)
        }
    }

    fun send(str: String) {
        if (connected != Connected.True) {
            Toast.makeText(context, "not connected", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val msg: String
            val data: ByteArray
            if (hexEnabled) {
                val sb = StringBuilder()
                TextUtil.toHexString(sb, TextUtil.fromHexString(str))
                TextUtil.toHexString(sb, newline.toByteArray())
                msg = sb.toString()
                data = TextUtil.fromHexString(msg)
            } else {
                msg = str
                data = (str + newline).toByteArray()
            }
            service!!.write(data)
        } catch (e: java.lang.Exception) {
            onSerialIoError(e)
        }
    }

    fun receive(datas: ArrayDeque<ByteArray>) {
        val spn = SpannableStringBuilder()
        for (data in datas) {
            if (hexEnabled) {
                spn.append(TextUtil.toHexString(data)).append('\n')
            } else {
                var msg = String(data)
                if (newline == TextUtil.newline_crlf && msg.length > 0) {
                    msg = msg.replace(TextUtil.newline_crlf, TextUtil.newline_lf)
                    if (pendingNewline && msg[0] == '\n') {
                        if (spn.length >= 2) {
                            spn.delete(spn.length - 2, spn.length)
                        }
                    }
                    pendingNewline = msg[msg.length - 1] == '\r'
                }
                val caretString = TextUtil.toCaretString(msg, newline.length != 0)
                spn.append(caretString)
                globalResponse.append(caretString)
            }
        }
    }

    private fun runOnUiThread(action: () -> Unit) {
        handler.post { action.invoke() }
    }
}
