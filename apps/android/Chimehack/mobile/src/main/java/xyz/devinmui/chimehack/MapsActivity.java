package xyz.devinmui.chimehack;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspClient;
import net.majorkernelpanic.streaming.video.VideoQuality;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Session.Callback, RtspClient.Callback, SurfaceHolder.Callback, LocationListener {

    private GoogleMap mMap;

    private Session mSession;
    private RtspClient mClient;
    private SurfaceView mSurfaceView;

    Api mApi = new Api();

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Configures the SessionBuilder
        mSession = SessionBuilder.getInstance()
                .setContext(getApplicationContext())
                .setAudioEncoder(SessionBuilder.AUDIO_AAC)
                .setAudioQuality(new AudioQuality(8000,16000))
                .setVideoEncoder(SessionBuilder.VIDEO_H264)
                .setSurfaceView(mSurfaceView)
                .setPreviewOrientation(0)
                .setCallback(this)
                .build();

        // Configures the RTSP client
        mClient = new RtspClient();
        mClient.setSession(mSession);
        mClient.setCallback(this);

        // Use this to force streaming with the MediaRecorder API
        //mSession.getVideoTrack().setStreamingMethod(MediaStream.MODE_MEDIARECORDER_API);

        // Use this to stream over TCP, EXPERIMENTAL!
        //mClient.setTransportMode(RtspClient.TRANSPORT_TCP);

        // Use this if you want the aspect ratio of the surface view to
        // respect the aspect ratio of the camera preview
        //mSurfaceView.setAspectRatioMode(SurfaceView.ASPECT_RATIO_PREVIEW);

        mSurfaceView.getHolder().addCallback(this);

        selectQuality();


        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        toggleStream();
    }

    private void selectQuality() {

        mSession.setVideoQuality(new VideoQuality(640, 480, 30, 1024));

    }

    // Connects/disconnects to the RTSP server and starts/stops the stream
    public void toggleStream() {
        if (!mClient.isStreaming()) {
            String ip,port,path;

            // We parse the URI written in the Editext
            ip = "192.168.1.40";
            port = "1935";
            path = "foobar/test.stream";

            mClient.setCredentials("devin", "foobar");
            mClient.setServerAddress(ip, Integer.parseInt(port));
            mClient.setStreamPath("/"+path);
            mClient.startStream();

        } else {
            // Stops the stream and disconnects from the RTSP server
            mClient.stopStream();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onBitrateUpdate(long bitrate) {

    }

    @Override
    public void onSessionError(int reason, int streamType, Exception e) {

    }

    @Override
    public void onPreviewStarted() {

    }

    @Override
    public void onSessionConfigured() {

    }

    @Override
    public void onSessionStarted() {

    }

    @Override
    public void onSessionStopped() {

    }

    @Override
    public void onRtspUpdate(int message, Exception exception) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSession.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mClient.stopStream();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mClient.release();
        mSession.release();
        mSurfaceView.getHolder().removeCallback(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    private class Task extends AsyncTask<String, Void, String> {
        private Context mContext;

        public Task (Context context){
            mContext = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // do stuff
            while(true) {
                String json = "{\"lat\":" + latitude + ", \"long\":" + longitude + "}";
                try {
                    mApi.post("/gps", json, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
