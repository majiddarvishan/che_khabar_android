package ir.rezarasuolzadeh.takhfif.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ir.rezarasuolzadeh.takhfif.R
import ir.rezarasuolzadeh.takhfif.service.extensions.hideKeyboard
import ir.rezarasuolzadeh.takhfif.service.extensions.whiteStatusBar
import ir.rezarasuolzadeh.takhfif.service.model.response.SetUserResponseModel
import ir.rezarasuolzadeh.takhfif.service.utils.CustomToolbar
import ir.rezarasuolzadeh.takhfif.service.utils.Enums
import ir.rezarasuolzadeh.takhfif.service.utils.UserPreferences
import ir.rezarasuolzadeh.takhfif.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.bottom_sheet_role.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoActivity : AppCompatActivity(), Observer<Any?> {

    private lateinit var roleBottomSheetBehavior: BottomSheetBehavior<View>
    private var account: GoogleSignInAccount? = null
    private val userViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        whiteStatusBar()

        CustomToolbar.setSimpleToolbar(this, "اطلاعات کاربری")

        roleBottomSheetBehavior = BottomSheetBehavior.from(roleBottomSheet)

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        account = GoogleSignIn.getLastSignedInAccount(this)
        roleEditText.setOnClickListener {
            hideKeyboard()
            openBottomSheet(roleBottomSheetBehavior)
        }

        roleBottomSheet.sellerButton.setOnClickListener {
            roleEditText.setText("صاحب کسب و کار")
            closeBottomSheet(roleBottomSheetBehavior)
        }

        roleBottomSheet.customerButton.setOnClickListener {
            roleEditText.setText("مشتری")
            closeBottomSheet(roleBottomSheetBehavior)
        }

        bottomSheetBlur.setOnClickListener {
            closeBottomSheet(roleBottomSheetBehavior)
        }

        confirmButton.setOnClickListener {
            if (isFieldsEmpty()) {
                Toast.makeText(this, "همه فیلدها باید ثبت شود", Toast.LENGTH_LONG).show()
            } else {
                userViewModel.setUser(
                    email = account!!.email.toString(),
                    firstName = nameEditText.text.toString(),
                    lastName = familyEditText.text.toString(),
                    phone = phoneEditText.text.toString(),
                    userStatus = roleEditText.text.toString()
                ).observe(this, this)
            }
        }

        roleBottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetBlur.visibility = View.GONE
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
                bottomSheetBlur.visibility = View.VISIBLE
                bottomSheetBlur.alpha = slideOffset
            }
        })
    }

    private fun isFieldsEmpty(): Boolean {
        return when {
            nameEditText.length() == 0 -> true
            familyEditText.length() == 0 -> true
            phoneEditText.length() == 0 -> true
            roleEditText.length() == 0 -> true
            else -> false
        }
    }

    override fun onBackPressed() {
        if (roleBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            closeBottomSheet(roleBottomSheetBehavior)
        } else {
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
        if (response is SetUserResponseModel) {
            if (roleEditText.text.toString() == "صاحب کسب و کار") {
                UserPreferences.setUserRole("seller")
                val intent = Intent(this, SellerActivity::class.java)
                intent.putExtra("name", account!!.displayName)
                intent.putExtra("picture", account!!.photoUrl)
                startActivity(intent)
                finish()
            }
            if (roleEditText.text.toString() == "مشتری") {
                UserPreferences.setUserRole("customer")
                val intent = Intent(this, CustomerActivity::class.java)
                intent.putExtra("name", account!!.displayName)
                intent.putExtra("picture", account!!.photoUrl)
                startActivity(intent)
                finish()
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
        if (response is String) {
            Toast.makeText(this, response, Toast.LENGTH_LONG).show()
        }
    }

}
