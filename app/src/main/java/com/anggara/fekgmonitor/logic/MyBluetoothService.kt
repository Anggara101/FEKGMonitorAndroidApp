package com.anggara.fekgmonitor.logic

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.anggara.fekgmonitor.ui.home.HomeViewModel
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*


class MyBluetoothService(homeViewModel: HomeViewModel) {
    // Debugging
    private val TAG = "BluetoothChatService"

    val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val listOfDeviceName: ArrayList<String> = ArrayList()
    val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private val mHomeViewModel = homeViewModel

    private var mConnectThread: ConnectThread? = null
    private var mConnectedThread: ConnectedThread? = null
    private var mState = 0
    private var mNewState = 0

    fun getListOfPairedDevices(): List<String>{
        val pairedDevices = bluetoothAdapter.bondedDevices
        pairedDevices.forEach { device ->
            listOfDeviceName.add(device.name)
        }
        return listOfDeviceName
    }

    fun getDeviceAddress(deviceName: String): String{
        val pairedDevices = bluetoothAdapter.bondedDevices
        var deviceAddress = "00:11:22:33:AA:BB"
        pairedDevices.forEach { device ->
            deviceAddress = if (device.name == deviceName){
                device.address
            }else{
                "00:11:22:33:AA:BB"
            }
        }
        return deviceAddress
    }

    // Constants that indicate the current connection state
    val STATE_NONE = 0 // we're doing nothing
    val STATE_LISTEN = 1 // now listening for incoming connections
    val STATE_CONNECTING = 2 // now initiating an outgoing connection
    val STATE_CONNECTED = 3 // now connected to a remote mmDevice

    init {
        mState = STATE_NONE
        mNewState = mState
    }

    /**
     * Return the current connection state.
     */
    @Synchronized
    fun getState(): Int {
        return mState
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    @Synchronized
    fun start() {
        Log.d(TAG, "start")

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread?.cancel()
            mConnectThread = null
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote mmDevice.
     * @param deviceName The BluetoothDevice to connect
     */
    @Synchronized fun connect(deviceName: String) {

        Log.d(TAG, "connect to: $deviceName")

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread?.cancel()
                mConnectThread = null
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }

        // Start the thread to connect with the given mmDevice
        mConnectThread = ConnectThread(deviceName)
        mConnectThread?.start()

    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket The BluetoothSocket on which the connection was made
     * *
     */
    @Synchronized fun connected(socket: BluetoothSocket?) {
        Log.d(TAG, "connected")

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread?.cancel()
            mConnectThread = null
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = ConnectedThread(socket)
        mConnectedThread?.start()
    }

    /**
     * Stop all threads
     */
    @Synchronized fun stop() {
        Log.d(TAG, "stop")

        if (mConnectThread != null) {
            mConnectThread?.cancel()
            mConnectThread = null
        }

        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }

        mState = STATE_NONE
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * *
     * @see ConnectedThread.write
     */
    fun write(out: ByteArray) {
        // Create temporary object
        var r: ConnectedThread?
        // Synchronize a copy of the ConnectedThread
        synchronized(this) {
            if (mState != STATE_CONNECTED) return
            r = mConnectedThread
        }
        // Perform the write unsynchronized
        r?.write(out)
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private fun connectionFailed() {

        mState = STATE_NONE
        Log.d(TAG, "connection failed")
        mHomeViewModel.homeSubtitle.value = "Connection failed"
        // Start the service over to restart listening mode
        this@MyBluetoothService.start()
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private fun connectionLost() {

        mState = STATE_NONE
        mHomeViewModel.homeSubtitle.value = "Connection Lost"
        // Update UI title
        // updateUserInterfaceTitle()

        // Start the service over to restart listening mode
        this@MyBluetoothService.start()
    }

    inner class ConnectThread(deviceName: String) : Thread() {
        private val deviceAddress = getDeviceAddress(deviceName)
        private val mmDevice = bluetoothAdapter.getRemoteDevice(deviceAddress)
        private var mmSocket: BluetoothSocket?

        init {
            var tmp: BluetoothSocket? = null

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = mmDevice?.createRfcommSocketToServiceRecord(MY_UUID)
            } catch (e: IOException) {
                Log.e(TAG, "Socket: create() failed", e)
            }

            mmSocket = tmp
            mState = STATE_CONNECTING
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery()

            // Make a connection to the BluetoothSocket
            try {
                Log.d(TAG, "connect $deviceAddress")
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket?.connect()

            } catch (e: IOException) {
                // Close the socket
                try {
                    mmSocket?.close()
                } catch (e2: IOException) {
                    Log.e(TAG, "unable to close() socket", e2)
                }
                connectionFailed()
                return
            }
            // Reset the ConnectThread because we're done
            synchronized(this@MyBluetoothService) {
                mConnectThread = null
            }

            // Start the connected thread
            connected(mmSocket)
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e("Bluetooth Service", "Could not close the client socket", e)
            }
        }
    }

    /**
     * This thread runs during a connection with a remote mmDevice.
     * It handles all incoming and outgoing transmissions.
     */
    private inner class ConnectedThread(private val mmSocket: BluetoothSocket?) : Thread() {

        private val mmInStream: InputStream?
        private val mmOutStream: OutputStream?

        init {
            Log.d(TAG, "create ConnectedThread")

            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = mmSocket?.inputStream
                tmpOut = mmSocket?.outputStream
            } catch (e: IOException) {
                Log.e(TAG, "temp sockets not created", e)
            }

            mmInStream = tmpIn
            mmOutStream = tmpOut

            mState = STATE_CONNECTED
        }

        override fun run() {
            Log.i(TAG, "BEGIN mConnectedThread")
            val buffer = ByteArray(1024)
            var bytes: Int

            try {
                val message = "2\n"
                mmOutStream?.write(message.toByteArray())
            } catch (e: IOException){
                Log.e(TAG, "Exception during write", e)
            }

            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream?.read(buffer) ?: 0
                    mHomeViewModel.onRead(bytes, buffer)

                } catch (e: IOException) {
                    Log.e(TAG, "disconnected", e)
                    connectionLost()
                    break
                }

            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer The bytes to write
         */
        fun write(buffer: ByteArray) {
            try {
                mmOutStream?.write(buffer)
            } catch (e: IOException) {
                Log.e(TAG, "Exception during write", e)
            }
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "close() of connect socket failed", e)
            }
        }

    }
}