package project.com.pockethistory;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesRecyclerAdapter extends RecyclerView.Adapter<FilesRecyclerAdapter.FilesRecyclerViewHolder> {
    private LayoutInflater inflater;
    private File[] stored_filenames;

    public FilesRecyclerAdapter(Context context, File[] stored_filenames) {
        inflater = LayoutInflater.from(context);
        this.stored_filenames = stored_filenames;
    }

    @Override
    public FilesRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = inflater.inflate(R.layout.file_row, viewGroup, false);
        FilesRecyclerViewHolder holder = new FilesRecyclerViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(FilesRecyclerViewHolder fileRecyclerViewHolder, int i) {
        String filename = stored_filenames[i].getName();
        String regex = "([a-zA-Z]+)_([a-zA-Z0-9\\s_-]+).dat";

        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(filename);

        if(m.find()) {
            fileRecyclerViewHolder.filename.setText(m.group(2));
        }
    }

    @Override
    public int getItemCount() {
        return stored_filenames.length;
    }

    static class FilesRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView filename;
        public FilesRecyclerViewHolder(View itemView) {
            super(itemView);
            filename = (TextView) itemView.findViewById(R.id.filename);
        }
    }
}