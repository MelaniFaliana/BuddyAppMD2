package com.example.buddyapp.ui.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.buddyapp.MainActivity
import com.example.buddyapp.R
import com.example.buddyapp.data.ds.UserPreference
import com.example.buddyapp.data.ds.dataStore
import com.example.buddyapp.data.viewmodelfactory.ViewModelFactory
import com.example.buddyapp.databinding.ActivityLoginBinding
import com.example.buddyapp.helper.ViewUtils
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        ViewUtils.setupView(window, supportActionBar)
        setupAction()
        playAnimation()

        observeViewModel()
        showLoading(false)

        val infoTextViewLogin2 = findViewById<TextView>(R.id.infoTextViewLogin2)
        infoTextViewLogin2.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                showLoading(true)
                viewModel.login(email, password)
                binding.loginButton.isEnabled = false
            } else {
                showDataNotFoundDialog()
            }
        }
    }

    private fun showDataNotFoundDialog() {
        val dialogView = layoutInflater.inflate(R.layout.alert_dialog_data_not_found, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<TextView>(R.id.option_ok).setOnClickListener {
            alertDialog.dismiss() // Dismiss the dialog when OK button is clicked
        }

        alertDialog.show()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.loginResult.collect { result ->
                result?.let {
                    it.onSuccess { userModel ->
                        userPreference.saveName(userModel.name)
                        viewModel.apiMessage.collect { message ->
                            message?.let {
                                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG)
                                    .show()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                        }
                    }.onFailure { exception ->
                        showAlertDialog(
                            exception.message ?: "Terjadi kesalahan",
                            "Password atau Email Salah, Silakan Coba Lagi.",
                            false
                        )
                        binding.loginButton.isEnabled = true
                        showLoading(false)
                    }
                }
            }
        }
    }


    private fun showAlertDialog(
        title: String,
        message: String,
        isSuccess: Boolean,
        onPositiveAction: (() -> Unit)? = null
    ) {
        showLoading(false)
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(if (isSuccess) "Lanjut" else "OK") { _, _ ->
                onPositiveAction?.invoke()
            }
            setCancelable(false)
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 7000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val infoTextViewLogin =
            ObjectAnimator.ofFloat(binding.infoTextViewLogin, View.ALPHA, 1f).setDuration(100)
        val infoTextViewLogin2 =
            ObjectAnimator.ofFloat(binding.infoTextViewLogin2, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                infoTextViewLogin,
                infoTextViewLogin2,
                login
            )
            startDelay = 100
        }.start()
    }
}
