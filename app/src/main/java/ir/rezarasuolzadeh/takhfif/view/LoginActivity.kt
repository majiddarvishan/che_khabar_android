package ir.rezarasuolzadeh.takhfif.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import ir.rezarasuolzadeh.takhfif.R
import ir.rezarasuolzadeh.takhfif.service.extensions.whiteStatusBar
import ir.rezarasuolzadeh.takhfif.service.utils.CustomToolbar
import ir.rezarasuolzadeh.takhfif.service.utils.Enums
import ir.rezarasuolzadeh.takhfif.service.utils.UserPreferences
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100
    private var account : GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        whiteStatusBar()

        CustomToolbar.setSimpleToolbar(this,"ورود")

        val role = UserPreferences.getUserRole()

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        account = GoogleSignIn.getLastSignedInAccount(this)

        if(account != null && role == "") {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginButton.setOnClickListener {
            if (account == null) {
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            } else {
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val check = task.isSuccessful
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            account = completedTask.getResult(ApiException::class.java)
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "خوش آمدید", Toast.LENGTH_LONG).show()
        } catch (e: ApiException) {
            Toast.makeText(this, "ورود ناموفق", Toast.LENGTH_LONG).show()
        }
    }

}