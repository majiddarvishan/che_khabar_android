package ir.rezarasuolzadeh.takhfif.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ir.rezarasuolzadeh.takhfif.R
import ir.rezarasuolzadeh.takhfif.service.extensions.whiteStatusBar
import ir.rezarasuolzadeh.takhfif.service.model.response.SetAdvertiserResponseModel
import ir.rezarasuolzadeh.takhfif.service.utils.CustomToolbar
import ir.rezarasuolzadeh.takhfif.service.utils.Enums
import ir.rezarasuolzadeh.takhfif.viewmodel.AdvertiserViewModel
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.bottom_sheet_location.*
import kotlinx.android.synthetic.main.bottom_sheet_store.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapActivity : AppCompatActivity(), OnMapReadyCallback, Observer<Any?> {

    private lateinit var googleMap: GoogleMap
    private lateinit var locationBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var storeBottomSheetBehavior: BottomSheetBehavior<View>
    private var user: String? = null
    private var currentLocationShowed = false
    private var sellerSelectedPosition : LatLng? = null
    private var account: GoogleSignInAccount? = null
    private val advertiserViewModel by viewModel<AdvertiserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        whiteStatusBar()

        CustomToolbar.setSimpleToolbar(this,"نقشه")

        user = intent.getStringExtra("user")

        locationBottomSheetBehavior = BottomSheetBehavior.from(locationBottomSheet)
        storeBottomSheetBehavior = BottomSheetBehavior.from(storeBottomSheet)

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        account = GoogleSignIn.getLastSignedInAccount(this)

        cancelButton.setOnClickListener {
            closeBottomSheet(locationBottomSheetBehavior)
            finish()
        }

        acceptButton.setOnClickListener {
            closeBottomSheet(locationBottomSheetBehavior)
            finish()
        }

        sendButton.setOnClickListener {
            if(!isCategorySelected()) {
                Toast.makeText(this, "نوع فروشگاه انتخاب نشده", Toast.LENGTH_LONG).show()
            } else if (!isStoreHasName()) {
                Toast.makeText(this, "اسم فروشگاه ثبت نشده", Toast.LENGTH_LONG).show()
            } else {
                advertiserViewModel.setAdvertiser(
                    email = /*account?.email!!*/"reza@test.com",
                    description = storeEditText.text.toString(),
                    latitude = latTextView.text.toString().split(":")[1].toDouble(),
                    longitude = longTextView.text.toString().split(":")[1].toDouble(),
                    startTime = "1400/01/01",
                    endTime = "1401/01/01",
                    tags = getCategory()
                ).observe(this, this)
                closeBottomSheet(storeBottomSheetBehavior)
            }
        }

        notsendButton.setOnClickListener {
            closeBottomSheet(storeBottomSheetBehavior)
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?

        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        this.googleMap.uiSettings.isMapToolbarEnabled = false

        if (user == "customer") {
            currentLocation()
        }

        currentLocationButton.setOnClickListener {
            currentLocation()
        }

        this.googleMap.setOnMapLongClickListener { location ->
            if (user == "seller") {
                latTextView.text = "عرض جغرافیایی: ".plus( location.latitude)
                longTextView.text = "طول جغرافیایی: ".plus( location.longitude)
                sellerSelectedPosition = location
                createMarker(location)
                openBottomSheet(storeBottomSheetBehavior)
            }
        }
    }

    private fun moveCamera(location: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun createMarker(location: LatLng) {
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(location).title("مکان انتخاب شده"))
    }

    @SuppressLint("MissingPermission")
    private fun currentLocation() {
        currentLocationShowed = false
        googleMap.clear()
        googleMap.isMyLocationEnabled = true
        googleMap.setOnMyLocationChangeListener { gps ->
            if (user == "customer") {
                locationTextView.text = "مکان فعلی شما ارسال شود؟"
                openBottomSheet(locationBottomSheetBehavior)
                createMarker(LatLng(gps.latitude, gps.longitude))
                zoomCamera(LatLng(gps.latitude, gps.longitude))
            }
            if (user == "seller") {
                if (!currentLocationShowed) {
                    zoomCamera(LatLng(gps.latitude, gps.longitude))
                    currentLocationShowed = true
                }
            }
        }
    }

    private fun zoomCamera(location: LatLng) {
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), 15.0f
            )
        )
    }

    private fun isCategorySelected() : Boolean{
        if(!radioRestaurant.isChecked && !radioCloths.isChecked && !radioCoffee.isChecked && !radioFruit.isChecked)
            return false
        return true
    }

    private fun isStoreHasName() : Boolean{
        if(storeEditText.length() == 0)
            return false
        return true
    }

    private fun getCategory() : String{
        if(radioFruit.isChecked)
            return "fruit"
        else if(radioCoffee.isChecked)
            return "coffee shop"
        else if(radioCloths.isChecked)
            return "cloths"
        else if(radioRestaurant.isChecked)
            return "restaurant"
        else
            return "coffee shop"
    }

    override fun onBackPressed() {
        if (locationBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            closeBottomSheet(locationBottomSheetBehavior)
        } else if(storeBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            closeBottomSheet(storeBottomSheetBehavior)
        } else{
            super.onBackPressed()
            finish()
        }
    }

    private fun openBottomSheet(sheetBehavior: BottomSheetBehavior<View>) {
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun closeBottomSheet(sheetBehavior: BottomSheetBehavior<View>) {
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onChanged(response: Any?) {
        if(response is String) {
            if (response == "با موفقیت ثبت شد") {
                Toast.makeText(this, "با موفقیت ثبت شد", Toast.LENGTH_LONG).show()
                finish()
            }
            if (response == "کاربری با این ایمیل وجود ندارد") {
                Toast.makeText(this, "کاربری با این ایمیل وجود ندارد", Toast.LENGTH_LONG).show()
            }
        }
        if (response is Enums.DataState) {
            if (response == Enums.DataState.FAILED) {
                Toast.makeText(this, "خطا در انجام عملیات", Toast.LENGTH_LONG).show()
            }
            if (response == Enums.DataState.NOT_INTERNET) {
                Toast.makeText(this, "ارتباط با سرور برقرار نشد", Toast.LENGTH_LONG).show()
            }
        }
    }
}