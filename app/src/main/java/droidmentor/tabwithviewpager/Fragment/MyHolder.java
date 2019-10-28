package droidmentor.tabwithviewpager.Fragment;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import droidmentor.tabwithviewpager.R;

class MyHolder extends RecyclerView.ViewHolder{

        TextView caseHolderName;
        ImageView caseHolderImage;
        TextView caseId;
        TextView textType;
        TextView caseStatus;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            caseHolderName= (TextView) itemView.findViewById(R.id.caseHolderName);
            caseHolderImage= (ImageView) itemView.findViewById(R.id.caseHolderImage);
            caseId = (TextView) itemView.findViewById(R.id.caseId);
            textType = (TextView) itemView.findViewById(R.id.textType);
            caseStatus = (TextView) itemView.findViewById(R.id.caseStatus);
        }

    }

