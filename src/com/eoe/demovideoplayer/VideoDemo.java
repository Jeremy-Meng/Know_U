package com.eoe.demovideoplayer;

import java.util.ArrayList;
import java.util.List;





import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VideoDemo extends Activity {  
    
    private Cursor cursor;  
      
    private ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();
    
    private ListView videoListView;
    
    public void onCreate(Bundle savedInstanceState){  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_video_demo);  
        
        videoListView=(ListView)findViewById(R.id.listView1);
        init();  
        
        videoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent playIntent=new Intent(VideoDemo.this, PlayActivity.class);
				String pathString=videoList.get(arg2).filePath;
				String name = videoList.get(arg2).title;
				playIntent.putExtra("name", name);
				playIntent.putExtra("path", pathString);
				VideoDemo.this.startActivity(playIntent);
			}
		});
    }  
      
      
    private void init(){  
        String[] thumbColumns = new String[]{  
                MediaStore.Video.Thumbnails.DATA,  
                MediaStore.Video.Thumbnails.VIDEO_ID  
        };  
          
        String[] mediaColumns = new String[]{  
                MediaStore.Video.Media.DATA,  
                MediaStore.Video.Media._ID,  
                MediaStore.Video.Media.TITLE,  
                MediaStore.Video.Media.MIME_TYPE  
        };  
          
        //首先检索SDcard上所有的video  
        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);  
          
       
          
        if(cursor.moveToFirst()){  
            do{  
                VideoInfo info = new VideoInfo();  
                  
                info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));  
                info.mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));  
                info.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));  
                  
                Log.d("-name debug-", info.title+"    "+info.filePath);
                
                //获取当前Video对应的Id，然后根据该ID获取其Thumb  
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));  
                BitmapFactory.Options options = new BitmapFactory.Options();  
                options.inDither = false;  
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;  
                info.b = MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), id,  Images.Thumbnails.MICRO_KIND, options);                  
                  
                //然后将其加入到videoList  
                videoList.add(info);  
                  
            }while(cursor.moveToNext());  
        }  
          
        //然后需要设置ListView的Adapter了，使用我们自定义的Adatper  
        VideoAdapter adapter = new VideoAdapter(this);  
        videoListView.setAdapter(adapter);  
    }  
      
    class VideoInfo{  
        String filePath;  
        String mimeType;  
        Bitmap b;  
        String title;  
    }  
      
    
    
    class ViewHolder{  
        ImageView thumbImage;  
        TextView titleText;  
    }  
 
    private class VideoAdapter extends BaseAdapter{  
        
        private Context mContext;  
        private LayoutInflater inflater;  
          
        public VideoAdapter(Context context){  
            this.mContext = context;  
            this.inflater = LayoutInflater.from(context);   
            this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    
        }  
        @Override  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return videoList.size();  
        }  
        @Override  
        public Object getItem(int p) {  
            // TODO Auto-generated method stub  
            return videoList.get(p);  
        }  
        @Override  
        public long getItemId(int p) {  
            // TODO Auto-generated method stub  
            return p;  
        }  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            ViewHolder holder ;  
            if(convertView == null){  
                holder = new ViewHolder();  
                convertView = inflater.inflate(R.layout.row, null);  
                holder.thumbImage = (ImageView)convertView.findViewById(R.id.icon);  
                holder.titleText = (TextView)convertView.findViewById(R.id.videoName);  
                convertView.setTag(holder);  
            }else{  
                holder = (ViewHolder)convertView.getTag();  
            }  
              
            //显示信息  
            holder.titleText.setText(videoList.get(position).title+"."+(videoList.get(position).mimeType).substring(6));  
            if(videoList.get(position).b != null){  
                holder.thumbImage.setImageBitmap(videoList.get(position).b);  
            }  
              
            return convertView;  
        }  
          
    }  
}