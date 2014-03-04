package fragments;

import java.util.ArrayList;

import com.tsegaab.dynamic.R;
import com.tsegaab.dynamic.R.id;
import com.tsegaab.dynamic.R.layout;
import com.tsegaab.dynamic.objects.Source;

import database.DbHandler;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoadingFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View lView = inflater.inflate(R.layout.sources_layout, container,
				false);
		ViewGroup loading = (ViewGroup) lView
				.findViewById(R.id.sources_container_no_2);
		return lView;
	}
}
