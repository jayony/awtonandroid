package julianwi.awtpeer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class WindowActivity extends Activity implements Callback {
	
	public DataOutputStream pipeout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!new File("/data/data/julianwi.awtpeer/pipe").exists()){
			try {
				String bbpath = createPackageContext("julianwi.javainstaller", 0).getSharedPreferences("settings", 1).getString("path1", "/data/data/jackpal.androidterm/bin")+"/busybox";
				System.out.println(bbpath);
				Runtime.getRuntime().exec(bbpath+" mkfifo /data/data/julianwi.awtpeer/pipe");
				Runtime.getRuntime().exec(bbpath+" mkfifo /data/data/julianwi.awtpeer/returnpipe");
				Runtime.getRuntime().exec(bbpath+" chmod 0666 /data/data/julianwi.awtpeer/pipe");
				Runtime.getRuntime().exec(bbpath+" chmod 0666 /data/data/julianwi.awtpeer/returnpipe");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			Window.class.getMethod("takeSurface", Class.forName("android.view.SurfaceHolder$Callback2")).invoke(getWindow(), Class.forName("julianwi.awtpeer.WindowNewApi").getConstructor().newInstance());
			System.out.println("succes!");
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
			//view = new GraphicsView(this);
			SurfaceView view = new SurfaceView(this);
			view.getHolder().addCallback(this);
			setContentView(view);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new PipeListener(holder).start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		System.out.println("surfaceChanged "+width+" * "+height);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(-1<ev.getAction()&&ev.getAction()<3){
			try {
				if(pipeout==null)pipeout = new DataOutputStream(new FileOutputStream("/data/data/julianwi.awtpeer/returnpipe"));
				pipeout.write(0x02+ev.getAction());
				pipeout.writeFloat(ev.getX());
				pipeout.writeFloat(ev.getY());
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
}