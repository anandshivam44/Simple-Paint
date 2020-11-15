package com.teamvoyager.simplesketch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.larswerkman.holocolorpicker.ColorPicker;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.feio.android.checklistview.utils.AlphaManager;

public class SketchFragment extends Fragment implements OnDrawChangedListener {


    @BindView(R.id.sketch_stroke)
    ImageView stroke;
    @BindView(R.id.sketch_eraser)
    ImageView eraser;
    @BindView(R.id.drawing)
    SketchView mSketchView;
    @BindView(R.id.sketch_undo)
    ImageView undo;
    @BindView(R.id.sketch_redo)
    ImageView redo;
    @BindView(R.id.sketch_erase)
    ImageView erase;
    @BindView(R.id.sketch_save)
    ImageView sketchSave;
    @BindView(R.id.sketch_menu)
    ImageView sketchMenu;

    ImageButton cancel;


    private int seekBarStrokeProgress;
    private int seekBarEraserProgress;
    private View popupLayout;
    private View popupEraserLayout;
    private ImageView strokeImageView;
    private ImageView eraserImageView;
    private int size;
    private ColorPicker mColorPicker;
    PopupWindow popup;
//    ImplementAdClick object;
    RewardedAd rewardedAd;
    TriggerEvent triggerEvent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);//it was false before


    }


    @Override
    public void onStart() {
//        ((OmniNotes) getActivity().getApplication()).getAnalyticsHelper().trackScreenView(getClass().getName());

        super.onStart();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity= (Activity) context;
        triggerEvent= (TriggerEvent) activity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sketch, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        getMainActivity().getToolbar().setNavigationOnClickListener(v -> getActivity().onBackPressed());//made changes here

        mSketchView.setOnDrawChangedListener(this);
//        object = getContext();

//        Uri baseUri = getArguments().getParcelable("base");
//        if (baseUri != null) {
//            Bitmap bmp;
//            try {
//                bmp = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(baseUri));
//                mSketchView.setBackgroundBitmap(getActivity(), bmp);
//            } catch (FileNotFoundException e) {
////                LogDelegate.e("Error replacing sketch bitmap background", e);
//                Toast.makeText(getActivity(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }

        // Show the Up button in the action bar.
//        if (getMainActivity().getSupportActionBar() != null) {
//            getMainActivity().getSupportActionBar().setDisplayShowTitleEnabled(true);
//            getMainActivity().getSupportActionBar().setTitle(R.string.title_activity_sketch);
//            getMainActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        stroke.setOnClickListener(v -> {
            if (mSketchView.getMode() == SketchView.STROKE) {
                showPopup(v, SketchView.STROKE);
            } else {
                mSketchView.setMode(SketchView.STROKE);
                AlphaManager.setAlpha(eraser, 0.4f);
                AlphaManager.setAlpha(stroke, 1f);
            }
        });

        AlphaManager.setAlpha(eraser, 0.4f);
        eraser.setOnClickListener(v -> {
            if (mSketchView.getMode() == SketchView.ERASER) {
                showPopup(v, SketchView.ERASER);
            } else {
                mSketchView.setMode(SketchView.ERASER);
                AlphaManager.setAlpha(erase, 0.4f);
                AlphaManager.setAlpha(stroke, 0.4f);
                AlphaManager.setAlpha(eraser, 1f);
            }
        });


        undo.setOnClickListener(v -> mSketchView.undo());

        redo.setOnClickListener(v -> mSketchView.redo());

        erase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlphaManager.setAlpha(erase, 1f);
                AlphaManager.setAlpha(stroke, 0.4f);
                AlphaManager.setAlpha(eraser, 0.4f);
                askForErase();
            }

            private void askForErase() {
                new MaterialDialog.Builder(getActivity())
                        .content(R.string.erase_sketch)
                        .positiveText(R.string.confirm)
                        .onPositive((dialog, which) -> mSketchView.erase()).dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        AlphaManager.setAlpha(erase, 0.4f);
                        if (mSketchView.getMode() == SketchView.STROKE) {
                            AlphaManager.setAlpha(stroke, 1f);
                            AlphaManager.setAlpha(eraser, 0.4f);
                        } else if (mSketchView.getMode() == SketchView.ERASER) {
                            AlphaManager.setAlpha(stroke, 0.4f);
                            AlphaManager.setAlpha(eraser, 1f);
                        }
                    }
                }).build().show();
            }
        });
        sketchSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlphaManager.setAlpha(sketchSave, 1f);
                AlphaManager.setAlpha(stroke, 0.4f);
                AlphaManager.setAlpha(eraser, 0.4f);
                saveSketch();
            }
        });
        sketchMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupOption(v);
            }
        });


        // Inflate the popup_layout.XML
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
                AppCompatActivity.LAYOUT_INFLATER_SERVICE);
        popupLayout = inflater.inflate(R.layout.popup_sketch_stroke, null);
        // And the one for eraser
        LayoutInflater inflaterEraser = (LayoutInflater) getActivity().getSystemService(
                AppCompatActivity.LAYOUT_INFLATER_SERVICE);
        popupEraserLayout = inflaterEraser.inflate(R.layout.popup_sketch_eraser, null);

        // Actual stroke shape size is retrieved
        strokeImageView = popupLayout.findViewById(R.id.stroke_circle);
        final Drawable circleDrawable = getResources().getDrawable(R.drawable.circle);
        size = circleDrawable.getIntrinsicWidth();
        // Actual eraser shape size is retrieved
        eraserImageView = popupEraserLayout.findViewById(R.id.stroke_circle);
        size = circleDrawable.getIntrinsicWidth();

        setSeekbarProgress(SketchView.DEFAULT_STROKE_SIZE, SketchView.STROKE);
        setSeekbarProgress(SketchView.DEFAULT_ERASER_SIZE, SketchView.ERASER);

        // Stroke color picker initialization and event managing
        mColorPicker = popupLayout.findViewById(R.id.stroke_color_picker);
        mColorPicker.addSVBar(popupLayout.findViewById(R.id.svbar));
        mColorPicker.addOpacityBar(popupLayout.findViewById(R.id.opacitybar));
        mColorPicker.setOnColorChangedListener(mSketchView::setStrokeColor);
        mColorPicker.setColor(mSketchView.getStrokeColor());
        mColorPicker.setOldCenterColor(mSketchView.getStrokeColor());
        cancel = popupLayout.findViewById(R.id.cancel_1);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        ImageButton cancel2 = popupEraserLayout.findViewById(R.id.cancel_2);
        cancel2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }

    private void saveSketch() {

//        Intent intent = new Intent(getContext(), SaveSketch.class);
        View root = getView().findViewById(R.id.drawing);
        root.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
        root.setDrawingCacheEnabled(false);
        BitmapHelper.getInstance().setBitmap(bitmap);
        if (bitmap == null) {
//            Toast.makeText(getContext(), "NULL Bitmap", Toast.LENGTH_SHORT).show();
        }
        triggerEvent.showAds();
//        startActivity(intent);
    }


    @SuppressLint("RestrictedApi")
    private void showPopupOption(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenuInflater().inflate(R.menu.action_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menu_item) {
                switch (menu_item.getItemId()) {
                    case R.id.one://

                        saveSketch();

                        break;

//                    case R.id.two:// about us
//
//                        break;

                    case R.id.three://about us
                        Uri uri1 = Uri.parse("https://www.facebook.com/teamvoyagerfb"); // missing 'http://' will cause crashed
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
                        startActivity(intent1);
                        break;
                    case R.id.four:
                        Uri uri2 = Uri.parse("https://play.google.com/store/apps/details?id=com.teamvoyager.myapplication"); // missing 'http://' will cause crashed
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
                        startActivity(intent2);
                        //https://play.google.com/store/apps/details?id=com.teamvoyager.myapplication
                    case R.id.five://rate us

                        triggerEvent.showAds();

//                        showAdd();

                        break;
                }
                return true;
            }
        });

        MenuPopupHelper menuHelper = new MenuPopupHelper(getContext(), (MenuBuilder) popup.getMenu(), v);
        menuHelper.setForceShowIcon(true);
        menuHelper.setGravity(Gravity.END);
        menuHelper.show();

    }

//    private void showAdd() {
//
//
//
//        rewardedAd = new RewardedAd(getActivity(),
//                "ca-app-pub-3940256099942544/5224354917");
//        //ca-app-pub-3940256099942544/5224354917 test
//        //ca-app-pub-5098396899135570/5051046004 working
//        rewardedAd=new RewardedAd(getActivity(),"ca-app-pub-3940256099942544/5224354917");
////
//
//
//        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
//            @Override
//            public void onRewardedAdLoaded() {
//                // Ad successfully loaded.
//            }
//
//            @Override
//            public void onRewardedAdFailedToLoad(int errorCode) {
//                // Ad failed to load.
//            }
//        };
//
//        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        } else {
//            LogDelegate.e("Wrong element choosen: " + item.getItemId());
//            Toast.makeText(getActivity(), "Error Wrong Element chosen e", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    public void save() {
        Bitmap bitmap = mSketchView.getBitmap();
        if (bitmap != null) {

            try {
                Uri uri = getArguments().getParcelable(MediaStore.EXTRA_OUTPUT);
                File bitmapFile = new File(uri.getPath());
                FileOutputStream out = new FileOutputStream(bitmapFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
//                if (bitmapFile.exists()) {
//                    getMainActivity().sketchUri = uri;
//                } else {
//                    getMainActivity().showMessage("Error", ONStyle.ALERT);
//                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error in saving e=" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showPopup(View anchor, final int eraserOrStroke) {

        boolean isErasing = eraserOrStroke == SketchView.ERASER;

        int oldColor = mColorPicker.getColor();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Creating the PopupWindow
        popup = new PopupWindow(getActivity());
        popup.setContentView(isErasing ? popupEraserLayout : popupLayout);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(true);
        popup.setOnDismissListener(() -> {
            if (mColorPicker.getColor() != oldColor) {
                mColorPicker.setOldCenterColor(oldColor);
//                Toast.makeText(getContext(), "Popup dismissed", Toast.LENGTH_SHORT).show();
            }
        });

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets (transformed
        // dp to pixel to support multiple screen sizes)
        popup.showAsDropDown(anchor);

        // Stroke size seekbar initialization and event managing
        SeekBar mSeekBar;
        mSeekBar = (SeekBar) (isErasing ? popupEraserLayout
                .findViewById(R.id.stroke_seekbar) : popupLayout
                .findViewById(R.id.stroke_seekbar));
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Nothing to do
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Nothing to do
            }


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // When the seekbar is moved a new size is calculated and the new shape
                // is positioned centrally into the ImageView
                setSeekbarProgress(progress, eraserOrStroke);
            }
        });
        int progress = isErasing ? seekBarEraserProgress : seekBarStrokeProgress;
        mSeekBar.setProgress(progress);
    }


    protected void setSeekbarProgress(int progress, int eraserOrStroke) {
        int calcProgress = progress > 1 ? progress : 1;

        int newSize = Math.round((size / 100f) * calcProgress);
        int offset = (size - newSize) / 2;
//        LogDelegate.v("Stroke size " + newSize + " (" + calcProgress + "%)");

        LayoutParams lp = new LayoutParams(newSize, newSize);
        lp.setMargins(offset, offset, offset, offset);
        if (eraserOrStroke == SketchView.STROKE) {
            strokeImageView.setLayoutParams(lp);
            seekBarStrokeProgress = progress;
        } else {
            eraserImageView.setLayoutParams(lp);
            seekBarEraserProgress = progress;
        }

        mSketchView.setSize(newSize, eraserOrStroke);
    }


    @Override
    public void onDrawChanged() {
        // Undo
        if (mSketchView.getPaths().isEmpty()) {

            AlphaManager.setAlpha(undo, 0.4f);
        } else {
            AlphaManager.setAlpha(undo, 1f);
        }
        // Redo
        if (mSketchView.getUndoneCount() > 0) {
            AlphaManager.setAlpha(redo, 1f);
        } else {
            AlphaManager.setAlpha(redo, 0.4f);
        }
    }


    //    private MainActivity getMainActivity () {
//        return (MainActivity) getActivity();
//    }


    public interface TriggerEvent {

        void showAds();


    }




}

