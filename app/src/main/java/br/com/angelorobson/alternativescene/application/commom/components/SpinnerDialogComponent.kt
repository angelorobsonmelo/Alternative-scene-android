package br.com.angelorobson.alternativescene.application.commom.components

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.databinding.SpinnerDialogComponentBinding
import br.com.ilhasoft.support.validation.Validator


class SpinnerDialogComponent(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var mSpinnerDialog: SpinnerDialog? = null
    private var arrayListToSelec = ArrayList<String>()
    private var itemSelected = ""
    private lateinit var mBinding: SpinnerDialogComponentBinding
    private var mHint: String? = null
    lateinit var mValidator: Validator

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.spinner_dialog_component, this, true
        )

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.SpinnerDialogComponent)
            with(typedArray) {
                mHint = getString(R.styleable.SpinnerDialogComponent_spinner_hint)
                mBinding.spinnerTextInputLayout.hint = mHint
                recycle()
            }
        }

        setupValidator()
    }

    private fun setupValidator() {
        mValidator = Validator(mBinding)
        mValidator.enableFormValidationMode()
    }

    fun setUpSpinnerDialog(
        arrayList: List<String>,
        activity: FragmentActivity?,
        handler: SpinnerDialogComponentHandler,
        code: Int = 0
    ) {
        arrayListToSelec = arrayList as ArrayList<String>

        mSpinnerDialog = SpinnerDialog(
            activity,
            arrayList,
            context.getString(R.string.search)
        )

        mBinding.bankNameTextView.setOnClickListener {
            mSpinnerDialog?.showSpinerDialog()
            if (arrayList.isEmpty()) {
                mSpinnerDialog?.showProgressBar()
                return@setOnClickListener
            }

            hideProgressBar()
        }

        mSpinnerDialog?.bindOnSpinerListener { _, position ->
            handler.setPositionSelected(position, code)
            itemSelected = arrayListToSelec[position]
            mBinding.bankNameTextView.setText(itemSelected)
        }
    }

    fun hideProgressBar() {
        mSpinnerDialog?.hideProgressBar()
    }

    fun addMoreItemns(items: List<String>) {
        arrayListToSelec = ArrayList(items)
        mSpinnerDialog?.addMoreItems(arrayListToSelec)
    }

}