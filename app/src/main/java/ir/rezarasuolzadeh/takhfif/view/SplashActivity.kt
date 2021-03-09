package ir.rezarasuolzadeh.takhfif.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import ir.rezarasuolzadeh.takhfif.R
import ir.rezarasuolzadeh.takhfif.service.extensions.whiteStatusBar
import ir.rezarasuolzadeh.takhfif.service.utils.UserPreferences

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        whiteStatusBar()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val account = GoogleSignIn.getLastSignedInAccount(this)

        Handler().postDelayed({
            if(account == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                val role = UserPreferences.getUserRole()
                if(role == "customer") {
                    val intent = Intent(this, CustomerActivity::class.java)
                    intent.putExtra("name", account.displayName)
                    intent.putExtra("picture", account.photoUrl)
                    startActivity(intent)
                    finish()
                }
                if(role == "seller") {
                    val intent = Intent(this, SellerActivity::class.java)
                    intent.putExtra("name", account.displayName)
                    intent.putExtra("picture", account.photoUrl)
                    startActivity(intent)
                    finish()
                }
                if(role == "") {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }, 1000)
    }
}