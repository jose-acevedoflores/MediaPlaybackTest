package msrp.aida.mediaplaybacktest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	private MediaPlayer player;
	private TextView tv;
	private TelephonyManager tm;
	private Cursor cu;
	private ContentResolver cr ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		player = new MediaPlayer();
		tv = (TextView) findViewById(R.id.tvMain);
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		cr = this.getContentResolver();

		//cu = cr.query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 
		//		null, null, null, null);
		
		this.printGenres();
		//this.printSongs();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		if(player != null && player.isPlaying())
			player.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		if(player != null)
			player.start();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		player.stop();
		player.release();
		player = null;
		//tm = null;
		Log.d("debug", "Destroyed");
		super.onDestroy();
	}

	//-----------------------------------------------------------------------------------------	

	public void playRandSong()
	{
		ArrayList<Long> ids = new ArrayList<Long>(); 
		ArrayList<String> title = new ArrayList<String>();
		int titleColumn = cu.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
		int idColumn = cu.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
		int isMusicCol = cu.getColumnIndex(android.provider.MediaStore.Audio.Media.IS_MUSIC);
		do {
			long thisId = cu.getLong(idColumn);
			String thisTitle = cu.getString(titleColumn);
			String isMusic = cu.getString(isMusicCol);
			if(isMusic.equals("1"))
			{
				ids.add(thisId);
				title.add(thisTitle);
			}

		} while (cu.moveToNext());

		int rand = new Random().nextInt(ids.size());
		long thisId = ids.get(rand);
		String songTitle = title.get(rand);

		Uri songUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, thisId);

		tm.listen(new MyPhoneStateListener() , PhoneStateListener.LISTEN_CALL_STATE);

		player.setAudioStreamType(AudioManager.STREAM_MUSIC);

		try {
			tv.setText(songTitle);
			player.setDataSource(getApplicationContext(), songUri);
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void printGenres()
	{
		
		cu = cr.query(android.provider.MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, null, null, null, null);

		if(cu == null)
		{
			System.out.println("Error in media query");
		}
		else if(!cu.moveToFirst())
		{
			System.out.println("No music on phone");
		}
		else
		{
			int genre = cu.getColumnIndex(android.provider.MediaStore.Audio.Genres.NAME);
			int id = cu.getColumnIndex(android.provider.MediaStore.Audio.Genres._ID);
			Uri temp;
			Cursor cu2;
			int songName;
			
			do{
				System.out.println("Genre: "+cu.getString(genre) + " -- id " + cu.getString(id) );
				
				//Query the MediaStore by genres
				//Fisrt build a query with the genre ID 
				temp = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, 
						Integer.valueOf(cu.getString(id)));
				//Then get the path to the members directory.
				temp = Uri.withAppendedPath(temp, android.provider.MediaStore.Audio.Genres.Members.CONTENT_DIRECTORY);
				
				cu2 = cr.query(temp, null, null, null, null);
				songName = cu2.getColumnIndex(android.provider.MediaStore.Audio.Genres.Members.TITLE);
				if(cu2 != null )
				{	
					while(cu2.moveToNext())
						System.out.println("Song: "+cu2.getString(songName));
				}
				
			}while(cu.moveToNext());
		}
	}
	
	public void printArtist()
	{
		
	}

	public void printSongs()
	{
		cu = cr.query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

		if(cu == null)
		{
			System.out.println("Error in media query");
		}
		else if(!cu.moveToFirst())
		{
			System.out.println("No music on phone");
		}
		else
		{
			int titleColumn = cu.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
			int idColumn = cu.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
			do{
				System.out.println(cu.getString(titleColumn) + " id "+cu.getString(idColumn) );
			}while(cu.moveToNext());
		}
	}

	//-----------------------------------------------------------------------------------------	
	/**
	 * 
	 * @author joseacevedo
	 *
	 */
	class MyPhoneStateListener extends PhoneStateListener
	{
		@Override
		public void onCallStateChanged(int state, String incomingNumber)
		{
			System.out.println("incoming number: " + incomingNumber);
			if(state == TelephonyManager.CALL_STATE_RINGING)
				Log.d("DEBUG", "Ringing");
			super.onCallStateChanged(state, incomingNumber);
		}
	}

}
