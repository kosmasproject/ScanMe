package com.kosmasfn.calculatorscan.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.kosmasfn.calculatorscan.BuildConfig
import com.kosmasfn.calculatorscan.data.ResultEntity
import com.kosmasfn.calculatorscan.databinding.ActivityMainBinding
import com.kosmasfn.calculatorscan.utils.Constant
import com.kosmasfn.calculatorscan.utils.launchAndCollectIn
import com.kosmasfn.calculatorscan.view.base.BaseActivity
import com.kosmasfn.calculatorscan.view.base.Resource
import com.kosmasfn.calculatorscan.view.di.ResultModel
import com.kosmasfn.calculatorscan.view.di.ResultModel.CalculationData
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

/**
 * Created by Kosmas on September 01, 2023.
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: ResultViewModel by viewModels()
    private val resultHistory: ResultModel = ResultModel(mutableListOf())
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun setBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun setUp(savedInstanceState: Bundle?) {
        initObserver()
        initView()
        initListener()
        fetchDataDefaultStorage()
    }

    private fun initObserver() {
        viewModel.getResultStateFlow().launchAndCollectIn(this, Lifecycle.State.CREATED) {
            when (it) {
                is Resource.StartLoading -> showLoading()
                is Resource.EndLoading -> showLoading(false)
                is Resource.Success -> {
                    showLoading(false)
                    it.data?.data?.let { list ->
                        resultHistory.data.clear()
                        resultHistory.data.addAll(list)
                        setDataAdapter()
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    showToast(it.message)
                }
                else -> showLoading(false)
            }
        }
        viewModel.getSaveResultStateFlow().launchAndCollectIn(this, Lifecycle.State.CREATED) {
            when (it) {
                is Resource.StartLoading -> showLoading()
                is Resource.EndLoading -> showLoading(false)
                is Resource.Success -> {
                    showLoading(false)
                    viewModel.fetchResults()
                }
                is Resource.Error -> {
                    showLoading(false)
                    showToast(it.message)
                }
                else -> showLoading(false)
            }
        }
    }

    private fun initView() = with(binding) {
        rvResult.adapter = ResultAdapter()
        radBtnFile.isChecked = true
    }

    private fun fetchDataDefaultStorage() = fetchDataFromFileStorage()
    private fun setDataAdapter() =
        (binding.rvResult.adapter as ResultAdapter).addItems(resultHistory.data)

    private fun showLoading(show: Boolean = false) {
        binding.loadingLottie.isVisible = show
    }

    private fun initListener() {
        with(binding) {
            btnAddInput.setOnClickListener {
                if (!hasPermissions(
                        this@MainActivity, *Constant.REQUIRED_PERMISSIONS
                    )
                ) ActivityCompat.requestPermissions(
                    this@MainActivity, Constant.REQUIRED_PERMISSIONS, Constant.ALL_PERMISSIONS
                )
                else pickImage()
            }
            radBtnDB.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    resultHistory.data.clear()
                    setDataAdapter()
                    viewModel.fetchResults()
                }
            }
            radBtnFile.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    resultHistory.data.clear()
                    setDataAdapter()
                    fetchDataFromFileStorage()
                }
            }
        }
    }

    private fun fetchDataFromFileStorage() = viewModel.fetchResultFromFile(this@MainActivity)?.let {
        resultHistory.data.addAll(it.data)
        setDataAdapter()
    }

    private fun pickImage() {
        when {
            BuildConfig.FLAVOR.contains("camera") -> {
                ImagePicker.with(this).cameraOnly().compress(1024).crop().start()
            }
            else -> ImagePicker.with(this).galleryOnly().compress(1024).crop().start()
        }
        showLoading(true)
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constant.ALL_PERMISSIONS -> {
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) pickImage()
                else showToast("You must grant all required permissions to continue")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                data?.data?.let {
                    processImage(MediaStore.Images.Media.getBitmap(this.contentResolver, it))
                }
            }
            ImagePicker.RESULT_ERROR -> {
                showLoading()
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                showLoading()
            }
        }
    }

    private fun processImage(bitmap: Bitmap?) =
        bitmap?.let { InputImage.fromBitmap(it, 0) }?.let { image ->
            recognizer.process(image).addOnSuccessListener {
                showLoading()
                checkSign(it.text)
            }
        }

    private fun checkSign(data: String) = when {
        isSignNotFound(data) -> showDataUndefined()
        else -> calculate(splitCalculationData(data), getFirstSignFound(data))
    }

    private fun isSignNotFound(data: String): Boolean {
        return listOf(
            data.indexOf(Constant.PLUS_SIGN),
            data.indexOf(Constant.MINUS_SIGN),
            data.indexOf(Constant.TIMES_SIGN),
            data.indexOf(Constant.DIVISION_SIGN)
        ).filter { it == -1 }.size == 4
    }

    private fun getFirstSignFound(data: String): String {
        return data[getFirstSignIndexFound(data)].toString()
    }

    private fun getFirstSignIndexFound(data: String): Int {
        return listOf(
            data.indexOf(Constant.PLUS_SIGN),
            data.indexOf(Constant.MINUS_SIGN),
            data.indexOf(Constant.TIMES_SIGN),
            data.indexOf(Constant.DIVISION_SIGN)
        ).filter { it != -1 }.min()
    }

    private fun splitCalculationData(data: String): String {
        val signSecondIndex = listOf(
            data.indexOf(Constant.PLUS_SIGN),
            data.indexOf(Constant.MINUS_SIGN),
            data.indexOf(Constant.TIMES_SIGN),
            data.indexOf(Constant.DIVISION_SIGN)
        ).sorted()[1]
        return if (signSecondIndex != -1) {
            data.removeRange(signSecondIndex, data.length)
        } else {
            return data
        }
    }

    private fun calculate(data: String, sign: String) {
        val number = data.split(sign).toMutableList()
        try {
            if (number.size > 1) {
               val result = when (sign) {
                    Constant.PLUS_SIGN -> number[0].toDouble().plus(number[1].toDouble())
                    Constant.MINUS_SIGN -> number[0].toDouble().minus(number[1].toDouble())
                    Constant.TIMES_SIGN -> number[0].toDouble().times(number[1].toDouble())
                    Constant.DIVISION_SIGN -> number[0].toDouble().div(number[1].toDouble())
                    else -> ""
                }
                saveResult(ResultEntity(null, number[0] + sign + number[1], DecimalFormat("#,###.###").format(result)))
            } else {
                showDataUndefined()
            }

        } catch (ex: java.lang.Exception) {
            var additionalInfo = ""
            if (number[0].length > 10 || number[1].length > 10) additionalInfo =
                "The number is too long, the maximum length of each number for this case is 10 (Integer)"
            showToast(additionalInfo + ex.message.toString())
        }
    }

    private fun saveResult(result: ResultEntity) {
        if (binding.radBtnDB.isChecked) {
            viewModel.saveResult(result)
        } else {
            resultHistory.data.add(
                CalculationData(result.input ?: "", result.result ?: "")
            )
            setDataAdapter()
            viewModel.saveResultToFile(this@MainActivity, resultHistory)
        }
    }

    private fun showDataUndefined() =
        showToast("Data cannot be calculated, please check the input data")

    private fun showToast(message: String) =
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
}