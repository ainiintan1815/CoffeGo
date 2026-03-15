package com.example.coffeeshop.ui.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.coffeeshop.activity.BaseActivity
import com.example.coffeeshop.adapter.PreviewCartAdapter
import com.example.coffeeshop.adapter.SizeAdapter
import com.example.coffeeshop.databinding.ActivityDetailedBinding
import com.example.coffeeshop.helper.ManagmentCart
import com.example.coffeeshop.model.ItemsModel
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class DetailedActivity : BaseActivity() {

    private lateinit var item: ItemsModel
    private val binding: ActivityDetailedBinding by lazy {
        ActivityDetailedBinding.inflate(layoutInflater)
    }
    private lateinit var managementcart: ManagmentCart

    private var selectedSizeIndex = 0
    private var basePrice = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        managementcart = ManagmentCart(this)
        bundle()
        refreshPreviewCart()
        initSizeList()
    }

    private fun refreshPreviewCart() {
        val cartList = managementcart.getListCart()

        if (cartList.isEmpty()) {
            binding.rvPreviewCart.visibility = View.GONE
            return
        }

        binding.rvPreviewCart.visibility = View.VISIBLE
        binding.rvPreviewCart.layoutManager = LinearLayoutManager(this)
        binding.rvPreviewCart.adapter = PreviewCartAdapter(cartList)
    }


    private fun initSizeList() {
        val sizeList = arrayListOf("Small", "Medium", "Large", "Extra Large")

        val adapter = SizeAdapter(this, sizeList) { index ->
            selectedSizeIndex = index

            item.numberInCart = 1
            binding.numberItemTxt.text = "1"

            updatePrice()
        }

        binding.rvSizeList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSizeList.adapter = adapter

        val firstImg = item.picUrl.firstOrNull()
        Glide.with(this)
            .load(firstImg)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
            .into(binding.shapeableImageView)
    }


    @SuppressLint("SetTextI18n")
    private fun bundle() {
        binding.apply {
            item = intent.getParcelableExtra("object")!!
            titleTxt.text = item.title
            descriptionTxt.text = item.description

            basePrice = item.price.toDouble()

            item.numberInCart = 1
            numberItemTxt.text = "1"

            updatePrice()

            ratingBar.rating = item.rating.toFloat()

            addToCart.setOnClickListener {
                val finalUnitPrice = basePrice + getSizeAdder(selectedSizeIndex)

                val cartItem = item.copy()
                cartItem.price = finalUnitPrice
                cartItem.numberInCart = numberItemTxt.text.toString().toInt()

                cartItem.extra = when (selectedSizeIndex) {
                    0 -> "size_small"
                    1 -> "size_medium"
                    2 -> "size_large"
                    3 -> "size_xl"
                    else -> "size_small"
                }

                managementcart.insertItems(cartItem)
                refreshPreviewCart()
            }


            ivBack.setOnClickListener { finish() }

            plusCart.setOnClickListener {
                item.numberInCart++
                numberItemTxt.text = item.numberInCart.toString()
                updatePrice()
            }

            minusCart.setOnClickListener {
                if (item.numberInCart > 1) {
                    item.numberInCart--
                    numberItemTxt.text = item.numberInCart.toString()
                    updatePrice()
                }
            }
        }
    }

    // 🔥 Size berdasarkan harga tambahan
    private fun getSizeAdder(index: Int): Double {
        return when (index) {
            0 -> 0.0      // size 1
            1 -> 3500.0   // size 2
            2 -> 7500.0   // size 3
            3 -> 10000.0  // size 4
            else -> 0.0
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updatePrice() {
        val sizeAdder = getSizeAdder(selectedSizeIndex)
        val total = (basePrice + sizeAdder) * item.numberInCart
        binding.priceTxt.text = formatRupiah(total)
    }

    private fun formatRupiah(amount: Number): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID).apply {
            currency = Currency.getInstance("IDR")
            maximumFractionDigits = 0
        }
        return formatter.format(amount)
    }
}
