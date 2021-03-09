package ir.rezarasuolzadeh.takhfif.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import ir.rezarasuolzadeh.takhfif.R
import ir.rezarasuolzadeh.takhfif.service.extensions.whiteStatusBar
import ir.rezarasuolzadeh.takhfif.service.utils.CustomToolbar
import ir.rezarasuolzadeh.takhfif.service.utils.Enums
import ir.rezarasuolzadeh.takhfif.service.utils.UserPreferences
import ir.rezarasuolzadeh.takhfif.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_seller.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception

class SellerActivity : AppCompatActivity(), Observer<Any?> {

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var name : String? = null
    private var picture : String? = null
    private val userViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller)
        whiteStatusBar()

        CustomToolbar.setSimpleToolbar(this,"صاحب کسب و کار")

        name = intent.getStringExtra("name")
        picture = intent.getStringExtra("picture")

        nameTextView.text = name

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val account = GoogleSignIn.getLastSignedInAccount(this)

        userViewModel.getUser(account!!.email.toString()).observe(this, this)

        exitButton.setOnClickListener {
            mGoogleSignInClient.signOut()
            UserPreferences.removeUserRole()
            finish()
        }

        mapButton.setOnClickListener {
            if (!hasPermissions(this, permissions)) {
                ActivityCompat.requestPermissions(this, permissions, 1)
            } else {
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra("user", "seller")
                startActivity(intent)
            }
        }

    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(
                context,
                it.toString()
            ) == PackageManager.PERMISSION_GRANTED
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(Intent(this, MapActivity::class.java))
                }
                return
            }
        }
    }

    override fun onChanged(t: Any?) {
        // complete this section to observe
    }
}