package com.tlabs.android.evanova.app.launcher.impl;

import android.content.Context;

import com.tlabs.android.evanova.R.string;
import com.tlabs.android.evanova.app.launcher.LauncherUseCase;
import com.tlabs.android.util.Environment;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

final class StorageInitOperation implements LauncherUseCase.Operation {
	private static final Logger LOG = LoggerFactory.getLogger(StorageInitOperation.class);

	@Override
	public void execute(Context context) {
		if (Environment.isExternalStorageAvailable(true)) {
			initSD(context);
		}
	}

	@Override
	public boolean runOnce() {
		return true;
	}

	private static boolean initSD(final Context context) {
		final String EVANOVA = 
				Environment.getExternalStorageDirectory().getAbsolutePath() +
			"/" + context.getResources().getString(string.app_dir) + "/";
		final String OLD = 
				Environment.getExternalStorageDirectory().getAbsolutePath() +
			"/evanova/";			
		
		File oldDir = new File(OLD);			
		File evanovaDir = new File(EVANOVA);			
		if (!evanovaDir.exists()) {
			if (!oldDir.equals(evanovaDir) && oldDir.exists()) {
				oldDir.renameTo(evanovaDir);
			}
			else {
				evanovaDir.mkdirs();
			}
    	}
		initSDNoMedia(EVANOVA);
		//add a sample api_keys.txt file when none exists
		initDefaultApiKeysFile(context, EVANOVA);
		
		return true;
	}
	
	private static void initSDNoMedia(String root) {
		try {
			File rootDir = new File(root);
			if (!rootDir.exists()) {
				rootDir.mkdirs();
			}
			
			File noMedia = new File(rootDir, ".nomedia");		    	
	    	if (!noMedia.exists()) {
	    		noMedia.createNewFile();
	    	}		    	
		}
		catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
		}
	}		
	
	private static void initDefaultApiKeysFile(final Context context, String root) {
	    File apiKeys = new File(root, "api_keys.txt");
        if (apiKeys.exists()) {
            return;
        }
        
        Reader r = null;
        Writer w = null;
        try {
			apiKeys.mkdirs();
            if (apiKeys.createNewFile()) {
                r = new InputStreamReader(context.getAssets().open("help/api_keys.txt"));
                w = new FileWriter(apiKeys);
                IOUtils.copy(r, w);
            }
            else {
                LOG.warn(".initDefaultApiKeysFile: could not create default api_keys.txt file.");
            }
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
        finally {
            IOUtils.closeQuietly(w);
            IOUtils.closeQuietly(r);
        }       
	}
}