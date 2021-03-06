package com.rockchips.mediacenter.modle.task;

import java.util.List;

import com.rockchips.mediacenter.bean.LocalMediaFile;
import com.rockchips.mediacenter.data.ConstData;
import com.rockchips.mediacenter.modle.db.LocalMediaFileService;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author GaoFei
 * 文件列表加载任务
 */
public class FileLoadTask extends AsyncTask<String, Integer, Integer> {
    private static final String TAG = FileLoadTask.class.getSimpleName();
	public interface Callback{
		void onSuccess(List<LocalMediaFile> mediaFolders);
		void onFailed();
	}
	
	private Callback mCallback;
	private List<LocalMediaFile> mFiles;
	public FileLoadTask(Callback callback){
	    Log.i(TAG, "FileLoadTask");
		mCallback = callback;
	}
	
	@Override
	protected Integer doInBackground(String... params) {
	    Log.i(TAG, "FileLoadTask->doInBackgreound");
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		String parentPath = params[0];
		int mediaType = Integer.parseInt(params[1]);
		String deviceID = params[2];
		LocalMediaFileService fileService = new LocalMediaFileService();
		if(params.length == 3){
			if(!TextUtils.isEmpty(parentPath))
				mFiles = fileService.getFilesByParentPath(parentPath, mediaType);
			else
				mFiles = fileService.getFilesByMediaTypeAndDeviceId(mediaType, deviceID);
		}else{
			int maxCount = Integer.parseInt(params[2]);
			mFiles = fileService.getFilesByParentPath(parentPath, mediaType, maxCount);
		}
		return ConstData.TaskExecuteResult.SUCCESS;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		if(result == ConstData.TaskExecuteResult.SUCCESS){
			mCallback.onSuccess(mFiles);
		}else{
			mCallback.onFailed();
		}
	}
}
