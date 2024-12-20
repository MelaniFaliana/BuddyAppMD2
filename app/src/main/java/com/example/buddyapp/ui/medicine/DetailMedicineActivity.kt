package com.example.buddyapp.ui.medicine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.buddyapp.R
import com.example.buddyapp.data.local.Medicine
import com.example.buddyapp.databinding.ActivityDetailMedicineBinding
import com.example.buddyapp.helper.ViewUtils

class DetailMedicineActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailMedicineBinding

    private lateinit var name: String
    private var price: String? = null
    private var description: String? = null
    private var url: String? = null
    private var imageUrl: String? = null
    private var medicine: Medicine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewUtils.setupView(window, supportActionBar)

        name = intent.getStringExtra(EXTRA_NAME).toString()
        price = intent.getStringExtra(EXTRA_PRICE)
        description = intent.getStringExtra(EXTRA_DESCRIPTION)
        url = intent.getStringExtra(EXTRA_URL)
        imageUrl = intent.getStringExtra(EXTRA_IMAGE)
        medicine = Medicine(
            name = name,
            price = price,
            description = description,
            url = url,
            imageUrl = imageUrl
        )
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.default_image_buddy)
            .into(binding.ivMedImage)
        binding.tvName.text = medicine?.name
        binding.tvPrice.text = medicine?.price
        binding.tvDescription.text = medicine?.description
        binding.btnLink.setOnClickListener(this)
        binding.btnBack.setOnClickListener { finish() }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnLink.id -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PRICE = "extra_price"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_IMAGE = "extra_image"
    }
}