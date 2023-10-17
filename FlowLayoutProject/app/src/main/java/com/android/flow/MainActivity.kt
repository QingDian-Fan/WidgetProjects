package com.android.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.flow.layout.FlowLayout

class MainActivity : AppCompatActivity() {

    private val datas: ArrayList<String> = arrayListOf(
            "城市",
            "不限",
            "白羊座",
            "金牛座",
            "双子座",
            "巨蟹座",
            "年龄",
            "18岁以下",
            "18-22岁",
            "23-26岁",
            "27-35岁",
            "性别",
            "不限",
            "男",
            "女",
            "星座",
            "城市",
            "不限",
            "白羊座",
            "金牛座",
            "双子座",
            "巨蟹座",
            "年龄",
            "18岁以下",
            "18-22岁",
            "23-26岁",
            "27-35岁",
            "性别",
            "不限",
            "男",
            "女",
            "星座"
    )

    private val mFlowLayout: FlowLayout by lazy { findViewById(R.id.flowLayout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFlowLayout.setAdapter(DataFlowAdapter(this@MainActivity, datas))
    }
}