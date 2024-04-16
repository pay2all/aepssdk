package com.pay2all.aeps.aepsnew

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.textfield.TextInputLayout
import com.pay2all.aeps.AEPSICICI.BankLIst.BankListBottomSheet3DialogFragment
import com.pay2all.aeps.AEPSICICI.BankLIst.BankListItems
import com.pay2all.aeps.AEPSICICI.DeviceScanFormateNew
import com.pay2all.aeps.AEPSICICI.DevicesList.DeviceCardAdapter
import com.pay2all.aeps.AEPSICICI.DevicesList.DevicesItems
import com.pay2all.aeps.AEPSICICI.MiniStateListData.MiniStatementLIst
import com.pay2all.aeps.AEPSICICI.ReceiptTest
import com.pay2all.aeps.AgentVerificationBottomSheet3DialogFragment
import com.pay2all.aeps.AgentVerifyDetail.VerifyAgent
import com.pay2all.aeps.BuildConfig
import com.pay2all.aeps.Constants
import com.pay2all.aeps.CustomeProgressBar
import com.pay2all.aeps.DBHelper
import com.pay2all.aeps.DetectConnection
import com.pay2all.aeps.MyLocation
import com.pay2all.aeps.R
import com.pay2all.aeps.UTLsData
import org.json.JSONException
import org.json.JSONObject
import javax.crypto.SecretKey

class AEPSNewService : AppCompatActivity(),LocationListener {

    lateinit var dbHelper: DBHelper
    lateinit var viewModel: AEPSViewModel
    lateinit var viewModelFactory: AEPSViewModelFactory
    var device_package=""

    private var biometricdata=""
    private var errCode=""
    private var errInfo=""
    private var pidtype=""
    private var Piddata=""
    private var ci=""
    private var qScore=""
    private var sessionKey=""
    private var action="scan"
    private var bank_id=""
    private var type=""
    private var api_id=""
    private var payment_id=""
    private var provider_id=""
    lateinit var alertDialog:AlertDialog

    var dialog: CustomeProgressBar?=null

    private var INTENT_CODE=1421

    lateinit var secretKey: SecretKey


    lateinit var myLocation : MyLocation

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    var lat = 0.0
    var log = 0.0

    lateinit var locationManager: LocationManager

    var provider = ""


    var PERMISSION_ALL = 1
    var PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


    lateinit var textinputlayout_amount: TextInputLayout
    lateinit var edittext_amount: EditText
    lateinit var edittext_customer_aadhaar_number: EditText
    lateinit var edittext_customer_mobile: EditText
    lateinit var imageview_finger_print: ImageView
    lateinit var ll_fingerprint: LinearLayout
    lateinit var ll_select_bank: LinearLayout
    lateinit var ll_select_device: LinearLayout


    lateinit var textview_bank: TextView
    lateinit var textview_capture_quality: TextView
    lateinit var textview_select_device: TextView
    lateinit var tv_term: TextView
    lateinit var cb_term: CheckBox


    lateinit var button_re_capture: Button
    lateinit var button_submit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aepsnew_service)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textinputlayout_amount=findViewById(R.id.textinputlayout_amount)
        edittext_amount=findViewById(R.id.edittext_amount)
        edittext_customer_aadhaar_number=findViewById(R.id.edittext_customer_aadhaar_number)
        edittext_customer_mobile=findViewById(R.id.edittext_customer_mobile)
        imageview_finger_print=findViewById(R.id.imageview_finger_print)
        ll_fingerprint=findViewById(R.id.ll_fingerprint)
        ll_select_bank=findViewById(R.id.ll_select_bank)
        ll_select_device=findViewById(R.id.ll_select_device)
        textview_bank=findViewById(R.id.textview_bank)
        textview_capture_quality=findViewById(R.id.textview_capture_quality)
        textview_select_device=findViewById(R.id.textview_select_device)
        tv_term=findViewById(R.id.tv_term)
        cb_term=findViewById(R.id.cb_term)
        button_submit=findViewById(R.id.button_submit)
        button_re_capture=findViewById(R.id.button_re_capture)

        viewModelFactory= AEPSViewModelFactory(BuildConfig.BASEURL)
        viewModel=ViewModelProvider(this@AEPSNewService,viewModelFactory).get(AEPSViewModel::class.java)
        dbHelper = DBHelper(this)
        dialog=CustomeProgressBar(this@AEPSNewService)

        if (intent.hasExtra("type"))
        {
            type=intent.getStringExtra("type").toString()
            supportActionBar?.setTitle(type)
        }

        if (intent.hasExtra("provider_id"))
        {
            provider_id=intent.getStringExtra("provider_id").toString()
            if (!provider_id.equals(""))
            {
                if (provider_id.equals("175")||provider_id.equals("158"))
                {
                    textinputlayout_amount.visibility=View.VISIBLE
                    mOpenVerifyFragment()
                }
                else
                {
                    textinputlayout_amount.visibility=View.GONE
                    edittext_amount.setText("0")
                }
            }
        }

        if (intent.hasExtra("payment_id"))
        {
            payment_id=intent.getStringExtra("payment_id").toString()
        }

        tv_term.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(this@AEPSNewService)
            builder.setTitle("Terms & Conditions")
            builder.setMessage(resources.getString(R.string.term_condition_icici))
            builder.setPositiveButton(
                "Ok"
            ) { dialog, which ->
                cb_term.setChecked(true)
                dialog.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        })

        ll_select_bank.setOnClickListener {
            if (DetectConnection.checkInternetConnection(this@AEPSNewService))
            {
                val bankListBottomSheet3DialogFragment = BankListBottomSheet3DialogFragment()
                val bundle = Bundle()
                bundle.putString("type", "2")
                bundle.putString("activity", "withnew")
                bankListBottomSheet3DialogFragment.arguments = bundle
                bankListBottomSheet3DialogFragment.show(
                    supportFragmentManager,
                    bankListBottomSheet3DialogFragment.tag
                )
            }
            else
            {
                mShowToast("No internet connection")
            }
        }

        ll_select_device.setOnClickListener(View.OnClickListener {
            mShowDialogDeviceDialog()
        })


        viewModel.mResponse.observe(this@AEPSNewService)
        {
            Log.e("data","here we got "+it.toString())
            dialog?.mDismissDialog()
            if (it!=null)
            {

                var status=""
                var message=""

                try {
                    val jsonObject=JSONObject(it)
                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status")
                    }
                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message")
                    }

                    if (status!="")
                    {
                        if (status.equals("0"))
                        {
                            action="scan"
                            edittext_customer_mobile.setText("")
                            edittext_customer_aadhaar_number.setText("")
                            ll_fingerprint.visibility=View.GONE

                        }
//                        else
//                        {
//                            mShowMessageDialog(message)
//                        }
                    }
                }
                catch (e:Exception)
                {
                    e.printStackTrace()
                }

                if (provider_id.equals("172"))
                {
                    Log.e("mini", "mini response $it")
                    val intent = Intent(this@AEPSNewService, MiniStatementLIst::class.java)
                    intent.putExtra("data", it.toString())
                    intent.putExtra("number", edittext_customer_mobile.text.toString())
                    intent.putExtra("aadhaar", edittext_customer_aadhaar_number.text.toString())
                    startActivity(intent)
                }
                else {
                    mShowReceipt(it)
                }
            }
            else{
                mShowToast("Server not responding")
            }
        }

        button_submit.setOnClickListener {
            if (DetectConnection.checkInternetConnection(this@AEPSNewService))
            {
                if (edittext_customer_mobile.text.toString().equals(""))
                {
                    mShowToast("Please enter customer aadhaar number")
                }
                else if (edittext_customer_mobile.text.toString().length<10)
                {
                    mShowToast("Please enter a valid customer mobile number")
                }
                else if(edittext_customer_aadhaar_number.text.toString().equals(""))
                {
                    mShowToast("Please enter aadhaar number")
                }
                else if (textinputlayout_amount.visibility==View.VISIBLE&&edittext_amount.text.toString().equals(""))
                {
                    mShowToast("Please enter transaction amount")
                }
                else if (bank_id.equals(""))
                {
                    mShowToast("Please enter bank name")
                }
                else if (device_package.equals(""))
                {
                    mShowToast("Please select fingerprint device")
                }
                else if (!cb_term.isChecked)
                {
                    mShowToast("Please accept terms & conditions")
                }
                else{
                    if (action.equals("submit"))
                    {
                        secretKey = UTLsData.generateKey(this.dbHelper.mGet());
                        val json_data=mEncodByteToStringBase64(UTLsData.encryptMsg(mGetJSONData(), this.secretKey))

                        Log.e("data","json_data "+mGetJSONData())
                        dialog?.mShowDialog()
                        viewModel.mCallAEPSAPI(biometricdata,json_data.toString())

                    }
                    else{
                        mCheckAppInstall()
                    }
                }
            }
            else
            {
                mShowToast("No internet connection")
            }
        }

        button_re_capture.setOnClickListener {
            mCheckAppInstall()
        }

        myLocation  = MyLocation()

        Handler().postDelayed({
            if (!VerifyAgent.hasPermissions(this@AEPSNewService, *PERMISSIONS)) {
                ActivityCompat.requestPermissions(this@AEPSNewService, PERMISSIONS, PERMISSION_ALL)
            }
            else {
                val r = myLocation.getLocation(
                    applicationContext,
                    locationResult
                )
                if (r) {
                    Log.e("location", "found")
                } else {
                    Log.e("location", "Not found")
                }
            }
        }, 700)

    }

    private fun mShowMessageDialog(msg:String) {
        val inflate = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.custom_alertdalog_for_message, null)
        val textView = inflate.findViewById<View>(R.id.button_ok) as TextView
        val textView2 = inflate.findViewById<View>(R.id.textview_message) as TextView
        textView2.setText(msg)
        textView2.setTextColor(Color.parseColor("#6c6c6c"))
        val imageView = inflate.findViewById<ImageView>(R.id.imageview_messase_image)
        imageView.setImageResource(R.drawable.error_icon)
        imageView.visibility = View.GONE
        val builder = AlertDialog.Builder(this@AEPSNewService)
        builder.setCancelable(false)
        builder.setView(inflate)
        val create = builder.create()
        textView.setOnClickListener { create.dismiss() }
        create.show()
    }

    private fun mGetJSONData(): String {
        var jsonObject:JSONObject ?=null
        try {
            jsonObject= JSONObject()
            jsonObject.put("mobile_number",Constants.mobile)
            jsonObject.put("payment_id",payment_id)
            jsonObject.put("customer_mobile_number",edittext_customer_mobile.text.toString())
            jsonObject.put("amount",edittext_amount.text.toString())
            jsonObject.put("aadhar_number",edittext_customer_aadhaar_number.text.toString())
            jsonObject.put("aadhaar_number",edittext_customer_aadhaar_number.text.toString())
            jsonObject.put("provider_id",provider_id)
            jsonObject.put("api_id",api_id)
            jsonObject.put("bank_id",bank_id)
            jsonObject.put("PidDatatype",pidtype)
            jsonObject.put("ci",ci)
            jsonObject.put("lat",lat.toString())
            jsonObject.put("long",log.toString())
            jsonObject.put("client_id",Constants.mobile+System.currentTimeMillis())
//            jsonObject.put("biometric_data",biometricdata)

        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }

        return jsonObject.toString()
    }

    private fun mShowToast(msg:String)
    {
        Toast.makeText(applicationContext,msg,Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home)
        {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun mCheckAppInstall() {
        val str = "android.intent.action.VIEW"
        if (isPackageInstalled(device_package, packageManager)) {
            mGetBioData(device_package)
            return
        }
        try {
            val sb = StringBuilder()
            sb.append("market://details?id=")
            sb.append(device_package)
            startActivity(Intent(str, Uri.parse(sb.toString())))
        } catch (unused: ActivityNotFoundException) {
            val sb2 = StringBuilder()
            sb2.append("https://play.google.com/store/apps/details?id=")
            sb2.append(device_package)
            startActivity(Intent(str, Uri.parse(sb2.toString())))
        }
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun mGetBioData(package_name: String) {
        try {
            var pIDOptions = DeviceScanFormateNew.createPidOptXML("0")
            if (package_name.equals("com.precision.pb510.rdservice", ignoreCase = true)) {
                pIDOptions = DeviceScanFormateNew.createPrecisionPidOptXML("0")
            } else if (package_name.equals("com.secugen.rdservice", ignoreCase = true)) {
                pIDOptions = DeviceScanFormateNew.createSecugenPidXML("0")
                //                Toast.makeText(this, "selected package "+package_name+"\nSecugen Block Executed", Toast.LENGTH_SHORT).show();
            } else if (package_name.equals("com.acpl.registersdk", ignoreCase = true)) {
                pIDOptions = DeviceScanFormateNew.createPidOptXMLOld("0")
                //                Toast.makeText(this, "selected package "+package_name+"\nSecugen Block Executed", Toast.LENGTH_SHORT).show();
            }
            if (pIDOptions != null) {
                Log.e("PidOptions", pIDOptions)
                val intent = Intent()
                intent.setPackage(package_name)
                intent.action = "in.gov.uidai.rdservice.fp.CAPTURE"
                intent.putExtra("PID_OPTIONS", pIDOptions)

                INTENT_CODE=1421
                resultLauncher.launch(intent)
            }
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
    }



    protected fun mOpenVerifyFragment() {
        val bankListBottomSheet3DialogFragment = AgentVerificationBottomSheet3DialogFragment()
        val bundle = Bundle()
        bundle.putString("activity", "withdrawalNew")
        bankListBottomSheet3DialogFragment.arguments = bundle
        bankListBottomSheet3DialogFragment.show(
            supportFragmentManager,
            bankListBottomSheet3DialogFragment.tag
        )
    }


    fun mCheckCamera(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            false
        }
    }

    fun mRequestCamera() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this@AEPSNewService,
                "android.permission.CAMERA"
            )
        ) {
            ActivityCompat.requestPermissions(
                this@AEPSNewService,
                arrayOf<String>("android.permission.CAMERA"),
                1
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun mShowReceipt(str: String?) {
        var status_id = ""
        try {
            val jsonObject = JSONObject(str)
            if (jsonObject.has("status_id")) {
                status_id = jsonObject.getString("status_id")
            }
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }

        val intent = Intent(this@AEPSNewService, ReceiptTest::class.java)
        intent.putExtra("status", status_id)
        intent.putExtra("data", str)
        intent.putExtra("number", edittext_customer_mobile.text.toString())
        intent.putExtra("aadhaar", edittext_customer_aadhaar_number.text.toString())
        intent.putExtra("service_name", type)
        intent.putExtra("provider_id", provider_id)
        startActivity(intent)

        if (status_id!="") {
            if (status_id == "1" || status_id.equals("success", ignoreCase = true)) {
                edittext_customer_mobile.setText("")
                edittext_customer_aadhaar_number.setText("")

                action = "scan"
                ll_fingerprint.setVisibility(View.GONE)
                button_re_capture.setVisibility(View.GONE)
                button_submit.setText(resources.getString(R.string.capture_fingerprint))
                bank_id = ""
                device_package = ""
                textview_select_device.setText("Select Device")
                biometricdata = ""
                textview_bank.setText("Select Bank")
            }
        }
    }

    private fun mShowDialogDeviceDialog() {
        val inflate = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.custome_alert_dialog_show_devices_list,
            null
        )
        val imageView = inflate.findViewById<View>(R.id.imageview_close) as ImageView
        val recyclerView = inflate.findViewById<View>(R.id.recyclerview) as RecyclerView
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(inflate)
        this.alertDialog = builder.create()
        val strArr = arrayOf("Mantra", "Morpho", "Startek", "SecuGen", "Tatvik", "Precision")
        val strArr2 = arrayOf(
            "MANTRA_PROTOBUF",
            "MORPHO_PROTOBUF",
            "STARTEK_PROTOBUF",
            "SECUGEN_PROTOBUF",
            "TATVIK_PROTOBUF",
            "PRECISION_PROTOBUF"
        )
        val strArr3 = arrayOf(
            "com.mantra.rdservice",
            "com.scl.rdservice",
            "com.acpl.registersdk",
            "com.secugen.rdservice",
            "com.tatvik.bio.tmf20",
            "com.precision.pb510.rdservice"
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val arrayList: ArrayList<DevicesItems?> = ArrayList()
        val deviceCardAdapter = DeviceCardAdapter(this, arrayList)
        recyclerView.adapter = deviceCardAdapter
        for (i in strArr.indices) {
            val devicesItems = DevicesItems()
            val sb = java.lang.StringBuilder()
            sb.append(i)
            sb.append("")
            devicesItems.id = sb.toString()
            devicesItems.name = strArr[i]
            devicesItems.package_name = strArr3[i]
            devicesItems.type = strArr2[i]
            devicesItems.fragment_type = "withnew"
            arrayList.add(devicesItems)
            deviceCardAdapter.notifyDataSetChanged()
        }
        imageView.setOnClickListener { alertDialog.dismiss() }
        this.alertDialog.show()
    }

    fun mGetDeviceDetail(item: DevicesItems)
    {
        alertDialog.dismiss()
        textview_select_device.setText(item.name)
        device_package=item.package_name
    }

    fun mGetBankDetail(item: BankListItems)
    {
        textview_bank.setText(item.bank_name)
        bank_id = item.id
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            val str = "qScore"
            val str2 = "ci"
            val str3 = "errInfo"
            val str4 = "type"
            val str5 = "Skey"
            val str6 = "errCode"
            val str7 = "Data"
            val str8 = "Resp"
            val str9 = "PidData"
            if (result.resultCode== RESULT_OK) {

                if (INTENT_CODE==1421) {
                    this.biometricdata = data?.getStringExtra("PID_DATA").toString()

                    Log.e("data", "biometric data here we got $biometricdata")
//            val str10 = ""
//            this.biometricdata = this.biometricdata.replace("\n".toRegex(), str10)
//            this.biometricdata = this.biometricdata.replace(
//                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>",
//                str10
//            )
//            val sb = java.lang.StringBuilder()
//            sb.append("result ")
//            sb.append(biometricdata)
//            Log.e("bio data", sb.toString())
                    try {
                        val json = JSONObject(UtilsXmlToJson(this).mGetJson(biometricdata))
                        if (json.has(str9)) {
                            val jSONObject = json.getJSONObject(str9)
                            if (jSONObject.has(str8)) {
                                val jSONObject2 = jSONObject.getJSONObject(str8)
                                if (jSONObject2.has(str6)) {
                                    this.errCode = jSONObject2.getString(str6)
                                }
                                if (jSONObject2.has(str3)) {
                                    this.errInfo = jSONObject2.getString(str3)
                                }
                                if (jSONObject2.has(str)) {
                                    this.qScore = jSONObject2.getString(str)
                                }
                            }
                            val str11 = "content"
                            if (jSONObject.has(str7)) {
                                val jSONObject3 = jSONObject.getJSONObject(str7)
                                if (jSONObject3.has(str4)) {
                                    this.pidtype = jSONObject3.getString(str4)
                                }
                                if (jSONObject3.has(str11)) {
                                    this.Piddata = jSONObject3.getString(str11)
                                }
                            }
                            if (jSONObject.has(str5)) {
                                val jSONObject4 = jSONObject.getJSONObject(str5)
                                if (jSONObject4.has(str2)) {
                                    this.ci = jSONObject4.getString(str2)
                                }
                                if (jSONObject4.has(str11)) {
                                    this.sessionKey = jSONObject4.getString(str11)
                                }
                            }
                            if (this.errCode == "0") {
                                this.ll_fingerprint.setVisibility(View.VISIBLE)
                                this.imageview_finger_print.setColorFilter(
                                    resources.getColor(
                                        R.color.green
                                    )
                                )
                                this.action = "submit"
                                this.button_submit.setText(resources.getString(R.string.proceed_now))
                                this.button_re_capture.setVisibility(View.VISIBLE)
                                val sb2 = java.lang.StringBuilder()
                                sb2.append("Capture Score ")
                                sb2.append(this.qScore)
                                sb2.append(" %")
                                textview_capture_quality.text = sb2.toString()
                            }
                            else {
                                this.action = "scan"
                                this.ll_fingerprint.setVisibility(View.GONE)
                                this.button_re_capture.setVisibility(View.GONE)
                                this.button_submit.setText(resources.getString(R.string.capture_fingerprint))
                                Toast.makeText(this, this.errInfo, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                else{
                    if (intent.hasExtra("uid")) {
                        edittext_customer_aadhaar_number.setText(intent.getStringExtra("uid"))
                        //                    Log.e("uid","uid length "+edittext_customer_aadhaar_number.getText().toString().length());
                    }
                }
            }
        }
    }

    fun mEncodByteToStringBase64(bArr: ByteArray?): String? {
        return Base64.encodeToString(bArr, 0)
    }


    override fun onLocationChanged(location: Location) {
        Log.e("data", "lat long \nlat " + location.latitude + "\nlong " + location.longitude)
    }


    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        super<LocationListener>.onStatusChanged(provider, status, extras)
    }

    override fun onProviderEnabled(provider: String) {
        super<LocationListener>.onProviderEnabled(provider)
    }

    override fun onProviderDisabled(provider: String) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }
        super<LocationListener>.onProviderDisabled(provider)
    }

    fun statusCheck() {
        val manager = getSystemService(
            LOCATION_SERVICE
        ) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(
            "Your GPS seems like disabled, Please enable to get live location"
        )
            .setCancelable(false).setPositiveButton(
                "Yes"
            ) { dialog, id ->
                startActivity(
                    Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                )
            }
            .setNegativeButton(
                "No"
            ) { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    protected fun mShowLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }
        locationManager = getSystemService(
            LOCATION_SERVICE
        ) as LocationManager
        statusCheck()

        // Creating an empty criteria object
        val criteria = Criteria()

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false)!!
        if (provider != null && provider != "") {
            if (!provider.contains("gps")) { // if gps is disabled
                val poke = Intent()
                poke.setClassName(
                    "com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider"
                )
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
                poke.data = Uri.parse("3")
                sendBroadcast(poke)
            }
            // Get the location from the given provider
            var location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 500, 0f, this
            )
            if (location != null) onLocationChanged(location) else location =
                locationManager.getLastKnownLocation(provider)
            if (location != null) onLocationChanged(location) else Toast.makeText(
                baseContext, "Location can't be retrieved",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                baseContext, "No Provider Found",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    var locationResult: MyLocation.LocationResult = object : MyLocation.LocationResult() {
        override fun gotLocation(location: Location) {
            val Longitude = location.longitude
            val Latitude = location.latitude

//            Toast.makeText(getApplicationContext(), "Got Location",
//                    Toast.LENGTH_LONG).show();
            lat = Latitude
            log = Longitude
//            binding.tvLatLong.text = "Latitude : $lat, Longitude : $log"
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val r = myLocation.getLocation(
            applicationContext,
            locationResult
        )
        if (r) {
            Log.e("location", "found")
        }
        else {
            Log.e("location", "Not found")
        }
    }
}