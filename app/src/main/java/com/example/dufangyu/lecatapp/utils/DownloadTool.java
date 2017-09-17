package com.example.dufangyu.lecatapp.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.example.dufangyu.lecatapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTool extends AsyncTask<String, Integer, Boolean> {
	 private Activity context;
	    private ProgressDialog dialog;
	    private File file;

	    public DownloadTool(Activity context) {
	        super();
	        this.context = context;
	    }

	    @Override
	    protected void onPreExecute() {
	        dialog = new ProgressDialog(context);
	        dialog.setCancelable(false);
	        dialog.setTitle("下载最新版本...");
	        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        dialog.show();
	    }

	    @Override
	    protected Boolean doInBackground(String... params) {
	        try {
//	            URL url = new URL(URLEncoder.encode(params[0]));
	            URL url = new URL(params[0]);
	            DownThread DT = new DownThread(url);
	            DT.start();
	            while (true) {
	                if (DT.getSize() == 0) {
	                    continue;
	                }
	                int downSize = 0;
	                // 没有下载完毕
	                downSize += DT.getSum();
	                publishProgress(downSize);
	                if (downSize >= DT.getSize() || !DT.isSuccess()) {
	                    break;
	                }
	                Thread.sleep(500);
	            }
	            return DT.isSuccess();
	        } catch (Exception e) {
	        }
	        return false;
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	        dialog.dismiss();
	        if (result) {
	            Builder builder = new Builder(context);
	            builder.setTitle("下载完毕");
	            builder.setMessage("将为您安装最新下载程序");
	            builder.setCancelable(false);
	            builder.setPositiveButton("安装", new OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {

						// 更新apk程序
						Intent intent = new Intent(Intent.ACTION_VIEW);
						Uri data;
						// 判断版本大于等于7.0
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							// "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
							data = FileProvider.getUriForFile(context, "com.example.dufangyu.lecatapp.fileprovider", file);
							// 给目标应用一个临时授权
							intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
						} else {
							data = Uri.fromFile(file);
						}
						intent.setDataAndType(data, "application/vnd.android.package-archive");
						context.startActivity(intent);

//	                    Intent intent = new Intent();
//	                    intent.setAction("android.intent.action.VIEW");
//	                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//	                    context.startActivityForResult(intent, 0);
	                }
	            });
	            builder.create().show();
	        } else {
	            Builder builder = new Builder(context);
	            builder.setTitle("下载失败");
	            builder.setMessage("请您稍后选择重新下载");
	            builder.setCancelable(false);
	            builder.setPositiveButton("确定", null);
	            builder.create().show();
	        }

	    }

	    @Override
	    protected void onProgressUpdate(Integer... values) {

	        dialog.setProgress(values[0]);
	    }

	    class DownThread extends Thread {
	        private URL url;
	        private int sum;
	        private int size = 0;
	        private boolean isSuccess = true;   //下载是否成功

	        public DownThread(URL url) {
	            super();
	            this.url = url;
	        }

	        public int getSum() {
	            return sum;
	        }

	        public int getSize() {
	            return size;
	        }

	        public boolean isSuccess() {
	            return isSuccess;
	        }

	        @Override
	        public void run() {

	            InputStream is = null;
	            OutputStream os = null;
	            HttpURLConnection conn = null;
	            try {
	                file = new File(context.getExternalCacheDir().getPath(), R.string.app_name+".apk");
	                LogUtil.d("dfy", "filePath = "+context.getExternalCacheDir().getPath());
	                if (file.exists()) {
	                    file.delete();
	                }
	                
	                conn = (HttpURLConnection) url.openConnection();
	                conn.setConnectTimeout(20 * 1000);
	                conn.setRequestMethod("GET");
	                conn.setRequestProperty("Accept-Language", "zh-CN");
	                conn.setRequestProperty("Charset", "UTF-8");
	                // 获取数据文件的大小
	                size = conn.getContentLength();
	                dialog.setMax(size);
	                //下载文件
	                is = conn.getInputStream();
	                os = new FileOutputStream(file);
	                byte[] buffer = new byte[1024 * 2];
	                int hasRead = 0;
	                while ((hasRead = is.read(buffer)) >= 0) {
	                    os.write(buffer, 0, hasRead);
	                    sum += hasRead;
	                }
	            } catch (Exception e) {
	            	e.printStackTrace();
	                isSuccess = false;
	            } finally {
	                try {
	                    if (null != is) {
	                        is.close();
	                    }
	                    if (null != os) {
	                        os.close();
	                    }
	                } catch (IOException e) {
	                }
	                if (null != conn) {
	                    conn.disconnect();
	                }
	            }
	        }
	    }
}
