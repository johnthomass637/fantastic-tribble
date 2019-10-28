package droidmentor.tabwithviewpager.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

import droidmentor.tabwithviewpager.R;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;

public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<dashboardclass> data= Collections.emptyList();
    dashboardclass current;
    int currentPos=0;


    public DashboardAdapter(Context context, List<dashboardclass> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.dashboard_container, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list


        MyHolder myHolder= (MyHolder) holder;
        String customFont = "alegreya-sans-v10-latin-regular.ttf";
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), customFont);

        dashboardclass current=data.get(position);
        myHolder.caseHolderName.setTypeface(typeface);
        myHolder.caseHolderName.setText( StringUtils.capitalize(current.Name));

        myHolder.caseId.setText("caseID: " + current.caseID);
        myHolder.caseStatus.setText(current.Status );
        if(current.Status.equalsIgnoreCase("PENDING")) {
            myHolder.caseStatus.setTextColor(ContextCompat.getColor(context, R.color.orange));
       myHolder.caseStatus.setBackground(ContextCompat.getDrawable(context,R.drawable.status_background));
        }
        else if(current.Status.equalsIgnoreCase("DENIED"))
        {

            myHolder.caseStatus.setBackground(ContextCompat.getDrawable(context,R.drawable.denied_status_ground));
            myHolder.caseStatus.setTextColor(ContextCompat.getColor(context, R.color.colorTextPrimary));
        }
        else
        {
            myHolder.caseStatus.setBackground(ContextCompat.getDrawable(context,R.drawable.submitted_status_ground));
            myHolder.caseStatus.setTextColor(ContextCompat.getColor(context, R.color.colorTextPrimary));
        }
        if(current.Name.equalsIgnoreCase("Sabari")) {
            myHolder.caseHolderImage.setImageResource(R.mipmap.sabari);
        } else if(current.Name.equalsIgnoreCase("kevin"))
        {
            myHolder.caseHolderImage.setImageResource(R.mipmap.kevin);
        }
        else if(current.Name.equalsIgnoreCase("siva"))
        {
            myHolder.caseHolderImage.setImageResource(R.mipmap.siva);
        }
        else if(current.Name.equalsIgnoreCase("smith"))
        {
            myHolder.caseHolderImage.setImageResource(R.mipmap.sruithin);
        }
        else if(current.Name.equalsIgnoreCase("jack"))
        {
            myHolder.caseHolderImage.setImageResource(R.mipmap.surender);
        }
        else if(current.Name.equalsIgnoreCase("john"))
        {
            myHolder.caseHolderImage.setImageResource(R.mipmap.jestin);
        }

        else
        {
            myHolder.caseHolderImage.setImageResource(R.mipmap.sabu);
        }
        // load image into imageview using glide

//                .placeholder(R.mipmap.chatboticon)
//                .error(R.mipmap.chatboticon)
//                .into(myHolder.ivFish);

    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}
