package droidmentor.tabwithviewpager.Fragment;

/**
 * Created by VMac on 17/11/16.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import droidmentor.tabwithviewpager.R;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.parseColor;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;

    private int SELF = 100;
    private int BUTTON = 200;
    private ArrayList<Message> messageArrayList;


    public ChatAdapter(Context context,ArrayList<Message> messageArrayList) {
        this.messageArrayList=messageArrayList;
        this.context=context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        }
        else if(viewType == BUTTON)
        {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.buttonreply, parent, false);
        }
        else {
            // WatBot message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_watson, parent, false);
        }


        return new ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if (message.getId()!=null && message.getId().equals("1")) {
            return SELF;
        }
        else if(message.getId().equals("3") && message.getId()!=null)
        {
            return BUTTON;
        }


        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);

        message.setMessage(message.getMessage());

        if(message.getId().equals("3") && message.getId()!=null && message.getMessage().equalsIgnoreCase("no") ) {
            ((ViewHolder) holder).button.setText(message.getMessage());
            ((ViewHolder) holder).button.setTextColor(WHITE);
            ((ViewHolder) holder).button.setBackground(ContextCompat.getDrawable(context,R.drawable.denied_status_ground));
        }
      else  if(message.getId().equals("3") && message.getId()!=null && message.getMessage().equalsIgnoreCase("Yes")) {
            ((ViewHolder) holder).button.setText(message.getMessage());
            ((ViewHolder) holder).button.setTextColor(WHITE);
            ((ViewHolder) holder).button.setBackground(ContextCompat.getDrawable(context,R.drawable.submitted_status_ground));
        }
        else
            ((ViewHolder) holder).message.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
            return messageArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        Button button;
        public ViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            button=(Button) itemView.findViewById(R.id.buttonrply);
            //TODO: Uncomment this if you want to use a custom Font
            /*String customFont = "Montserrat-Regular.ttf";
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), customFont);
            message.setTypeface(typeface);*/

        }
    }


}