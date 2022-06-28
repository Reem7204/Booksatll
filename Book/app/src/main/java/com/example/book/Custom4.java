package com.example.book;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class Custom4 extends BaseAdapter{
    private Context context;

    ArrayList<String> title,author,price,apimages,rating;
    SharedPreferences sh;




    public Custom4(Context applicationContext, ArrayList<String> title, ArrayList<String> author, ArrayList<String> price, ArrayList<String> apimages, ArrayList<String> rating ) {
        // TODO Auto-generated constructor stub
        this.context=applicationContext;
        this.title=title;
        this.author=author;
        this.price=price;
        this.apimages=apimages;
        this.rating=rating;
        sh= PreferenceManager.getDefaultSharedPreferences(applicationContext);


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return title.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getItemViewType(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(convertview==null)
        {
            gridView=new View(context);
            gridView=inflator.inflate(R.layout.activity_custom4, null);

        }
        else
        {
            gridView=(View)convertview;

        }
        ImageView image=(ImageView)gridView.findViewById(R.id.imgbook2);
        TextView tvtitle=(TextView)gridView.findViewById(R.id.title4);
        TextView tvauthor=(TextView)gridView.findViewById(R.id.author4);
        TextView tvprice=(TextView)gridView.findViewById(R.id.price4);
        TextView tvrating=(TextView)gridView.findViewById(R.id.rating4);


        tvtitle.setText(title.get(position));
        tvauthor.setText(" by "+author.get(position));
        tvrating.setText( "Rating : "+rating.get(position));
        tvprice.setText( "Rs"+price.get(position));



        //new RetrieveFeedTask(position).execute(image);
        Picasso.with(context)
                .load("http://"+sh.getString("ip","")+":5000/static/bookpic/"+apimages.get(position))
                .into(image);


//        tv1.setTextColor(Color.BLACK);
//        tv2.setTextColor(Color.BLACK);
//
//








        return gridView;

    }

    class RetrieveFeedTask extends AsyncTask<ImageView, Void, Void> {

        int position;

        public RetrieveFeedTask(int position) {
            this.position = position;
        }

        @Override
        protected Void doInBackground(ImageView... imageViews) {
            try {

                URL thumb_u = new java.net.URL("http://"+sh.getString("ip","")+":5000/static/bookpic/"+apimages.get(position));
                Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
                imageViews[0].setImageDrawable(thumb_d);

            } catch (Exception e) {
                Log.d("errsssssssssssss",""+e);

            }
            return null;
        }

        protected void onPostExecute(Void feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

}
