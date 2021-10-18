package com.example.permission

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.lrxun.lpermission.LPermission
import com.lrxun.lpermission.Permission
import com.lrxun.lpermission.PermissionCallback

class MainActivity: FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.button).setOnClickListener {
            LPermission.with(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(object : PermissionCallback {
                    override fun onResult(permission: Permission) {
                        if (permission.granted) {
                            Toast.makeText(this@MainActivity, "申请成功", Toast.LENGTH_SHORT).show()
                            return
                        }
                        if (permission.shouldShowRequestPermissionRationale) {
                            Toast.makeText(this@MainActivity, "需要展示权限弹窗", Toast.LENGTH_SHORT).show()
                            return
                        }
                        Toast.makeText(this@MainActivity, "申请失败", Toast.LENGTH_SHORT).show()
                    }
                })
        }
        findViewById<View>(R.id.button1).setOnClickListener {
            LPermission.with(this)
                .request(Manifest.permission.CAMERA)
                .subscribe(object : PermissionCallback {
                    override fun onResult(permission: Permission) {
                        if (permission.granted) {
                            Toast.makeText(this@MainActivity, "申请成功", Toast.LENGTH_SHORT).show()
                            return
                        }
                        if (permission.shouldShowRequestPermissionRationale) {
                            Toast.makeText(this@MainActivity, "需要展示权限弹窗", Toast.LENGTH_SHORT).show()
                            return
                        }
                        Toast.makeText(this@MainActivity, "申请失败", Toast.LENGTH_SHORT).show()
                    }
                })
        }
        findViewById<View>(R.id.button2).setOnClickListener {
            LPermission.with(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(object : PermissionCallback {
                    override fun onResult(permission: Permission) {
                        if (permission.granted) {
                            Toast.makeText(this@MainActivity, "申请成功", Toast.LENGTH_SHORT).show()
                            return
                        }
                        if (permission.shouldShowRequestPermissionRationale) {
                            Toast.makeText(this@MainActivity, "需要展示权限弹窗", Toast.LENGTH_SHORT).show()
                            return
                        }
                        Toast.makeText(this@MainActivity, "申请失败", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}