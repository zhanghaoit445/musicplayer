package com.example.musicplay;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class DonCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getAction();
		System.out.println(action);
		if(action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {//下载完成
			long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);   
			//TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
			Toast.makeText(context, 
					"Done",1).show();
		//	ACTION_MEDIA_REMOVED 被删除
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
			+ Environment.getExternalStorageDirectory())));
		/*	Query query = new Query();
			query.setFilterById(id);
			downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			Cursor cursor = downloadManager.query(query);
			int columnCount = cursor.getColumnCount();
			String path = null;                                 
			ContentValues values=new ContentValues();
			context.getContentResolver().insert(MyConstant.MUSIC_URI, values);
			while(cursor.moveToNext()) { //有sd卡到时候
				for (int j = 0; j < columnCount; j++) {
					String columnName = cursor.getColumnName(j);
					String string = cursor.getString(j);
					if(columnName.equals("local_uri")) {
						path = string;
					}
					if(string != null) {
						System.out.println(columnName+": "+ string);
					}else {
						System.out.println(columnName+": null");
					}
					System.out.println(columnName+"2222222"+string);
				}
			}
			cursor.close();*/
			              /*    if(path.startsWith("content:")) {//没有sd卡到时候  放在内置存储上
                               cursor = context.getContentResolver().query(Uri.parse(path), null, null, null, null);
                               columnCount = cursor.getColumnCount();
                               while(cursor.moveToNext()) {
                                    for (int j = 0; j < columnCount; j++) {
                                                String columnName = cursor.getColumnName(j);
                                                String string = cursor.getString(j);
                                                if(string != null) {
                                                     System.out.println(columnName+": "+ string);
						}else {
							System.out.println(columnName+": null");
						}
					}
				}
				cursor.close();
			}*/
			
		}/*else if(action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
			//点击正在下载框到时候
Toast.makeText(context, 
		"dolanding",1).show();
//全部无须处理  
		}*//*else if (action.equals(DownloadManager.ERROR_INSUFFICIENT_SPACE)) {// 网络链接 中断错误
		
		}else if (action.equals(DownloadManager.ERROR_INSUFFICIENT_SPACE)) {// 空间不够
			
		}else if (action.equals(DownloadManager.ERROR_FILE_ALREADY_EXISTS)) {// 文件已经那个存在
			
		}else if (action.equals(DownloadManager.	ERROR_CANNOT_RESUME)) {// 未知错去
			
		}else if (action.equals(DownloadManager.	ERROR_DEVICE_NOT_FOUND	)) {// 没有 外部存储
			
		}*/
	}

}
